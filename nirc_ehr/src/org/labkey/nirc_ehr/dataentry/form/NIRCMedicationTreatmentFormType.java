package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTaskFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTreatmentGivenFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTreatmentOrderFormSection;

import java.util.List;

public class NIRCMedicationTreatmentFormType extends NIRCBaseTaskFormType
{
    public static final String NAME = "medicationTreatment";
    public static final String LABEL = "Medications/Treatments";

    public NIRCMedicationTreatmentFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, LABEL, "Clinical", List.of(
                new NIRCTaskFormSection(),
                new NIRCAnimalDetailsFormSection(),
                new NIRCTreatmentGivenFormSection(),
                new NIRCTreatmentOrderFormSection()
        ));

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/TreatmentSchedule.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/field/DrugVolumeField.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/window/DrugAmountWindow.js"));

        // Needed for case and scheduled date/time when navigating from treatment or observation schedule
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/buttons/treatmentSubmit.js"));

        for (FormSection s : getFormSections())
        {
            s.addConfigSource("TreatmentSchedule");
        }
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
