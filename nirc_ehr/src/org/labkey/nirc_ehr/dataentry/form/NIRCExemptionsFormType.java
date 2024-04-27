package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCExemptionsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTaskFormSection;

import java.util.Arrays;

public class NIRCExemptionsFormType extends NIRCBaseTaskFormType
{
    public static final String NAME = "Exemptions";

    public NIRCExemptionsFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner,  NAME, NAME, "Colony Management", Arrays.asList(
                new NIRCTaskFormSection(),
                new NIRCAnimalDetailsFormSection(),
                new NIRCExemptionsFormSection(NAME)
        ));

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/NIRCDefault.js"));
    }
}