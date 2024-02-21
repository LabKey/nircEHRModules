package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.view.template.ClientDependency;

import java.util.List;

public class NIRCTissueDispositionFormSection extends BaseFormSection
{
    public NIRCTissueDispositionFormSection(boolean isChild)
    {
        super("study", "tissueDisposition", "Tissue Disposition", "ehr-gridpanel", true, true);
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
        defaults.remove("ADDANIMALS");
        defaults.remove("TEMPLATE");
        return defaults;
    }

    @Override
    public List<String> getTbarMoreActionButtons()
    {
        List<String> defaultButtons = super.getTbarMoreActionButtons();
        defaultButtons.remove("COPY_IDS");
        return defaultButtons;
    }
}
