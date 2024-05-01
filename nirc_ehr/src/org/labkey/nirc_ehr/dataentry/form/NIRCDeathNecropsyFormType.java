package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.ehr.security.EHRVeterinarianPermission;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCDeathFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCGrossPathologyFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCNecropsyFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTaskFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTissueDispositionFormSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NIRCDeathNecropsyFormType extends NIRCBaseTaskFormType
{
    public static final String NAME = "Necropsy";
    public static final String LABEL = "Death/Necropsy";

    public NIRCDeathNecropsyFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, LABEL, "Pathology", Arrays.asList(
                new NIRCTaskFormSection(),
                new NIRCAnimalDetailsFormSection(),
                new NIRCDeathFormSection(),
                new NIRCNecropsyFormSection(true),
                new NIRCGrossPathologyFormSection(true),
                new NIRCTissueDispositionFormSection(true)

        ));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/DeathNecropsy.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/buttons/deathNecropsyButtons.js"));

        for (FormSection s : getFormSections())
        {
            s.addConfigSource("DeathNecropsy");
        }
    }

    @Override
    protected List<String> getButtonConfigs()
    {
        List<String> defaultButtons = new ArrayList<String>();
        boolean isVet = getCtx().getContainer().hasPermission(getCtx().getUser(), EHRVeterinarianPermission.class);

        defaultButtons.add("SAVEDRAFT");
        defaultButtons.add("DEATHSUBMIT");

        if (isVet) {
            defaultButtons.add("SUBMITNECROPSYFORREVIEW");
            defaultButtons.add("SUBMIT"); //submit final
        }

        return defaultButtons;
    }
}
