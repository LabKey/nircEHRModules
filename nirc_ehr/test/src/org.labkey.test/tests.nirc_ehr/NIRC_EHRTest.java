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
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.labkey.api.reader.Readers;
import org.labkey.remoteapi.query.InsertRowsCommand;
import org.labkey.remoteapi.security.CreateUserResponse;
import org.labkey.test.Locator;
import org.labkey.test.ModulePropertyValue;
import org.labkey.test.TestFileUtils;
import org.labkey.test.WebTestHelper;
import org.labkey.test.categories.EHR;
import org.labkey.test.components.ui.grids.QueryGrid;
import org.labkey.test.pages.ehr.EHRAdminPage;
import org.labkey.test.pages.ehr.EHRLookupPage;
import org.labkey.test.pages.ehr.NotificationAdminPage;
import org.labkey.test.tests.ehr.AbstractGenericEHRTest;
import org.labkey.test.util.DataRegionTable;
import org.labkey.test.util.LogMethod;
import org.labkey.test.util.PortalHelper;
import org.labkey.test.util.PostgresOnlyTest;

import java.io.BufferedReader;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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
        List<ModulePropertyValue> props = new ArrayList<>();
        props.add(new ModulePropertyValue("EHR", "/" + getProjectName(), "EHRCustomModule", "NIRC_EHR"));
        goToProjectHome();
        setModuleProperties(props);

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
//        NIRC_EHRTest init = (NIRC_EHRTest) getCurrentTest();
//        init.doSetup();
    }

    private void doSetup() throws Exception
    {
        initProject(PROJECT_TYPE);
        goToEHRFolder();
        createTestSubjects();
        addNIRCEhrLinks();
        addExtensibleCols();
        enableSiteNotification();
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
        notificationAdminPage.enableRequestAdminAlerts(notification);
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

    @Override
    @Test
    public void testQuickSearch()
    {
        //TODO: Implement this test once Quick Search is customized for NIRC
    }

    public void addDeathNecropsyUsersAndPermissions()
    {
        //create animal care basic submitter user (this user can 'Submit Death')
        createUser("ac_bs@test.com", "EHR Basic Submitters", null);

        //create a vet user with 'EHR Basic Submitter' role (this user can 'Submit for Review')
        createUser("vet_bs@test.com", "EHR Basic Submitters", "EHR Veterinarian");

        //create a vet user with 'EHR Full Submitter' role (this user can 'Submit Final')
        createUser("vet_fs@test.com", "EHR Full Submitters", "EHR Veterinarian");
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
    public void testDeathNecropsyForm()
    {
//        enableNotification("status_org.labkey.nirc_ehr.notification.NIRCDeathNotification"); //TODO: fix
        addDeathNecropsyUsersAndPermissions();

        //Go to EHR page > Enter Data > Death/Necropsy

        //impersonate as basic submitter user (ac_bs@test.com)

        //verify that only 'Submit Death' button is visible, and 'Submit for Review' and 'Submit Final' buttons are not visible.

        //Enter data in the Death section
        //1. Try entering Id of an already dead animal, verify error message near the submit buttons and 'Submit Death' btn stays disabled
        //2. Try entering Id of animal that's shipped, verify error message near the submit buttons and 'Submit Death' btn stays disabled

        //3. Now enter alive animal (animal 44444 has all the correct data for this test)
        //verify that 'Submit Death' button is enabled, and submit the form
        //you should be redirected to the main Data Entry page

        //go to Animal History > enter animal that's marked as dead > General > Demographics > verify that animal is displayed with Status 'Dead'

        //stop impersonation

        //wait for 1 minute (since the notification is set to run after one min after a Death record is submitted)

        //impersonate as a vet user who is a "EHR Basic Submitter" (vet_bs@test.com)

        //go to Dumbster module

        //verify death notification email was sent
        //click on the necropsy link from the "email" and verify that the necropsy form is displayed with Death data filled in

        //verify that 'Submit for Review' button is visible

        //Fill out necropsy, gross pathology, and tissue disposition sections
        //submit for review, and assign to vet_fs@test.com
        //verify data in study.necropsy, grossPathology, and tissueDisposition datasets

        //stop impersonation
        //impersonate as a vet user who is a "EHR Full Submitter" (vet_fs@test.com)
        //Go to EHR page > Enter Data > My tasks
        //Click on task Title -  this should take user to the Death/Necropsy data entry form
        //'Submit Final' button should be visible
        //click Submit Final

        //verify data in study.weight is inserted for this dead animal (submit final sets the QC State to "Completed", aka makes data public to be then available to view from Demographics as Most Recent Weight)

        //verify that animal's record is closed, meaning 'end date' is set in study.assignments, study.protocolAssignments, and study.housing as the date of death
    }

    @Override
    @Test
    public void testCalculatedAgeColumns()
    {
        String subjectId = "test2008446";

        beginAt(String.format("%s/query-executeQuery.view?schemaName=study&query.queryName=Weight&query.Id~contains=%s", getContainerPath(), subjectId));
        _customizeViewsHelper.openCustomizeViewPanel();
        _customizeViewsHelper.addColumn("ageAtTime/AgeAtTime");
        _customizeViewsHelper.addColumn("ageAtTime/AgeAtTimeYearsRounded");
        _customizeViewsHelper.addColumn("ageAtTime/AgeAtTimeMonths");
        _customizeViewsHelper.applyCustomView();

        DataRegionTable table = new DataRegionTable("query", this);
        int columnCount = table.getColumnCount();
        List<String> row = table.getRowDataAsText(0);
        assertEquals("Calculated ages are incorrect", Arrays.asList("4.0", "4.0", "48.0"), row.subList(columnCount - 3, columnCount));
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
}