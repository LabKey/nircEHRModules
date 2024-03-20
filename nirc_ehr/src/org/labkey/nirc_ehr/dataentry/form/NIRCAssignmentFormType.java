package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCProjectAssignmentFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCProtocolAssignmentFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTaskFormSection;

import java.util.List;

public class NIRCAssignmentFormType extends NIRCBaseTaskFormType
{
    public static final String NAME = "Assignment";
    public static final String LABEL = "Assignment";

    public NIRCAssignmentFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, LABEL, "Colony Management", List.of(
                new NIRCTaskFormSection(),
                new NIRCAnimalDetailsFormSection(),
                new NIRCProtocolAssignmentFormSection(true, true, true),
                new NIRCProjectAssignmentFormSection(true, true, true)
        ));

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/plugin/RowEditor.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/Assignment.js"));

        for (FormSection s : getFormSections())
        {
            s.addConfigSource("Assignment");
        }
    }
}
