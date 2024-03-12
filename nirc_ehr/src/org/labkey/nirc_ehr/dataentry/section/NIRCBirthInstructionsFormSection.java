package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.ehr.dataentry.AbstractFormSection;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormElement;
import org.labkey.api.view.template.ClientDependency;

import java.util.Collections;
import java.util.List;

public class NIRCBirthInstructionsFormSection extends AbstractFormSection
{
    public NIRCBirthInstructionsFormSection()
    {
        super("BirthInstructions", "Instructions", "nirc_ehr-birthinstructionspanel");

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/panel/InstructionsPanel.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/panel/BirthInstructionsPanel.js"));
    }

    @Override
    protected List<FormElement> getFormElements(DataEntryFormContext ctx)
    {
        return Collections.emptyList();
    }
}