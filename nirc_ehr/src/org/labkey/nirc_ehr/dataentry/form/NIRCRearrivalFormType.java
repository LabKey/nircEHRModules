package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.ehr.dataentry.forms.LockAnimalsFormSection;
import org.labkey.api.module.Module;
import org.labkey.api.security.permissions.AdminPermission;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCArrivalInstructionsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCRearrivalFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTaskFormSection;

import java.util.Arrays;

public class NIRCRearrivalFormType extends NIRCBaseTaskFormType
{
    public static final String NAME = "Rearrival";

    public NIRCRearrivalFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, "Rearrivals", "Colony Management", Arrays.asList(
                new LockAnimalsFormSection(),
                new NIRCArrivalInstructionsFormSection(),
                new NIRCTaskFormSection(),
                new NIRCAnimalDetailsFormSection(),
                new NIRCRearrivalFormSection()
        ));

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/Rearrival.js"));

        for (FormSection s : getFormSections())
        {
            s.addConfigSource("Rearrival");
        }
    }

    @Override
    public boolean isAvailable()
    {
        return super.isAvailable() && getCtx().getContainer().hasPermission(getCtx().getUser(), AdminPermission.class);
    }
}
