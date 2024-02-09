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

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.labkey.test.Locator;
import org.labkey.test.TestFileUtils;
import org.labkey.test.WebTestHelper;
import org.labkey.test.categories.EHR;
import org.labkey.test.tests.ehr.AbstractGenericEHRTest;
import org.labkey.test.util.DataRegionTable;
import org.labkey.test.util.Ext4Helper;
import org.labkey.test.util.PortalHelper;
import org.labkey.test.util.PostgresOnlyTest;
import org.labkey.test.util.ext4cmp.Ext4ComboRef;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

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

    @BeforeClass
    public static void setupProject() throws Exception
    {
        NIRC_EHRTest init = (NIRC_EHRTest) getCurrentTest();
        init.doSetup();
    }

    private void doSetup() throws Exception
    {
        initProject(PROJECT_TYPE);
        goToEHRFolder();
        createTestSubjects();
        addNIRCEhrLinks();
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
        assertEquals("Calculated ages are incorrect", Arrays.asList("4.0", "4.0", "48.0"), row.subList(columnCount - 4, columnCount));
    }
}