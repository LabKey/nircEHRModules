package org.labkey.nirc_ehr.dataentry.section;

import org.json.JSONObject;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.NonStoreFormSection;
import org.labkey.api.view.template.ClientDependency;

import java.util.List;
import java.util.function.Supplier;

public class NIRCCaseTemplateFormSection extends NonStoreFormSection
{
    public NIRCCaseTemplateFormSection(String name, String label, String xtype, List<Supplier<ClientDependency>> dependencies)
    {
        super(name, label, xtype, dependencies);
    }

    @Override
    public JSONObject toJSON(DataEntryFormContext ctx, boolean includeFormElements)
    {
        JSONObject json = super.toJSON(ctx, includeFormElements);
        json.put("collapsible", true);
        json.put("initCollapsed", true);
        json.put("dataDependentCollapseHeader", false);
        return json;
    }
}
