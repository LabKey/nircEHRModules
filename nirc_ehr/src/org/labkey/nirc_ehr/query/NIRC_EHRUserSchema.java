package org.labkey.nirc_ehr.query;

import org.jetbrains.annotations.Nullable;
import org.labkey.api.data.Container;
import org.labkey.api.data.DbSchema;
import org.labkey.api.query.SimpleUserSchema;
import org.labkey.api.security.User;
import org.labkey.api.security.permissions.ReadPermission;

public class NIRC_EHRUserSchema extends SimpleUserSchema
{
    public static String SCHEMA_NAME = "nirc_ehr";

    public NIRC_EHRUserSchema(String name, @Nullable String description, User user, Container container, DbSchema dbschema)
    {
        super(name, description, user, container, dbschema);
    }

    @Override
    public boolean canReadSchema()
    {
        User user = getUser();
        if (user == null)
            return false;

        return getContainer().hasPermission(user, ReadPermission.class);
    }
}
