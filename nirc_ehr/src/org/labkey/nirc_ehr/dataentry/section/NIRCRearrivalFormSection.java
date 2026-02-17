package org.labkey.nirc_ehr.dataentry.section;

import org.json.JSONObject;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;

public class NIRCRearrivalFormSection extends BaseFormSection
{
    public NIRCRearrivalFormSection()
    {
        super("study", "arrival", "Rearrivals", "ehr-gridpanel", true, true, true);
    }

    @Override
    public JSONObject toJSON(DataEntryFormContext ctx, boolean includeFormElements)
    {
        JSONObject json = super.toJSON(ctx, includeFormElements);
        json.put("dataDependentCollapseHeader", true);
        return json;
    }
}
