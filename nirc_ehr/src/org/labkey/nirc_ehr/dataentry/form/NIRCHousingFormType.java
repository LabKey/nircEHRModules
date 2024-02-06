package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.ehr.dataentry.TaskForm;
import org.labkey.api.ehr.security.EHRHousingTransferPermission;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCHousingFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTaskFormSection;

import java.util.Arrays;

public class NIRCHousingFormType extends TaskForm
{
    public static final String NAME = "Housing";

    public NIRCHousingFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, "Housing Transfers", "Colony Management", Arrays.asList(
                new NIRCTaskFormSection(),
                new NIRCAnimalDetailsFormSection(),
                new NIRCHousingFormSection()
        ));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/plugin/RowEditor.js"));
    }

    @Override
    protected boolean canInsert()
    {
        if (!getCtx().getContainer().hasPermission(getCtx().getUser(), EHRHousingTransferPermission.class))
            return false;

        return super.canInsert();
    }
}