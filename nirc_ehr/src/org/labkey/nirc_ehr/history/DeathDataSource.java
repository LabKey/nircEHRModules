package org.labkey.nirc_ehr.history;

import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;
import org.labkey.api.util.PageFlowUtil;

import java.sql.SQLException;
import java.util.Set;

public class DeathDataSource extends AbstractDataSource
{
    public DeathDataSource(Module module)
    {
        super("study", "Deaths", "Death", "Deaths", module);
    }

    @Override
    protected Set<String> getColumnNames()
    {
        return PageFlowUtil.set("Id", "date", "reason/title");
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        StringBuilder sb = new StringBuilder();

        if(rs.hasColumn(FieldKey.fromString("reason/title")) && rs.getObject(FieldKey.fromString("reason/title")) != null)
            sb.append("Disposition: " + rs.getString(FieldKey.fromString("reason/title")));
        else
            sb.append("Disposition: Unknown");

        return sb.toString();
    }
}
