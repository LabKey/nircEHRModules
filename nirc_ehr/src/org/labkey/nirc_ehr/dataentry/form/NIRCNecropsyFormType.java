package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.AnimalDetailsFormSection;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.ehr.dataentry.TaskForm;
import org.labkey.api.ehr.dataentry.TaskFormSection;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCGrossPathologyFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCDeathFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCNecropsyFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTissueDispositionFormSection;

import java.util.Arrays;

public class NIRCNecropsyFormType extends TaskForm
{
    public static final String NAME = "Necropsy";
    public static final String LABEL = "Death/Necropsy";

    public NIRCNecropsyFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, LABEL, "Pathology", Arrays.asList(
                new TaskFormSection(),
                new AnimalDetailsFormSection(),
                new NIRCDeathFormSection(),
                new NIRCGrossPathologyFormSection(),
                new NIRCTissueDispositionFormSection(),
                new NIRCNecropsyFormSection()
        ));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/NIRCNecropsies.js"));

        for (FormSection s : getFormSections())
        {
            s.addConfigSource("NIRCNecropsies");
        }
    }
}
