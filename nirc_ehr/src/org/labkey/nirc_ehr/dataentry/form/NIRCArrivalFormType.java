package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.ehr.dataentry.forms.LockAnimalsFormSection;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCArrivalFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCArrivalInstructionsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCProjectAssignmentFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCProtocolAssignmentFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTaskFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCWeightFormSection;

import java.util.Arrays;

public class NIRCArrivalFormType extends NIRCBaseTaskFormType
{
    public static final String NAME = "arrival";

    public NIRCArrivalFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, "Arrivals", "Colony Management", Arrays.asList(
                new LockAnimalsFormSection(),
                new NIRCArrivalInstructionsFormSection(),
                new NIRCTaskFormSection(),
                new NIRCAnimalDetailsFormSection(),
                new NIRCArrivalFormSection(),
                new NIRCWeightFormSection(true, true)
                ));

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/Arrival.js"));

        for (FormSection s : getFormSections())
        {
            s.addConfigSource("Arrival");
        }

    }
}
