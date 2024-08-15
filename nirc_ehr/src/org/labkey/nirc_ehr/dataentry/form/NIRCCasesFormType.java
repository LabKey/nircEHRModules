package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.AbstractFormSection;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.ehr.dataentry.SimpleFormSection;
import org.labkey.api.ehr.security.EHRClinicalEntryPermission;
import org.labkey.api.ehr.security.EHRVeterinarianPermission;
import org.labkey.api.module.Module;
import org.labkey.api.query.Queryable;
import org.labkey.api.security.permissions.AdminPermission;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCBloodDrawFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCCaseTemplateFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCCasesFormPanelSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCClinicalObservationsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCClinicalRemarksFormPanelSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCHousingFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCProcedureFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTaskFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTreatmentGivenFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTreatmentOrderFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCVitalsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCWeightFormSection;
import org.labkey.nirc_ehr.security.NIRCEHRVetTechPermission;
import org.labkey.nirc_ehr.security.NIRCEHRVetTechRole;

import java.util.Arrays;
import java.util.Set;

public class NIRCCasesFormType extends NIRCBaseTaskFormType
{
    @Queryable
    public static final String NAME = "Clinical Cases";
    public static final String LABEL = "Clinical Cases";

    public NIRCCasesFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, LABEL, "Clinical", Arrays.<FormSection>asList(
                new NIRCTaskFormSection(),
                new NIRCAnimalDetailsFormSection(),
                new NIRCCaseTemplateFormSection("Case Template", "Case Template", "nirc_ehr-casetemplatepanel", Arrays.asList(ClientDependency.supplierFromPath("nirc_ehr/panel/CaseTemplatePanel.js"))),
                new NIRCCasesFormPanelSection("Clinical Case",
                        ctx.getContainer().hasPermission(ctx.getUser(), NIRCEHRVetTechPermission.class),
                        ctx.getContainer().hasPermission(ctx.getUser(), EHRVeterinarianPermission.class),
                        ctx.getContainer().hasPermission(ctx.getUser(), AdminPermission.class)),
                new NIRCClinicalRemarksFormPanelSection(true, "cases", "Clinical Remarks"),
                new NIRCProcedureFormSection(true, "cases"),
                new NIRCClinicalObservationsFormSection(),
                new NIRCTreatmentGivenFormSection(true, "cases"),
                new NIRCTreatmentOrderFormSection(true, "cases"),
                new NIRCWeightFormSection(true, false, true, "cases"),
                new NIRCVitalsFormSection(true, "cases"),
                new NIRCHousingFormSection(true, true, true, "cases"),
                new NIRCBloodDrawFormSection(true, true, true)
        ));

        setTemplateMode(AbstractFormSection.TEMPLATE_MODE.NO_ID);
        setDisplayReviewRequired(true);

        for (FormSection s : this.getFormSections())
        {
            s.addConfigSource("ClinicalDefaults");
            s.addConfigSource("ClinicalCase");
            s.addConfigSource("TreatmentSchedule");

            if (s instanceof SimpleFormSection && !s.getName().equals("tasks"))
                s.setTemplateMode(AbstractFormSection.TEMPLATE_MODE.NO_ID);

            if (s instanceof AbstractFormSection)
            {
                ((AbstractFormSection)s).setAllowBulkAdd(false);
            }
        }
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/TreatmentSchedule.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/field/DrugVolumeField.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/window/DrugAmountWindow.js"));

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/ClinicalDefaults.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/ClinicalCase.js"));
        addClientDependency(ClientDependency.supplierFromPath("ehr/panel/ExamDataEntryPanel.js"));
        setJavascriptClass("EHR.panel.ExamDataEntryPanel");

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/panel/NIRCExamCasesDataEntryPanel.js"));
        setJavascriptClass("NIRC_EHR.panel.ExamCasesDataEntryPanel");
    }

    @Override
    protected boolean canInsert()
    {
        if (!getCtx().getContainer().hasPermission(getCtx().getUser(), EHRClinicalEntryPermission.class))
            return false;

        return super.canInsert();
    }
}