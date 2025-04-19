package org.labkey.nirc_ehr.dataentry.section;

import org.json.JSONObject;
import org.labkey.api.data.TableInfo;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.forms.NewAnimalFormSection;
import org.labkey.api.query.FieldKey;
import org.labkey.api.view.template.ClientDependency;

import java.util.List;

public class NIRCBirthFormSection extends NewAnimalFormSection
{
    public NIRCBirthFormSection()
    {
        super("study", "birth", "Births", false);
        addClientDependency(ClientDependency.supplierFromPath("ehr/window/FormBulkAddWindow.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/window/FormBulkAddWindow.js"));
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

        keys.add(2, FieldKey.fromString("Id/demographics/species"));
        keys.add(3, FieldKey.fromString("Id/demographics/gender"));
        keys.add(4, FieldKey.fromString("Id/demographics/dam"));
        keys.add(5, FieldKey.fromString("Id/demographics/sire"));

        return keys;
    }

    @Override
    public List<String> getTbarButtons()
    {
        List<String> defaultButtons = super.getTbarButtons();

        int idx = defaultButtons.indexOf("ADDANIMALS");
        if (idx > -1)
        {
            defaultButtons.remove(idx);
            defaultButtons.add(idx, "NIRC_ADDANIMALS");
        }

        idx = defaultButtons.indexOf("COPYFROMSECTION");
        if (idx > -1)
        {
            defaultButtons.remove(idx);
        }
        return defaultButtons;
    }

    @Override
    public List<String> getTbarMoreActionButtons()
    {
        List<String> defaultButtons = super.getTbarMoreActionButtons();
        defaultButtons.remove("GUESSPROJECT");
        defaultButtons.add("NIRC_FORM_BULK_ADD");
        return defaultButtons;
    }
}