package org.labkey.nirc_ehr.dataentry.section;

import org.json.JSONObject;
import org.labkey.api.data.TableInfo;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.SimpleFormPanelSection;
import org.labkey.api.query.FieldKey;
import org.labkey.api.view.template.ClientDependency;

import java.util.List;

public class NIRCNecropsyFormSection extends SimpleFormPanelSection
{

    public NIRCNecropsyFormSection(boolean isChild)
    {
        super("study", "necropsy", "Necropsy");
        setSupportFormSort(false);
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
        json.put("initCollapsedWithData", true);
        json.put("dataDependentCollapseHeader", true);
        return json;
    }
}