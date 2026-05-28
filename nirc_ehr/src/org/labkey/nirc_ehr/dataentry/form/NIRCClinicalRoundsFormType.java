/*
 * Copyright (c) 2024-2026 LabKey Corporation
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
package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.AbstractFormSection;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.ehr.dataentry.SimpleFormSection;
import org.labkey.api.ehr.security.EHRClinicalEntryPermission;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCBloodDrawFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCCaseTemplateFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCCasesFormPanelSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCClinicalObservationsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCHousingFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCProcedureFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTaskFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTreatmentGivenFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCVitalsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCWeightFormSection;

import java.util.Arrays;
import java.util.List;

public class NIRCClinicalRoundsFormType extends NIRCBaseTaskFormType
{
    public static final String NAME = "Clinical Rounds";
    public static final String LABEL = "Clinical Rounds";

    public NIRCClinicalRoundsFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, LABEL, "Clinical", Arrays.<FormSection>asList(
                new NIRCTaskFormSection(),
                new NIRCAnimalDetailsFormSection(),
                new NIRCCaseTemplateFormSection("Case Template", "Case Template", "nirc_ehr-casetemplatepanel", Arrays.asList(ClientDependency.supplierFromPath("nirc_ehr/panel/CaseTemplatePanel.js"))),
                new NIRCCasesFormPanelSection("Clinical Case", ctx, false),
                new NIRCWeightFormSection(true, false, true, "cases"),
                new NIRCClinicalObservationsFormSection(true, "cases"),
                new NIRCProcedureFormSection(true, "cases"),
                new NIRCTreatmentGivenFormSection(true, "cases"),
                new NIRCVitalsFormSection(true, "cases"),
                new NIRCBloodDrawFormSection(true, "cases"),
                new NIRCHousingFormSection(true, true, true, "cases")
        ));

        setTemplateMode(AbstractFormSection.TEMPLATE_MODE.NO_ID);
        setDisplayReviewRequired(true);

        for (FormSection s : this.getFormSections())
        {
            s.addConfigSource("ClinicalDefaults");
            s.addConfigSource("ClinicalCase");
            s.addConfigSource("ClinicalRounds");
            s.addConfigSource("TreatmentSchedule");
            s.addConfigSource("MedicationEndDate");

            if (s instanceof SimpleFormSection && !s.getName().equals("tasks"))
                s.setTemplateMode(AbstractFormSection.TEMPLATE_MODE.NO_ID);

            if (s instanceof AbstractFormSection)
            {
                ((AbstractFormSection)s).setAllowBulkAdd(false);
            }
        }
        setStoreCollectionClass("NIRC_EHR.data.CaseStoreCollection");
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/data/CaseStoreCollection.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/TreatmentSchedule.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/field/DrugVolumeField.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/window/DrugAmountWindow.js"));

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/ClinicalDefaults.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/ClinicalCase.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/ClinicalRounds.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/MedicationEndDate.js"));
        addClientDependency(ClientDependency.supplierFromPath("ehr/panel/ExamDataEntryPanel.js"));
        setJavascriptClass("EHR.panel.ExamDataEntryPanel");

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/panel/NIRCExamCasesDataEntryPanel.js"));
        setJavascriptClass("NIRC_EHR.panel.ExamCasesDataEntryPanel");

        // Needed for case and scheduled date/time when navigating from treatment or observation schedule
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/buttons/treatmentSubmit.js"));
    }

    @Override
    protected boolean canInsert()
    {
        if (!getCtx().getContainer().hasPermission(getCtx().getUser(), EHRClinicalEntryPermission.class))
            return false;

        return super.canInsert();
    }

    @Override
    protected List<String> getButtonConfigs()
    {
        List<String> ret = super.getButtonConfigs();

        ret.remove("SUBMIT");
        ret.add("NIRC_TREATMENT_SUBMIT");

        return ret;
    }

    @Override
    protected List<String> getMoreActionButtonConfigs()
    {
        List<String> configs = super.getMoreActionButtonConfigs();
        configs.remove("DISCARD");
        return configs;
    }
}
