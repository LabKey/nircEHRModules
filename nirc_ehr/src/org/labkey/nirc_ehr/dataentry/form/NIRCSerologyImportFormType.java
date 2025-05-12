package org.labkey.nirc_ehr.dataentry.form;

import org.json.JSONObject;
import org.labkey.api.ehr.dataentry.AbstractDataEntryForm;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.module.Module;
import org.labkey.api.view.ActionURL;

import java.util.ArrayList;

public class NIRCSerologyImportFormType extends AbstractDataEntryForm
{
    private final DataEntryFormContext _formContext;

    public NIRCSerologyImportFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, "SerologyImport", "Serology Import", "Lab Results", new ArrayList<>());
        _formContext = ctx;
    }

    @Override
    public JSONObject toJSON(boolean includeFormElements)
    {
        JSONObject json = super.toJSON(includeFormElements);

        ActionURL url = new ActionURL("study", "import", _formContext.getContainer());
        url.addParameter("datasetId", "1032");
        json.put("url", url);

        return json;
    }
}
