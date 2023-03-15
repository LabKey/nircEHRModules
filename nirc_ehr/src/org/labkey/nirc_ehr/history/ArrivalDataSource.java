package org.labkey.nirc_ehr.history;

import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;
import org.labkey.api.util.PageFlowUtil;

import java.sql.SQLException;
import java.util.Set;

public class ArrivalDataSource extends AbstractDataSource
{
    public ArrivalDataSource(Module module)
    {
        super("study", "Arrival", "Arrival", "Arrival", module);
    }

    @Override
    protected Set<String> getColumnNames()
    {
        return PageFlowUtil.set("Id", "date", "sourceFacility");
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        StringBuilder sb = new StringBuilder();

        if (rs.hasColumn(FieldKey.fromString("sourceFacility")) && rs.getObject(FieldKey.fromString("sourceFacility")) != null)
            sb.append("Lab Transfer From: " + rs.getString(FieldKey.fromString("sourceFacility")));

        return sb.toString();
    }
}
