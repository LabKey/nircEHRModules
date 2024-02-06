package org.labkey.nirc_ehr.dataentry.section;

import org.json.JSONObject;
import org.labkey.api.data.TableInfo;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.forms.NewAnimalFormSection;
import org.labkey.api.query.FieldKey;

import java.util.List;

public class NIRCBirthFormSection extends NewAnimalFormSection
{
    public NIRCBirthFormSection()
    {
        super("study", "birth", "Births", false);
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

        keys.add(2, FieldKey.fromString("Id/demographics/dam"));
        keys.add(3, FieldKey.fromString("Id/demographics/sire"));

        return keys;
    }

    @Override
    public List<String> getTbarButtons()
    {
        List<String> defaults = super.getTbarButtons();
        defaults.remove("COPYFROMSECTION");
        return defaults;
    }

    @Override
    public List<String> getTbarMoreActionButtons()
    {
        List<String> defaultButtons = super.getTbarMoreActionButtons();
        defaultButtons.remove("GUESSPROJECT");
        return defaultButtons;
    }
}