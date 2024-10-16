package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCPregnancyFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTaskFormSection;

import java.util.List;

public class NIRCPregnancyFormType extends NIRCBaseTaskFormType
{
    public static final String NAME = "Pregnancy";
    public static final String LABEL = "Pregnancy Outcomes";

    public NIRCPregnancyFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, LABEL, "Colony Management", List.of(
                new NIRCTaskFormSection(),
                new NIRCAnimalDetailsFormSection(),
                new NIRCPregnancyFormSection(LABEL)
        ));

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/Pregnancy.js"));

        for (FormSection s : getFormSections())
        {
            s.addConfigSource("Pregnancy");
        }
    }
}
