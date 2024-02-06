package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.view.template.ClientDependency;

import java.util.List;

public class NIRCGrossPathologyFormSection extends BaseFormSection
{
    public NIRCGrossPathologyFormSection()
    {
        super("study", "grossPathology", "Gross Pathology", "ehr-gridpanel", true, true);
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/buttons/necropsyGridButtons.js"));
    }

    @Override
    public List<String> getTbarButtons()
    {
        List<String> defaults = super.getTbarButtons();

        defaults.clear();
        defaults.add("ADDGROSSPATHOLOGY");
        defaults.add("SELECTALL");
        defaults.add("DELETERECORD");

        return defaults;
    }

    @Override
    public List<String> getTbarMoreActionButtons()
    {
        List<String> defaultButtons = super.getTbarMoreActionButtons();
        defaultButtons.clear();
        return defaultButtons;
    }
}
