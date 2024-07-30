package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.forms.AdminLinksFormType;
import org.labkey.api.module.Module;
import org.labkey.api.view.ActionURL;

import java.util.ArrayList;

public class NIRCRoomFormType extends AdminLinksFormType
{
    public NIRCRoomFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, "Room", "Room", "Admin", new ArrayList<>());
    }

    @Override
    protected ActionURL dataEntryLink()
    {
        ActionURL url = new ActionURL("ldk", "updateQuery", getCtx().getContainer());
        url.addParameter("schemaName", "ehr_lookups");
        url.addParameter("query.queryName", "rooms");
        url.addParameter("showImport", "true");
        return url;
    }
}
