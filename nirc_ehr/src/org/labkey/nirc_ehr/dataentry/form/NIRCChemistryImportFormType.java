package org.labkey.nirc_ehr.dataentry.form;

import org.json.JSONObject;
import org.labkey.api.ehr.dataentry.AbstractDataEntryForm;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.security.EHRDataAdminPermission;
import org.labkey.api.module.Module;
import org.labkey.api.view.ActionURL;

import java.util.ArrayList;

public class NIRCChemistryImportFormType extends AbstractDataEntryForm
{
    private final DataEntryFormContext _formContext;

    public NIRCChemistryImportFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, "ChemistryImport", "Chemistry Import", "Lab Results", new ArrayList<>());
        _formContext = ctx;
    }

    @Override
    public JSONObject toJSON(boolean includeFormElements)
    {
        JSONObject json = super.toJSON(includeFormElements);

        ActionURL url = new ActionURL("study", "import", _formContext.getContainer());
        url.addParameter("datasetId", "1024");
        json.put("url", url);

        return json;
    }

    @Override
    protected boolean canInsert()
    {
        if (!getCtx().getContainer().hasPermission(getCtx().getUser(), EHRDataAdminPermission.class))
            return false;

        return super.canInsert();
    }
}
