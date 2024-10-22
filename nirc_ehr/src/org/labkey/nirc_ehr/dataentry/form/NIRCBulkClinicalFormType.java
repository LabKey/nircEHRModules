package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.ehr.security.EHRVeterinarianPermission;
import org.labkey.api.module.Module;
import org.labkey.api.security.permissions.AdminPermission;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCBloodDrawFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCClinicalObservationsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCClinicalRemarksFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCObservationOrdersFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCProcedureFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTaskFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTreatmentGivenFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTreatmentOrderFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCVitalsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCWeightFormSection;
import org.labkey.nirc_ehr.security.NIRCEHRVetTechPermission;

import java.util.List;

public class NIRCBulkClinicalFormType extends NIRCBaseTaskFormType
{
    public static final String NAME = "Bulk Clinical Entry";
    public static final String LABEL = "Bulk Clinical Entry";

    public NIRCBulkClinicalFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, LABEL, "Clinical", List.of(
                new NIRCTaskFormSection(),
                new NIRCAnimalDetailsFormSection(),
                new NIRCClinicalRemarksFormSection(ctx.getContainer().hasPermission(ctx.getUser(), NIRCEHRVetTechPermission.class),
                        ctx.getContainer().hasPermission(ctx.getUser(), EHRVeterinarianPermission.class),
                        ctx.getContainer().hasPermission(ctx.getUser(), AdminPermission.class)),
                new NIRCProcedureFormSection(),
                new NIRCClinicalObservationsFormSection(false, null),
                new NIRCObservationOrdersFormSection("NIRC_DAILY_CLINICAL_OBS_ORDERS", false, null),
                new NIRCTreatmentGivenFormSection(),
                new NIRCTreatmentOrderFormSection(),
                new NIRCWeightFormSection(true, true),
                new NIRCVitalsFormSection(),
                new NIRCBloodDrawFormSection(true, true, true)
        ));

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/ClinicalDefaults.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/BulkClinical.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/TreatmentSchedule.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/field/DrugVolumeField.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/window/DrugAmountWindow.js"));

        // Needed for case and scheduled date/time when navigating from treatment or observation schedule
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/buttons/treatmentSubmit.js"));

        for (FormSection s : getFormSections())
        {
            s.addConfigSource("ClinicalDefaults");
            s.addConfigSource("BulkClinical");
            s.addConfigSource("TreatmentSchedule");
        };
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
