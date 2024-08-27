package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.forms.AdminLinksFormType;
import org.labkey.api.module.Module;
import org.labkey.api.security.permissions.AdminPermission;
import org.labkey.api.view.ActionURL;

import java.util.ArrayList;

public class NIRCFloorFormType extends AdminLinksFormType
{
    public NIRCFloorFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, "Floor", "Floor", "Locations", new ArrayList<>());
    }

    @Override
    protected ActionURL dataEntryLink()
    {
        ActionURL url = new ActionURL("ldk", "updateQuery", getCtx().getContainer());
        url.addParameter("schemaName", "ehr_lookups");
        url.addParameter("query.queryName", "floors");
        url.addParameter("showImport", "true");
        return url;
    }

    @Override
    public boolean isAvailable()
    {
        return super.isAvailable() && getCtx().getContainer().hasPermission(getCtx().getUser(), AdminPermission.class);
    }
}
