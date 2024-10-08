package org.labkey.nirc_ehr.history;

import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;
import org.labkey.api.util.PageFlowUtil;

import java.sql.SQLException;
import java.util.Set;

public class FlagsDataSource extends AbstractDataSource
{

    public FlagsDataSource(Module module)
    {
        super("study", "flags", "Flags", "Flags", module);
    }

    @Override
    protected Set<String> getColumnNames()
    {
        return PageFlowUtil.set("Id", "date", "flag/category", "remark");
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        StringBuilder sb = new StringBuilder();

        if (rs.hasColumn(FieldKey.fromString("flag/category")) && rs.getObject(FieldKey.fromString("flag/category")) != null)
        {
            addRow(sb, "Type", rs.getString(FieldKey.fromString("flag/category")));
        }

        if (rs.hasColumn(FieldKey.fromString("remark")) && rs.getObject(FieldKey.fromString("remark")) != null)
        {
            addRow(sb, "Remark", rs.getString(FieldKey.fromString("remark")));
        }
        return sb.toString();
    }

    private void addRow(StringBuilder sb, String displayLabel, String value)
    {
        sb.append(displayLabel);
        sb.append(": ");
        sb.append(PageFlowUtil.filter(value));
        sb.append("\n");
    }
}
