package org.labkey.nirc_ehr.dataentry.section;

import org.json.JSONObject;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.SimpleFormPanelSection;

public class NIRCNecropsyFormSection extends SimpleFormPanelSection
{
    public NIRCNecropsyFormSection()
    {
        super("study", "necropsy", "Necropsy");
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