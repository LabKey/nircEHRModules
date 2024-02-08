package org.labkey.nirc_ehr.dataentry.section;

import org.json.JSONObject;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.SimpleFormPanelSection;
import org.labkey.api.view.template.ClientDependency;

public class NIRCDeathNecropsyWeightFormSection extends SimpleFormPanelSection
{
    public NIRCDeathNecropsyWeightFormSection(boolean isChild)
    {
        super("study", "weight", "Weights");
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
    public JSONObject toJSON(DataEntryFormContext ctx, boolean includeFormElements)
    {
        JSONObject json = super.toJSON(ctx, includeFormElements);
        json.put("collapsible", true);
        json.put("initCollapsed", true);
        json.put("dataDependentCollapseHeader", true);
        return json;
    }
}
