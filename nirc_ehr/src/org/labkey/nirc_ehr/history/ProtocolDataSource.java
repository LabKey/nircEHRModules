package org.labkey.nirc_ehr.history;

import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;
import org.labkey.api.util.PageFlowUtil;

import java.sql.SQLException;
import java.util.Set;

public class ProtocolDataSource extends AbstractDataSource
{

    public ProtocolDataSource(Module module)
    {
        super("study", "protocolAssignment", "Protocol Transfer", "Protocol Transfer", module);
        setShowTime(true);
    }

    @Override
    protected Set<String> getColumnNames()
    {
        return PageFlowUtil.set("Id", "date", "enddate", "protocol/title");
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        StringBuilder sb = new StringBuilder();

        if (rs.hasColumn(FieldKey.fromString("protocol/title")) && rs.getObject(FieldKey.fromString("protocol/title")) != null)
        {
            addRow(sb, "Protocol", rs.getString(FieldKey.fromString("protocol/title")));
        }

        if (rs.hasColumn(FieldKey.fromString("enddate")) && rs.getObject("enddate") != null)
        {
            addRow(sb, "End Date", rs.getString("enddate"));
        }

        return sb.toString();
    }

    private void addRow(StringBuilder sb, String displayLabel, String value)
    {
        sb.append(displayLabel);
        sb.append(": ");
        sb.append(PageFlowUtil.filter(value));
        sb.append("\n");;
    }
}
