package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.ehr.dataentry.forms.BirthFormType;
import org.labkey.api.ehr.dataentry.forms.LockAnimalsFormSection;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCBirthFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCBirthInstructionsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCProjectAssignmentFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCProtocolAssignmentFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTaskFormSection;

import java.util.Arrays;

public class NIRCBirthFormType extends BirthFormType
{
    public NIRCBirthFormType (DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, Arrays.asList(
                new LockAnimalsFormSection(),
                new NIRCBirthInstructionsFormSection(),
                new NIRCTaskFormSection(),
                new NIRCAnimalDetailsFormSection(),
                new NIRCBirthFormSection(),
                new NIRCProtocolAssignmentFormSection(),
                new NIRCProjectAssignmentFormSection()
        ));

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/plugin/RowEditor.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/Birth.js"));

        for (FormSection s : getFormSections())
        {
            s.addConfigSource("Birth");
        }
    }
}
