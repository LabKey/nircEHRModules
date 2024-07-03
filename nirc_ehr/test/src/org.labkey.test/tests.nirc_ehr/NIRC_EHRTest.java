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
import org.labkey.test.util.ext4cmp.Ext4GridRef;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
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
    DateTimeFormatter _dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static String  NIRC_BASIC_SUBMITTER = "ac_bs@nirctest.com";
    private static String NIRC_BASIC_SUBMITTER_VET = "vet_bs@nirctest.com";
    private  static String NIRC_FULL_SUBMITTER_VET = "vet_fs@nirctest.com";

    private static String deadAnimalId = "D5454";
    private static String departedAnimalId = "H6767";
    private static String aliveAnimalId = "A4545";
    private static String orchardFileLocation;

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
        CreateUserResponse inves2 =_userHelper.createUser(DUMMY_INVES + emailDomain, true);
        goToEHRFolder();
        _permissionsHelper.addUserToProjGroup(inves1.getEmail(), getProjectName(), INVESTIGATOR.getGroup());
        _permissionsHelper.addUserToProjGroup(inves2.getEmail(), getProjectName(), INVESTIGATOR.getGroup());

        InsertRowsCommand insertCmd = new InsertRowsCommand("ehr", "protocol");

        Map<String,Object> rowMap = new HashMap<>();
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
        List<ModuleProperty> props = List.of(
                new ModuleProperty("EHR", "/" + getProjectName(), "EHRCustomModule", "NIRC_EHR"),
                new ModuleProperty("NIRC_EHR", "/", "NIRCOrchardFileLocation", orchardFileLocation)
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

    @BeforeClass
    public static void setupProject() throws Exception
    {
        NIRC_EHRTest init = (NIRC_EHRTest) getCurrentTest();
        init.doSetup();
    }

    private void doSetup() throws Exception
    {
        setOrchardFileLocation();
        initProject(PROJECT_TYPE);
        goToEHRFolder();
        createTestSubjects();
        addNIRCEhrLinks();
        addExtensibleCols();
        enableSiteNotification();
        populateLocations();
    }

    private void setOrchardFileLocation()
    {
        try
        {
            orchardFileLocation = TestFileUtils.ensureTestTempDir().getAbsolutePath();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }
    private void enableSiteNotification()
    {
        log("Enabling the notification at the site level");
        goToAdminConsole().clickNotificationServiceAdmin();
        _ext4Helper.selectComboBoxItem("Status of Notification Service:","Enabled");
        clickButton("Save",0);
        _helper.clickExt4WindowBtn("Success","OK");
    }

    private void enableNotification(String notification)
    {
        goToEHRFolder();
        _containerHelper.enableModule("Dumbster");
        log("Setup the notification service for this container");
        EHRAdminPage.beginAt(this,getContainerPath());
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
        roomCmd.addRow(Map.of("room", "R1"));
        roomCmd.addRow(Map.of("room", "R2"));
        roomCmd.addRow(Map.of("room", "R3"));
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
        EHRAdminPage.beginAt(this,getContainerPath());
        click(Locator.linkWithText("EHR EXTENSIBLE COLUMNS"));
        click(Locator.linkWithText("Load EHR table definitions"));
        waitForElement(Locator.tagWithClass("span", "x4-window-header-text").withText("Success"));
        assertExt4MsgBox("EHR tables updated successfully.", "OK");
    }

    private void addNIRCEhrLinks()
    {
        navigateToFolder(getProjectName(), getFolderName());
        (new PortalHelper(this)).addWebPart("NIRC EHR Links");
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
        arrivals.setGridCell(1, "Id", arrivedAnimal);
        arrivals.setGridCell(1, "cage", "C1");
        arrivals.setGridCell(1, "project", "640991");
        arrivals.setGridCell(1, "arrivalProtocol", "dummyprotocol");
        arrivals.setGridCellJS(1, "Id/demographics/birth", now.minusDays(7).format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_STRING)));
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

    public void addDeathNecropsyUsersAndPermissions()
    {
        //create animal care basic submitter user (this user can 'Submit Death')
        createUser(NIRC_BASIC_SUBMITTER, "EHR Basic Submitters", null);

        //create a vet user with 'EHR Basic Submitter' role (this user can 'Submit for Review')
        createUser(NIRC_BASIC_SUBMITTER_VET, "EHR Basic Submitters", "EHR Veterinarian");

        //create a vet user with 'EHR Full Submitter' role (this user can 'Submit Final')
        createUser(NIRC_FULL_SUBMITTER_VET, "EHR Administrators", "EHR Veterinarian"); //'EHR Full Submitter' role doesn't allow updating Demographics dataset, so setting user as a 'EHR Administrator'
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

    private void createUser(String userEmail, String groupName, @Nullable String permission)
    {
        _userHelper.createUser(userEmail, false);
        goToEHRFolder();
        if (permission != null)
            _permissionsHelper.setUserPermissions(userEmail, permission);
        _permissionsHelper.addUserToProjGroup(userEmail, getProjectName(), groupName);
    }

    @Test
    public void testDeathNecropsyForm() throws IOException, CommandException
    {
        addDeathNecropsyUsersAndPermissions();
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
        setFormElement(Locator.name("reason"), "Euthaniasia (project)");

        Assert.assertFalse(isElementPresent(Locator.linkWithText("Submit for Review")));
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
        impersonate(NIRC_BASIC_SUBMITTER_VET);
        beginAt(url);
        waitForElement(Locator.name("necropsyWeight"));
        setFormElement(Locator.name("necropsyWeight"), "23");

        log("Entering Tissue Disposition");
        Ext4GridRef tissueDisposition = _helper.getExt4GridForFormSection("Tissue Disposition");
        _helper.addRecordToGrid(tissueDisposition);
        tissueDisposition.setGridCell(1, "necropsyDispositionCode", "Frozen");
        tissueDisposition.setGridCell(1, "necropsyTissue", "Pancreas");
        waitAndClick(_helper.getDataEntryButton("Submit for Review"));

        log("Assigning the reviewer");
        Window<?> submitForReview = new Window<>("Submit For Review", getDriver());
        setFormElement(Locator.tagWithNameContaining("input", "ehr-usersandgroups"), NIRC_FULL_SUBMITTER_VET);
        submitForReview.clickButton("Submit");
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
        File orchardFile =  new File(orchardFileLocation + "/orchardFile.txt");
        waitFor(() -> orchardFile.exists(), WAIT_FOR_PAGE);
        Assert.assertTrue("Edited animal is not present in the orchard file",
               TestFileUtils.getFileContents(orchardFile).startsWith(animalId));
    }

    private void submitForm(String buttonText, String windowTitle)
    {
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
        if (Ext4Helper.Locators.ext4Button("Lock Entry").isDisplayed(getDriver()))
        {
            Ext4Helper.Locators.ext4Button("Lock Entry").findElement(getDriver()).click();
            waitForElement(Ext4Helper.Locators.ext4Button("Unlock Entry"));
        }
    }
}