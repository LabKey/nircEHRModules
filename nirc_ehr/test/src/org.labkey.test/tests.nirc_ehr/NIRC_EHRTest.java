/*
 * Copyright (c) 2024 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.labkey.test.tests.nirc_ehr;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.labkey.api.reader.Readers;
import org.labkey.remoteapi.CommandException;
import org.labkey.remoteapi.core.SaveModulePropertiesCommand;
import org.labkey.remoteapi.query.InsertRowsCommand;
import org.labkey.remoteapi.security.CreateUserResponse;
import org.labkey.test.Locator;
import org.labkey.test.TestFileUtils;
import org.labkey.test.WebTestHelper;
import org.labkey.test.categories.EHR;
import org.labkey.test.components.CustomizeView;
import org.labkey.test.components.dumbster.EmailRecordTable;
import org.labkey.test.components.ext4.Window;
import org.labkey.test.components.ui.grids.QueryGrid;
import org.labkey.test.pages.ehr.AnimalHistoryPage;
import org.labkey.test.pages.ehr.EHRAdminPage;
import org.labkey.test.pages.ehr.EHRLookupPage;
import org.labkey.test.pages.ehr.EnterDataPage;
import org.labkey.test.pages.ehr.NotificationAdminPage;
import org.labkey.test.params.ModuleProperty;
import org.labkey.test.tests.ehr.AbstractGenericEHRTest;
import org.labkey.test.util.DataRegionTable;
import org.labkey.test.util.Ext4Helper;
import org.labkey.test.util.LogMethod;
import org.labkey.test.util.PortalHelper;
import org.labkey.test.util.PostgresOnlyTest;
import org.labkey.test.util.ext4cmp.Ext4FieldRef;
import org.labkey.test.util.ext4cmp.Ext4GridRef;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.labkey.test.components.html.Input.Input;

@Category({EHR.class})
public class NIRC_EHRTest extends AbstractGenericEHRTest implements PostgresOnlyTest
{
    private static final String PROJECT_NAME = "NIRC";
    private static final String PROJECT_TYPE = "NIRC EHR";
    private static final File orchardFileLocation = TestFileUtils.getTestTempDir();
    private static String NIRC_BASIC_SUBMITTER = "ac_bs@nirctest.com";
    private static String NIRC_BASIC_SUBMITTER_NAME = "ac bs";
    private static String NIRC_BASIC_SUBMITTER_VET_TECH = "vet_tech_bs@nirctest.com";
    private static String NIRC_FULL_SUBMITTER_VET_TECH = "vet_tech_fs@nirctest.com";
    private static String NIRC_FULL_SUBMITTER_VET = "vet_fs@nirctest.com";
    private static String NIRC_VET_NAME = "vet fs";

    private static String deadAnimalId = "D5454";
    private static String departedAnimalId = "H6767";
    private static String aliveAnimalId = "A4545";
    DateTimeFormatter _dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeClass
    public static void setupProject() throws Exception
    {
        NIRC_EHRTest init = (NIRC_EHRTest) getCurrentTest();
        init.doSetup();
    }

    @Override
    public void importStudy()
    {
        File path = new File(TestFileUtils.getLabKeyRoot(), getModulePath() + "/resources/referenceStudy");
        importFolderByPath(path, getContainerPath(), 1);
        path = TestFileUtils.getSampleData("nirc_ehr/study");
        importFolderByPath(path, getContainerPath(), 2);
    }

    @Override
    protected String getExpectedAnimalIDCasing(String id)
    {
        return id.toUpperCase();
    }

    @LogMethod
    protected void populateProtocolRecords() throws Exception
    {
        final String emailDomain = "@ehrstudy.test";
        CreateUserResponse inves1 = _userHelper.createUser(INVES_ID + emailDomain, true);
        CreateUserResponse inves2 = _userHelper.createUser(DUMMY_INVES + emailDomain, true);
        goToEHRFolder();
        _permissionsHelper.addUserToProjGroup(inves1.getEmail(), getProjectName(), INVESTIGATOR.getGroup());
        _permissionsHelper.addUserToProjGroup(inves2.getEmail(), getProjectName(), INVESTIGATOR.getGroup());

        InsertRowsCommand insertCmd = new InsertRowsCommand("ehr", "protocol");

        Map<String, Object> rowMap = new HashMap<>();
        rowMap.put("protocol", PROTOCOL_ID);
        rowMap.put("InvestigatorId", inves1.getUserId());
        rowMap.put("title", PROTOCOL_ID);
        insertCmd.addRow(rowMap);
        rowMap = new HashMap<>();
        rowMap.put("protocol", DUMMY_PROTOCOL);
        rowMap.put("InvestigatorId", inves2.getUserId());
        rowMap.put("title", DUMMY_PROTOCOL);
        insertCmd.addRow(rowMap);

        insertCmd.execute(createDefaultConnection(), getContainerPath());
    }

    public void importFolderByPath(File path, String containerPath, int finishedJobsExpected)
    {
        setPipelineRoot(path.getPath(), false);

        beginAt(WebTestHelper.getBaseURL() + "/pipeline-status/" + containerPath + "/begin.view");
        clickButton("Process and Import Data", defaultWaitForPage);
        _fileBrowserHelper.expandFileBrowserRootNode();
        _fileBrowserHelper.checkFileBrowserFileCheckbox("folder.xml");
        _fileBrowserHelper.selectImportDataAction("Import Folder");

        Locator cb = Locator.checkboxByName("validateQueries");
        waitForElement(cb);
        uncheckCheckbox(cb);
        clickButton("Start Import");

        waitForPipelineJobsToComplete(finishedJobsExpected, "Folder import", false, MAX_WAIT_SECONDS * 2500);
    }

    @Override
    protected String getModuleDirectory()
    {
        return "nircEHRModules/nirc_ehr";
    }

    @Override
    protected boolean doSetUserPasswords()
    {
        return true;
    }

    @Override
    public String getModulePath()
    {
        return "/server/modules/" + getModuleDirectory();
    }

    @Override
    protected File getStudyPolicyXML()
    {
        return TestFileUtils.getSampleData("nircEHRStudyPolicy.xml");
    }

    @Override
    @LogMethod
    protected void populateInitialData() throws Exception
    {
        FileUtils.deleteQuietly(orchardFileLocation);
        FileUtils.forceMkdir(orchardFileLocation);
        List<ModuleProperty> props = List.of(
                new ModuleProperty("EHR", "/" + getProjectName(), "EHRCustomModule", "NIRC_EHR"),
                new ModuleProperty("NIRC_EHR", "/", "NIRCOrchardFileLocation", orchardFileLocation.getAbsolutePath())
        );
        SaveModulePropertiesCommand command = new SaveModulePropertiesCommand(props);
        command.execute(createDefaultConnection(), "/");

        beginAt(WebTestHelper.buildURL("ehr", getContainerPath(), "populateLookupData", Map.of("manifest", "lookupsManifestTest")));

        waitForElement(Locator.linkWithText("Populate Lookups"));
        click(Locator.linkWithText("Populate Lookups"));
        acceptAlert();

        waitFor(() -> Input(Locator.textarea("populateLookupResults"), getDriver()).waitFor().getValue().contains("Loading lookups is complete."),
                "Lookups didn't finish loading", 60000);

        waitForElement(Locator.linkWithText("Populate Reports"));
        click(Locator.linkWithText("Populate Reports"));
        acceptAlert();

        waitFor(() -> Input(Locator.textarea("populateLookupResults"), getDriver()).waitFor().getValue().contains("Loading reports is complete."),
                "Reports didn't finish loading", 60000);
    }

    @Override
    protected void populateRoomRecords() throws Exception
    {
        InsertRowsCommand insertCmd = new InsertRowsCommand("ehr_lookups", "rooms");
        Map<String, Object> rowMap = new HashMap<>();
        rowMap.put("name", ROOM_ID);
        rowMap.put("floor", "floor1");
        rowMap.put("housingType", 1);
        rowMap.put("housingCondition", 1);
        insertCmd.addRow(rowMap);

        rowMap = new HashMap<>();
        rowMap.put("name", ROOM_ID2);
        rowMap.put("floor", "floor2");
        rowMap.put("housingType", 1);
        rowMap.put("housingCondition", 1);
        insertCmd.addRow(rowMap);

        insertCmd.execute(createDefaultConnection(), getContainerPath());
    }

    private void doSetup() throws Exception
    {
        initProject(PROJECT_TYPE);
        goToEHRFolder();
        createTestSubjects();
        addNIRCEhrLinks();
        addExtensibleCols();
        enableSiteNotification();
        populateLocations();
        addUsersAndPermissions();//create users and assign roles, created for Death/Necropsy form, but users can be repurposed for other forms.
    }

    private void enableSiteNotification()
    {
        log("Enabling the notification at the site level");
        goToAdminConsole().clickNotificationServiceAdmin();
        _ext4Helper.selectComboBoxItem("Status of Notification Service:", "Enabled");
        clickButton("Save", 0);
        _helper.clickExt4WindowBtn("Success", "OK");
    }

    private void enableNotification(String notification)
    {
        goToEHRFolder();
        _containerHelper.enableModule("Dumbster");
        log("Setup the notification service for this container");
        EHRAdminPage.beginAt(this, getContainerPath());
        NotificationAdminPage notificationAdminPage = EHRAdminPage.clickNotificationService(this);
        notificationAdminPage.setNotificationUserAndReplyEmail(DATA_ADMIN_USER);
        notificationAdminPage.addManageUsers("org.labkey.nirc_ehr.NIRCDeathNotification", "EHR Administrators");
        notificationAdminPage.enableRequestAdminAlerts(notification);
    }

    @LogMethod
    private void populateLocations() throws IOException, CommandException
    {
        goToEHRFolder();
        log("Inserting values in rooms");
        InsertRowsCommand roomCmd = new InsertRowsCommand("ehr_lookups", "rooms");
        roomCmd.addRow(Map.of("name", "R1", "floor", "F1"));
        roomCmd.addRow(Map.of("name", "R2", "floor", "F2"));
        roomCmd.addRow(Map.of("name", "R3", "floor", "F3"));
        roomCmd.execute(getApiHelper().getConnection(), getContainerPath());

        log("Inserting values in cage");
        InsertRowsCommand cageCmd = new InsertRowsCommand("ehr_lookups", "cage");
        cageCmd.addRow(Map.of("location", "L1", "cage", "C1", "room", "R1"));
        cageCmd.addRow(Map.of("location", "L2", "cage", "C2", "room", "R1"));
        cageCmd.addRow(Map.of("location", "L3", "cage", "C3", "room", "R2"));
        cageCmd.addRow(Map.of("location", "L4", "cage", "C4", "room", "R3"));
        cageCmd.execute(getApiHelper().getConnection(), getContainerPath());
    }

    private void addExtensibleCols()
    {
        log("Setup the EHR table definitions");
        EHRAdminPage.beginAt(this, getContainerPath());
        click(Locator.linkWithText("EHR EXTENSIBLE COLUMNS"));
        click(Locator.linkWithText("Load EHR table definitions"));
        waitForElement(Locator.tagWithClass("span", "x4-window-header-text").withText("Success"));
        assertExt4MsgBox("EHR tables updated successfully.", "OK");
        click(Locator.linkWithText("Load EHR_Lookup table definitions"));
        waitForElement(Locator.tagWithClass("span", "x4-window-header-text").withText("Success"));
        assertExt4MsgBox("EHR_Lookups tables updated successfully.", "OK");
    }

    private void addNIRCEhrLinks()
    {
        navigateToFolder(getProjectName(), getFolderName());
        (new PortalHelper(this)).addWebPart("NIRC EHR Links");
    }

    @Test
    public void testSetup()
    {

    }

    @Override
    protected String getProjectName()
    {
        return PROJECT_NAME;
    }

    @Override
    protected String getAnimalHistoryPath()
    {
        return "/ehr/" + PROJECT_NAME + "/animalHistory.view?";
    }

    @Test
    public void testArrivalForm()
    {
        String arrivedAnimal = "30905";
        LocalDateTime now = LocalDateTime.now();

        gotoEnterData();
        waitAndClickAndWait(Locator.linkWithText("Arrivals"));
        lockForm();

        Ext4GridRef arrivals = _helper.getExt4GridForFormSection("Arrivals");
        _helper.addRecordToGrid(arrivals);
        arrivals.setGridCellJS(1, "date", now.minusDays(1).format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_STRING)));
        arrivals.setGridCell(1, "arrivalType", "Non-quarantine Arrival");
        arrivals.setGridCell(1, "acquisitionType", "Lab Transfer (Wild Born)");
        arrivals.setGridCell(1, "Id", arrivedAnimal);
        arrivals.setGridCell(1, "cage", "C1");
        arrivals.setGridCell(1, "project", "640991");
        arrivals.setGridCell(1, "arrivalProtocol", "dummyprotocol");
        arrivals.setGridCell(1, "Id/demographics/gender", "female");
        arrivals.setGridCell(1, "Id/demographics/geographic_origin", "BRAZIL");
        arrivals.setGridCell(1, "Id/demographics/species", "Macaca nemestrina PIG");
        arrivals.setGridCellJS(1, "Id/demographics/birth", now.minusDays(7).format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_STRING)));
        arrivals.setGridCell(1, "sourceFacility", "BIOQUAL, Inc.");
        submitForm("Submit Final", "Finalize");

        goToSchemaBrowser();
        DataRegionTable table = viewQueryData("study", "arrival");
        table.setFilter("Id", "Equals", arrivedAnimal);
        CustomizeView view = table.openCustomizeGrid();
        view.addColumn("cage");
        view.addColumn("project");
        view.addColumn("arrivalProtocol");
        view.applyCustomView();
        Assert.assertEquals("Invalid Arrival record", Arrays.asList(arrivedAnimal), table.getRowDataAsText(0, "Id"));
        Assert.assertEquals("Invalid Arrival record", Arrays.asList("C1"), table.getRowDataAsText(0, "cage"));
        Assert.assertEquals("Invalid Arrival record", Arrays.asList("640991"), table.getRowDataAsText(0, "project"));
        Assert.assertEquals("Invalid Arrival record", Arrays.asList("dummyprotocol"), table.getRowDataAsText(0, "arrivalProtocol"));

        verifyRowCreated("study", "birth", arrivedAnimal, 1);
        verifyRowCreated("study", "assignment", arrivedAnimal, 1);
        verifyRowCreated("study", "protocolAssignment", arrivedAnimal, 1);
        verifyRowCreated("study", "demographics", arrivedAnimal, 1);
        verifyRowCreated("study", "housing", arrivedAnimal, 1);

        log("Verifying Orchard file is created");
        verifyOrchardFileGenerated(arrivedAnimal);
    }

    @Test
    public void testBirthForm()
    {
        String bornAnimal = "80801";
        LocalDateTime now = LocalDateTime.now();

        gotoEnterData();
        waitAndClickAndWait(Locator.linkWithText("Birth"));
        lockForm();

        Ext4GridRef births = _helper.getExt4GridForFormSection("Births");
        _helper.addRecordToGrid(births);
        births.setGridCellJS(1, "date", now.minusDays(1).format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_STRING)));
        births.setGridCell(1, "Id", bornAnimal);
        births.setGridCell(1, "cage", "C3");
        births.setGridCell(1, "project", "795644");
        births.setGridCell(1, "birthProtocol", "protocol101");
        submitForm("Submit Final", "Finalize");

        goToSchemaBrowser();
        DataRegionTable table = viewQueryData("study", "birth");
        table.setFilter("Id", "Equals", bornAnimal);
        Assert.assertEquals("Invalid Birth record", Arrays.asList(bornAnimal), table.getRowDataAsText(0, "Id"));
        Assert.assertEquals("Invalid Birth record", Arrays.asList("C3"), table.getRowDataAsText(0, "cage"));
        Assert.assertEquals("Invalid Birth record", Arrays.asList("795644"), table.getRowDataAsText(0, "project"));
        Assert.assertEquals("Invalid Birth record", Arrays.asList("protocol101"), table.getRowDataAsText(0, "birthProtocol"));

        verifyRowCreated("study", "assignment", bornAnimal, 1);
        verifyRowCreated("study", "protocolAssignment", bornAnimal, 1);
        verifyRowCreated("study", "housing", bornAnimal, 1);
        verifyRowCreated("study", "demographics", bornAnimal, 1);

        log("Verifying Orchard file is created");
        verifyOrchardFileGenerated(bornAnimal);
    }

    @Override
    @Test
    public void testQuickSearch()
    {
        //TODO: Implement this test once Quick Search is customized for NIRC
    }

    public void addUsersAndPermissions()
    {
        //create animal care basic submitter user (ex. this user can 'Submit Death' in Death/Necropsy)
        createUser(NIRC_BASIC_SUBMITTER, "EHR Basic Submitters", null);

        //create a vet tech user with 'EHR Basic Submitters' group (ex. this user can 'Submit Necropsy for Review' in Death/Necropsy)
        createUser(NIRC_BASIC_SUBMITTER_VET_TECH, "EHR Basic Submitters", "EHR Veterinarian Technician");

        //create a vet tech user with 'EHR Full Updaters' group (ex. this user can 'Submit for Review' and 'Submit Final' in cases)
        createUser(NIRC_FULL_SUBMITTER_VET_TECH, "EHR Full Updaters", "EHR Veterinarian Technician");

        //create a vet user with 'EHR Full Updaters' group (ex. this user can 'Submit Final' in Death/Necropsy)
        createUser(NIRC_FULL_SUBMITTER_VET, "EHR Full Updaters", "EHR Veterinarian");

        _permissionsHelper.setPermissions(FULL_UPDATER.getGroup(), "EHR Clinical Entry");

    }

    public void createSubjectsForDeathForm() throws IOException, CommandException
    {
        goToEHRFolder();
        goToSchemaBrowser();
        log("Creating animals");
        getApiHelper().doSaveRows(DATA_ADMIN.getEmail(), getApiHelper().prepareInsertCommand("study", "birth", "lsid",
                new String[]{"Id", "Date", "gender", "QCStateLabel"},
                new Object[][]{
                        {aliveAnimalId, LocalDateTime.now().minusDays(30), "f", "Completed"},
                        {deadAnimalId, LocalDateTime.now().minusDays(30), "m", "Completed"},
                        {departedAnimalId, LocalDateTime.now().minusDays(30), "m", "Completed"},
                }
        ), getExtraContext());

        log("Inserting rows in assignments, protocolAssignment and housing");
        InsertRowsCommand protocol = new InsertRowsCommand("study", "protocolAssignment");
        protocol.addRow(Map.of("Id", aliveAnimalId, "date", LocalDateTime.now().minusDays(10), "protocol", "protocol101", "QCStateLabel", "Completed"));
        protocol.execute(getApiHelper().getConnection(), getContainerPath());

        InsertRowsCommand project = new InsertRowsCommand("study", "assignment");
        project.addRow(Map.of("Id", aliveAnimalId, "date", LocalDateTime.now().minusDays(10), "project", "640991", "QCStateLabel", "Completed"));
        project.execute(getApiHelper().getConnection(), getContainerPath());

        InsertRowsCommand housing = new InsertRowsCommand("study", "housing");
        housing.addRow(Map.of("Id", aliveAnimalId, "date", LocalDateTime.now().minusDays(10), "cage", "C4", "QCStateLabel", "Completed"));
        housing.execute(getApiHelper().getConnection(), getContainerPath());

        log("Marking an animal dead");
        InsertRowsCommand deaths = new InsertRowsCommand("study", "deaths");
        deaths.addRow(Map.of("Id", deadAnimalId, "date", LocalDateTime.now().minusDays(10), "reason", "4"));
        deaths.execute(getApiHelper().getConnection(), getContainerPath());

        log("Marking an animal departed");
        InsertRowsCommand departure = new InsertRowsCommand("study", "departure");
        departure.addRow(Map.of("Id", departedAnimalId, "date", LocalDateTime.now().minusDays(1), "destination", "Oregon NPRC"));
        departure.execute(getApiHelper().getConnection(), getContainerPath());
    }

    private void createUser(String userEmail, String groupName, @Nullable String roleClass)
    {
        _userHelper.createUser(userEmail, false);
        goToEHRFolder();
        if (roleClass != null)
            _permissionsHelper.setUserPermissions(userEmail, roleClass);
        _permissionsHelper.addUserToProjGroup(userEmail, getProjectName(), groupName);
    }

    @Test
    public void testDeathNecropsyForm() throws IOException, CommandException
    {
        enableNotification("status_org.labkey.nirc_ehr.NIRCDeathNotification");
        createSubjectsForDeathForm();

        log("Go to EHR page > Enter Data > Death/Necropsy");
        impersonate(NIRC_BASIC_SUBMITTER);
        gotoEnterData();
        waitAndClickAndWait(Locator.linkWithText("Death/Necropsy"));

        waitForElement(Locator.name("Id"));
        setFormElement(Locator.name("Id"), departedAnimalId);
        waitForText("Id: ERROR: Animal is not at the center.");

        setFormElement(Locator.name("Id"), aliveAnimalId);
        _ext4Helper.selectComboBoxItem("Disposition:", "Euthaniasia (project)");

        Assert.assertFalse(isElementPresent(Locator.linkWithText("Submit Necropsy for Review")));
        Assert.assertFalse(isElementPresent(Locator.linkWithText("Submit Final")));
        submitForm("Submit Death", "Confirm");
        stopImpersonating();

        log("Trigger notifications");
        goToEHRFolder();
        NotificationAdminPage adminPage = NotificationAdminPage.beginAt(this);
        adminPage.clickManuallyTriggerEmail("NIRC Death Notification");

        goToModule("Dumbster");
        EmailRecordTable notifications = new EmailRecordTable(this);
        waitForTextWithRefresh(WAIT_FOR_PAGE + WAIT_FOR_JAVASCRIPT, "Death Notification: " + aliveAnimalId); //wait for more than a min
        notifications.getMessage("Death Notification: " + aliveAnimalId).getBody().
                contains("Animal '" + aliveAnimalId + "' has been declared dead on '" + LocalDateTime.now().format(_dateFormat) + "'.");
        notifications.clickMessage(notifications.getMessageWithSubjectContaining("Death Notification: " + aliveAnimalId));
        String url = Locator.linkWithText("Click here to record Necropsy").findElement(notifications).getAttribute("href");

        log("Entering Necropsy");
        impersonate(NIRC_BASIC_SUBMITTER_VET_TECH);
        beginAt(url);
        Ext4GridRef necropsy = _helper.getExt4GridForFormSection("Necropsy");
        necropsy.expand();
        waitForElement(Locator.name("necropsyWeight"));
        setFormElement(Locator.name("necropsyWeight"), "23");
        scrollIntoView(Locator.linkContainingText("More Actions"));
        _ext4Helper.selectComboBoxItem("Physical Condition:", "Excellent");
        _ext4Helper.selectComboBoxItem("Reason for Examination:", "Natural Death");
        _ext4Helper.selectComboBoxItem("Condition of Specimen:", "Fresh");
        scrollIntoView(Locator.name("diagnosis"));
        _helper.setDataEntryField("identification", "Extra information");
        _helper.setDataEntryField("grossAbnormalities", "Extra leg");
        _helper.setDataEntryField("diagnosis", "Dead");
        _ext4Helper.selectComboBoxItem("Performed By:", NIRC_BASIC_SUBMITTER_NAME);

        log("Entering Tissue Disposition");
        Ext4GridRef tissueDisposition = _helper.getExt4GridForFormSection("Tissue Disposition");
        _helper.addRecordToGrid(tissueDisposition);
        tissueDisposition.setGridCell(1, "necropsyDispositionCode", "Frozen");
        tissueDisposition.setGridCell(1, "necropsyTissue", "Pancreas");
        waitAndClick(_helper.getDataEntryButton("Submit Necropsy for Review"));

        log("Assigning the reviewer");
        Window<?> submitForReview = new Window<>("Submit For Review", getDriver());

        // Make sure to find the element in submitForReview window.
        WebElement assignedToElement = Locator.tagWithNameContaining("input", "assignedTo").findWhenNeeded(submitForReview);
        setFormElement(assignedToElement, _userHelper.getDisplayNameForEmail(NIRC_FULL_SUBMITTER_VET));

        // Entering the text leaves the selection list visible, send 'Enter' to remove it.
        assignedToElement.sendKeys(Keys.ENTER);

        // The 'button' is actually a link tag.
        WebElement submitButton = Locator.tagWithText("a", "Submit").findWhenNeeded(submitForReview);
        scrollIntoView(submitButton);
        doAndWaitForPageToLoad(() -> submitButton.click());

        stopImpersonating();

        log("Verify rows were inserted in appropriate datasets");
        goToEHRFolder();
        verifyRowCreated("study", "necropsy", aliveAnimalId, 1);
        verifyRowCreated("study", "grossPathology", aliveAnimalId, 9);
        verifyRowCreated("study", "tissueDisposition", aliveAnimalId, 1);

        goToEHRFolder();
        impersonate(NIRC_FULL_SUBMITTER_VET);
        EnterDataPage enterDataPage = EnterDataPage.beginAt(this, getContainerPath());
        enterDataPage.clickAllTasksTab();
        waitAndClick(Locator.linkWithText("Death/Necropsy"));
        switchToWindow(1);
        submitForm("Submit Final", "Finalize");
        switchToMainWindow();
        stopImpersonating();

        log("Verify rows were inserted in appropriate datasets");
        goToEHRFolder();
        verifyRowCreated("study", "weight", aliveAnimalId, 1);

        log("Verify animal is marked as dead");
        AnimalHistoryPage historyPage = AnimalHistoryPage.beginAt(this);
        historyPage.searchSingleAnimal(aliveAnimalId);
        waitForText(WAIT_FOR_PAGE, "Dead");
        waitForText("23 kg"); //checking latest weight is updated.

        goToSchemaBrowser();
        DataRegionTable table = viewQueryData("study", "housing");
        table.setFilter("Id", "Equals", aliveAnimalId);
        Assert.assertTrue("End date is not updated for study.housing", table.getDataAsText(0, "endDate").contains(LocalDateTime.now().format(_dateFormat)));

        log("Verify end date in study.assignment");
        goToSchemaBrowser();
        table = viewQueryData("study", "assignment");
        table.setFilter("Id", "Equals", aliveAnimalId);
        Assert.assertTrue("End date is not updated for study.assignment", table.getDataAsText(0, "endDate").contains(LocalDateTime.now().format(_dateFormat)));

        log("Verify end date in study.protocolAssignment");
        goToSchemaBrowser();
        table = viewQueryData("study", "protocolAssignment");
        table.setFilter("Id", "Equals", aliveAnimalId);
        Assert.assertTrue("End date is not updated for study.protocolAssignment", table.getDataAsText(0, "endDate").contains(LocalDateTime.now().format(_dateFormat)));

    }

    @Test
    public void testClinicalCasesWorkflow()
    {
        String animalId = "8377984";

        //Go to NIRC/EHR main page
        goToEHRFolder();

        //Impersonate as NIRC_FULL_SUBMITTER_VET_TECH
        impersonate(NIRC_FULL_SUBMITTER_VET_TECH);

        //Navigate to Enter Data > Clinical Cases
        gotoEnterData();
        clickAndWait(Locator.linkWithText("Clinical Cases"));

        //Fill out Clinical Case section with Id, Date, Open Remark
        setFormElement(Locator.textarea("openRemark"), "Clinical Case WorkFlow - Test");
        setFormElement(Locator.textarea("plan"), "Case plan");
        setFormElement(Locator.name("Id"), animalId);
        _helper.getExt4FieldForFormSection("Clinical Case", "Open Date").setValue(LocalDateTime.now().minusDays(1).format(_dateFormat));
        Assert.assertEquals("Performed by is incorrect ", "vet tech fs", getFormElement(Locator.name("performedby")));

        //Fill out Clinical Remarks section with Date, Remark
         scrollIntoView(Locator.textarea("remark"));
        _helper.getExt4FieldForFormSection("Clinical Remarks", "Date").setValue(LocalDateTime.now().minusDays(1).format(_dateFormat));
        _helper.setDataEntryField("remark", "Clinical Remarks - Test");
        if (null == _helper.getExt4FieldForFormSection("Clinical Remarks", "Remark").getValue())
            _helper.setDataEntryField("remark", "Clinical Remarks - Test");
        waitForTextToDisappear("Remark: WARN: Must enter at least one comment");

        Ext4GridRef weight = _helper.getExt4GridForFormSection("Weights");
        _helper.addRecordToGrid(weight);
        weight.setGridCellJS(1, "date", LocalDateTime.now().minusDays(1).format(_dateFormat));
        weight.setGridCell(1, "weight", "6.000");

        log("Adding Medications/Treatments Orders");
        Ext4GridRef orderGrid = _helper.getExt4GridForFormSection("Medications/Treatments Orders");
        _helper.addRecordToGrid(orderGrid);
        orderGrid.setGridCell(1, "date", LocalDateTime.now().minusDays(1).format(_dateFormat));
        orderGrid.clickDownArrowOnGrid(1, "code");
        orderGrid.setGridCell(1, "code", "Diazepam");
        orderGrid.clickDownArrowOnGrid(1, "frequency");
        orderGrid.setGridCell(1, "frequency", "QID");
        orderGrid.clickDownArrowOnGrid(1, "route");
        orderGrid.setGridCell(1, "route", "IVAG");
        orderGrid.clickDownArrowOnGrid(1, "orderedby");
        orderGrid.setGridCell(1, "orderedby", NIRC_VET_NAME);
        orderGrid.completeEdit();
        submitForm("Submit Final", "Finalize Form");

        log("Completing today's Medication Schedule");
        goToEHRFolder();
        waitAndClickAndWait(Locator.linkWithText("Today's Medication/Treatment Schedule"));
        AnimalHistoryPage animalHistoryPage = new AnimalHistoryPage<>(getDriver());
        DataRegionTable scheduleTable = animalHistoryPage.getActiveReportDataRegion();
        Assert.assertEquals("Incorrect number of rows", 4, scheduleTable.getDataRowCount());
        scheduleTable.link(0, "treatmentRecord").click();
        switchToWindow(1);

        waitForText("Diazepam");
        waitForText(animalId);
        waitForTextToDisappear("Id is required");
        orderGrid = _helper.getExt4GridForFormSection("Medications/Treatments Given");
        submitForm("Submit Final", "Finalize");
        stopImpersonating();

        // TODO: This will be reimplemented in a current PR, this can than be uncommented.
        //Go to NIRC/EHR main page
        goToEHRFolder();
        impersonate(NIRC_FULL_SUBMITTER_VET);

        //Go to 'Active Clinical Cases'
        clickAndWait(Locator.linkWithText("Active Clinical Cases"));

        //Click on 'Case Update' link
        AnimalHistoryPage historyPage = new AnimalHistoryPage<>(getDriver());
        DataRegionTable activeClinicalCases = historyPage.getActiveReportDataRegion();
        activeClinicalCases.link(0, "caseCheck").click();
        switchToWindow(2);

        //Fill out Close Date
        waitForText(animalId);
        waitForTextToDisappear("Id is required");
        _helper.setDataEntryField("s", "Closing the case");
        waitForTextToDisappear("Subjective: WARN: Must enter at least one comment");

        waitForElement(Ext4Helper.Locators.ext4Button("Edit"));
        Ext4Helper.Locators.ext4Button("Edit").findElement(getDriver()).click();
        Ext4FieldRef enddateField = _helper.getExt4FieldForFormSection("Clinical Case", "Close Date");
        if( !enddateField.isVisible())
            Ext4Helper.Locators.ext4Button("Edit").findElement(getDriver()).click(); //click again
        enddateField.setValue(LocalDateTime.now().format(_dateFormat));

        //'Submit Final'
        submitForm("Submit Final", "Finalize Form");

        //Go to NIRC/EHR main page
        goToEHRFolder();
        clickAndWait(Locator.linkWithText("Active Clinical Cases"));

        //Verify that the case is no longer present/is closed
        historyPage = new AnimalHistoryPage<>(getDriver());
        activeClinicalCases = historyPage.getActiveReportDataRegion();
        Assert.assertEquals("No active cases", 0, activeClinicalCases.getDataRowCount());
        stopImpersonating();
    }

    @Override
    @Test
    public void testCalculatedAgeColumns()
    {
        String subjectId = "TEST6390238";

        beginAt(String.format("%s/query-executeQuery.view?schemaName=study&query.queryName=Weight&query.Id~contains=%s", getContainerPath(), subjectId));
        _customizeViewsHelper.openCustomizeViewPanel();
        _customizeViewsHelper.addColumn("ageAtTime/AgeAtTime");
        _customizeViewsHelper.addColumn("ageAtTime/AgeAtTimeYearsRounded");
        _customizeViewsHelper.addColumn("ageAtTime/AgeAtTimeMonths");
        _customizeViewsHelper.applyCustomView();

        DataRegionTable table = new DataRegionTable("query", this);
        int columnCount = table.getColumnCount();
        List<String> row = table.getRowDataAsText(0);
        assertEquals("Calculated ages are incorrect", Arrays.asList("4.8", "4.0", "58.0"), row.subList(columnCount - 3, columnCount));
    }

    @Test
    public void testLookupPage() throws Exception
    {
        goToEHRFolder();
        clickAndWait(Locator.linkWithText("Manage Lookup Tables"));

        EHRLookupPage ehrLookupPage = new EHRLookupPage(this);
        ehrLookupPage.waitForPage();
        QueryGrid grid = ehrLookupPage.getQueryGrid();
        checker().verifyEquals("Missing look up tables", countLines(TestFileUtils.getLabKeyRoot() + getModulePath() + "/resources/data/editable_lookups.tsv") - 1, grid.getRecordCount());

        clickAndWait(Locator.linkWithText("Age Class"));
        checker().verifyEquals("Navigated to incorrect schema", "ehr_lookups", getUrlParam("schemaName"));
        checker().verifyEquals("Navigated to incorrect query", "ageclass", getUrlParam("query.queryName"));
    }

    private int countLines(String filePath) throws Exception
    {
        try (BufferedReader reader = Readers.getReader(new File(filePath)))
        {
            int count = 0;
            while (reader.readLine() != null)
            {
                count++;
            }
            return count;
        }
    }

    private void verifyRowCreated(String schema, String query, String animalId, int rowCount)
    {
        goToSchemaBrowser();
        DataRegionTable table = viewQueryData(schema, query);
        table.setFilter("Id", "Equals", animalId);
        Assert.assertEquals("Record not created in " + schema + "." + query, rowCount, table.getDataRowCount());
    }

    private void verifyOrchardFileGenerated(String animalId)
    {
        String prefix = "orchardFile";
        final String[] largestTimestamp = {"0"};

        try
        {
            // Use Files.walkFileTree to traverse the directory
            Files.walkFileTree(orchardFileLocation.toPath(), new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
                {
                    // Check if the file name starts with "orchardFile"
                    if (file.getFileName().toString().startsWith(prefix))
                    {
                        String fileName = file.getFileName().toString();
                        String timestamp = fileName.substring(prefix.length(), fileName.indexOf(".txt"));
                        if (timestamp.compareTo(largestTimestamp[0]) > 0)
                        {
                            largestTimestamp[0] = timestamp;
                        }

                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (largestTimestamp[0].equals("0"))
        {
            Assert.fail("Orchard file is not created");
        }

        File orchardFile = new File(orchardFileLocation + "/orchardFile" + largestTimestamp[0] + ".txt");
        waitFor(() -> orchardFile.exists(), WAIT_FOR_PAGE);
        Assert.assertTrue("Edited animal is not present in the orchard file",
                TestFileUtils.getFileContents(orchardFile).contains(animalId));
    }

    private void submitForm(String buttonText, String windowTitle)
    {
        //Give time for errors to disappear after validation
        longWait().until(ExpectedConditions.invisibilityOfElementWithText(Locator.tag("div"), "The form has the following errors and warnings:"));
        Locator submitFinalBtn = Locator.linkWithText(buttonText);
        shortWait().until(ExpectedConditions.elementToBeClickable(submitFinalBtn));
        Window<?> msgWindow;
        try
        {
            submitFinalBtn.findElement(getDriver()).click();
            msgWindow = new Window.WindowFinder(this.getDriver()).withTitleContaining(windowTitle).waitFor();
        }
        catch (NoSuchElementException e)
        {
            //retry
            sleep(500);
            submitFinalBtn.findElement(getDriver()).click();
            msgWindow = new Window.WindowFinder(this.getDriver()).withTitleContaining(windowTitle).waitFor();
        }
        msgWindow.clickButton("Yes");
    }

    private void gotoEnterData()
    {
        beginAt(WebTestHelper.buildURL("ehr", getContainerPath(), "enterData.view"));
    }

    private void lockForm()
    {
        Locator.XPathLocator lockBtn = Ext4Helper.Locators.ext4Button("Lock Entry");
        Locator.XPathLocator unlockBtn = Ext4Helper.Locators.ext4Button("Unlock Entry");
        try
        {
            log("Locking the entry");
            waitForElement(lockBtn);
            lockBtn.findElement(getDriver()).click();
            waitForElementToDisappear(lockBtn);
            Assert.assertTrue("Entry did not lock", isElementPresent(unlockBtn));
        }
         catch (NoSuchElementException e)
         {
             log("Form is already unlocked");
         }
    }
}