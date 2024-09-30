package org.labkey.nirc_ehr.history;

import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;
import org.labkey.api.util.PageFlowUtil;

import java.sql.SQLException;
import java.util.Set;

public class DepartureDataSource extends AbstractDataSource
{
    public DepartureDataSource(Module module)
    {
        super("study", "Departure", "Departure", "Departure", module);
    }

    @Override
    protected Set<String> getColumnNames()
    {
        return PageFlowUtil.set("Id", "date", "destination/meaning");
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        StringBuilder sb = new StringBuilder();

        if (rs.hasColumn(FieldKey.fromString("destination/meaning")) && rs.getObject(FieldKey.fromString("destination/meaning")) != null)
            sb.append("Destination: " + rs.getString(FieldKey.fromString("destination/meaning")));
        else
            sb.append("Destination: Unknown");

        return sb.toString();
    }
}
