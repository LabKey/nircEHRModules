package org.labkey.nirc_ehr;

import org.labkey.api.data.DbSchema;
import org.labkey.api.data.DbSchemaType;
import org.labkey.api.data.dialect.SqlDialect;

public class NIRC_EHRSchema
{
    private static final NIRC_EHRSchema _instance = new NIRC_EHRSchema();
    public static final String NAME = "nirc_ehr";

    public static NIRC_EHRSchema get_instance()
    {
        return _instance;
    }

    private NIRC_EHRSchema()
    {
        // singleton accessed via org.labkey.nircc_ehr.NIRC_EHRSchema.getInstance()
    }

    public DbSchema getSchema()
    {
        return DbSchema.get(NAME, DbSchemaType.Module);
    }

    public SqlDialect getSqlDialect()
    {
        return getSchema().getSqlDialect();
    }
}
