package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCBloodDrawFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCClinicalObservationsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCClinicalRemarksFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCProcedureFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTaskFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTreatmentGivenFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTreatmentOrderFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCVitalsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCWeightFormSection;

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
                new NIRCClinicalRemarksFormSection(),
                new NIRCProcedureFormSection(),
                new NIRCClinicalObservationsFormSection(),
                new NIRCTreatmentGivenFormSection(),
                new NIRCTreatmentOrderFormSection(),
                new NIRCWeightFormSection(true, true),
                new NIRCVitalsFormSection(),
                new NIRCBloodDrawFormSection(true, true, true)
        ));

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/BulkClinical.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/TreatmentSchedule.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/field/DrugVolumeField.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/window/DrugAmountWindow.js"));

        for (FormSection s : getFormSections())
        {
            s.addConfigSource("BulkClinical");
            s.addConfigSource("TreatmentSchedule");
        };
    }
}