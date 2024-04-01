package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.forms.AdminLinksFormType;
import org.labkey.api.module.Module;
import org.labkey.api.view.ActionURL;

import java.util.ArrayList;

public class NIRCProtocolFormType extends AdminLinksFormType
{
    public NIRCProtocolFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, "Protocols", "Protocols", "Admin", new ArrayList<>());
    }

    @Override
    protected ActionURL dataEntryLink()
    {
        ActionURL url = new ActionURL("ehr", "updateQuery", getCtx().getContainer());
        url.addParameter("schemaName", "ehr");
        url.addParameter("query.queryName", "protocol");
        url.addParameter("showImport", "true");
        return url;
    }
}
