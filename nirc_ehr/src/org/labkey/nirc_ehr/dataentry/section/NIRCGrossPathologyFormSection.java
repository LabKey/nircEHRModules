package org.labkey.nirc_ehr.dataentry.section;

import org.json.JSONObject;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.view.template.ClientDependency;

import java.util.List;

public class NIRCGrossPathologyFormSection extends BaseFormSection
{
    public NIRCGrossPathologyFormSection(boolean isChild)
    {
        super("study", "grossPathology", "Gross Pathology", "ehr-gridpanel", true, true);
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/buttons/necropsyGridButtons.js"));
        if (isChild)
        {
            addExtraProperty("parentQueryName", "deaths");

            addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/ParentChild.js"));
            addConfigSource("ParentChild");

            addClientDependency(ClientDependency.supplierFromPath("ehr/data/ChildClientStore.js"));
            setClientStoreClass("EHR.data.ChildClientStore");
        }
    }

    @Override
    public List<String> getTbarButtons()
    {
        List<String> defaults = super.getTbarButtons();
        defaults.clear();
        defaults.add("ADDGROSSPATHOLOGY");
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