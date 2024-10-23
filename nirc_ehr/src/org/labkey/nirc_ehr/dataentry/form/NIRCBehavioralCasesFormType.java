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
import org.labkey.nirc_ehr.dataentry.section.NIRCClinicalObservationsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCClinicalRemarksFormPanelSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCObservationOrdersFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTaskFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTreatmentGivenFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTreatmentOrderFormSection;

import java.util.Arrays;
import java.util.List;

public class NIRCBehavioralCasesFormType extends NIRCBaseTaskFormType
{
    @Queryable
    public static final String NAME = "Behavioral Cases";
    public static final String LABEL = "Behavioral Cases";

    public NIRCBehavioralCasesFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, LABEL, "Behavior", Arrays.<FormSection>asList(
                new NIRCTaskFormSection(),
                new NIRCAnimalDetailsFormSection(),
                new NIRCCaseTemplateFormSection("Case Template", "Case Template", "nirc_ehr-casetemplatepanel", Arrays.asList(ClientDependency.supplierFromPath("nirc_ehr/panel/CaseTemplatePanel.js"))),
                new NIRCCasesFormPanelSection("Behavior Case", ctx, true),
                new NIRCClinicalRemarksFormPanelSection(true, "cases", "Behavior Assessment", ctx, true),
                new NIRCClinicalObservationsFormSection(true, "cases"),
                new NIRCObservationOrdersFormSection(null, true, "cases"),
                new NIRCTreatmentGivenFormSection(true, "cases"),
                new NIRCTreatmentOrderFormSection(true, "cases")
        ));

        setTemplateMode(AbstractFormSection.TEMPLATE_MODE.NO_ID);
        setDisplayReviewRequired(true);

        for (FormSection s : this.getFormSections())
        {
            s.addConfigSource("BehavioralCase");
            s.addConfigSource("BehaviorDefaults");
            s.addConfigSource("TreatmentSchedule");

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

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/BehavioralCase.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/BehaviorDefaults.js"));
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
}
