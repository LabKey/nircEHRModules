package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.forms.AdminLinksFormType;
import org.labkey.api.module.Module;
import org.labkey.api.view.ActionURL;

import java.util.ArrayList;

public class NIRCProjectFormType extends AdminLinksFormType
{
    public NIRCProjectFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, "Projects", "Projects", "Admin", new ArrayList<>());
    }

    @Override
    protected ActionURL dataEntryLink()
    {
        ActionURL url = new ActionURL("ldk", "updateQuery", getCtx().getContainer());
        url.addParameter("schemaName", "ehr");
        url.addParameter("query.queryName", "project");
        url.addParameter("showImport", "true");
        return url;
    }
}
