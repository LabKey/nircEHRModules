package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.ehr.dataentry.AbstractFormSection;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormElement;
import org.labkey.api.view.template.ClientDependency;

import java.util.Collections;
import java.util.List;

public class NIRCArrivalInstructionsFormSection extends AbstractFormSection
{
    public NIRCArrivalInstructionsFormSection()
    {
        super("ArrivalInstructions", "Instructions", "nirc_ehr-arrivalinstructionspanel");

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/panel/InstructionsPanel.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/panel/ArrivalInstructionsPanel.js"));
    }

    @Override
    protected List<FormElement> getFormElements(DataEntryFormContext ctx)
    {
        return Collections.emptyList();
    }
}