package org.labkey.nirc_ehr.dataentry.section;

import org.json.JSONObject;
import org.labkey.api.data.TableInfo;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.forms.NewAnimalFormSection;
import org.labkey.api.query.FieldKey;

import java.util.List;

public class NIRCArrivalFormSection extends NewAnimalFormSection
{
    public NIRCArrivalFormSection()
    {
        super("study", "arrival", "Arrivals", false);
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

    @Override
    protected List<FieldKey> getFieldKeys(TableInfo ti)
    {
        List<FieldKey> keys = super.getFieldKeys(ti);

        keys.add(6, FieldKey.fromString("Id/demographics/dam"));
        keys.add(7, FieldKey.fromString("Id/demographics/sire"));
        keys.add(8, FieldKey.fromString("Id/demographics/species"));
        keys.add(9, FieldKey.fromString("Id/demographics/birth"));
        keys.add(10, FieldKey.fromString("Id/demographics/gender"));

        return keys;
    }
}
