package org.labkey.nirc_ehr.dataentry.section;

import org.json.JSONObject;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.ParentFormPanelSection;
import org.labkey.api.view.template.ClientDependency;

public class NIRCClinicalRemarksFormPanelSection extends ParentFormPanelSection
{
    public NIRCClinicalRemarksFormPanelSection(String label)
    {
        super("study", "clinremarks", label);
        setSupportFormSort(false);
    }

    public NIRCClinicalRemarksFormPanelSection(boolean isChild, String parentQueryName, String label)
    {
        this(label);
        if (isChild)
        {
            addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/ParentChild.js"));
            addConfigSource("ParentChild");

            addClientDependency(ClientDependency.supplierFromPath("ehr/data/ChildClientStore.js"));
            setClientStoreClass("EHR.data.ChildClientStore");
            addExtraProperty("parentQueryName", parentQueryName);
        }

    }

    @Override
    public JSONObject toJSON(DataEntryFormContext ctx, boolean includeFormElements)
    {
        JSONObject json = super.toJSON(ctx, includeFormElements);
        json.put("collapsible", true);
        json.put("initCollapsed", false);
        json.put("dataDependentCollapseHeader", true);
        return json;
    }
}
