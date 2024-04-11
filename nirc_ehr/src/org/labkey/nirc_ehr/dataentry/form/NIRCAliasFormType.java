package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;

import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCAliasFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTaskFormSection;

import java.util.List;

public class NIRCAliasFormType extends NIRCBaseTaskFormType
{
    public static final String NAME = "Alias";
    public static final String LABEL = "Alias";

    public NIRCAliasFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, LABEL, "Colony Management", List.of(
                new NIRCTaskFormSection(),
                new NIRCAnimalDetailsFormSection(),
                new NIRCAliasFormSection(LABEL)
        ));

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/Alias.js"));

        for (FormSection s : getFormSections())
        {
            s.addConfigSource("Alias");
        }
    }
}
