package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.AbstractFormSection;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.ehr.dataentry.SimpleFormSection;
import org.labkey.api.ehr.security.EHRClinicalEntryPermission;
import org.labkey.api.module.Module;
import org.labkey.api.query.Queryable;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCCaseTemplateFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCCasesFormPanelSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCHousingFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTaskFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTreatmentGivenFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTreatmentOrderFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCWeightFormSection;

import java.util.Arrays;

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
                new NIRCCasesFormPanelSection("Clinical Case"),
                // new NIRCClinicalObservationsFormSection(),//TODO: add
                new NIRCWeightFormSection(true, false),
                new NIRCTreatmentOrderFormSection(),
                new NIRCTreatmentGivenFormSection(),
                new NIRCHousingFormSection(true, true)
                // new NIRCProcedureFormSection(),//TODO: add
                // new NIRCBloodDrawsFormSection(),//TODO: add
        ));

        setTemplateMode(AbstractFormSection.TEMPLATE_MODE.NO_ID);
        setDisplayReviewRequired(true);

        for (FormSection s : this.getFormSections())
        {
            s.addConfigSource("ClinicalDefaults");
            s.addConfigSource("ClinicalCase");

            if (s instanceof SimpleFormSection && !s.getName().equals("tasks"))
                s.setTemplateMode(AbstractFormSection.TEMPLATE_MODE.NO_ID);

            if (s instanceof AbstractFormSection)
            {
                ((AbstractFormSection)s).setAllowBulkAdd(false);
            }
        }

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