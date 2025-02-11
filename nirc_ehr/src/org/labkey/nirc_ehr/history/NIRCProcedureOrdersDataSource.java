package org.labkey.nirc_ehr.history;

import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;
import org.labkey.api.util.PageFlowUtil;

import java.sql.SQLException;
import java.util.Set;

public class NIRCProcedureOrdersDataSource extends AbstractDataSource
{
    public NIRCProcedureOrdersDataSource(Module module)
    {
        super("study", "prc_order", "Procedure Orders", "Procedures", module);
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        StringBuilder sb = new StringBuilder();

        if (rs.hasColumn(FieldKey.fromString("procedure/name")) && rs.getObject(FieldKey.fromString("procedure/name")) != null)
        {
            addRow(sb, "Procedure", rs.getString(FieldKey.fromString("procedure/name")));
        }

        if (rs.hasColumn(FieldKey.fromString("remark")) && rs.getObject("remark") != null)
        {
            addRow(sb, "Remark", rs.getString("remark"));
        }

        if (rs.hasColumn(FieldKey.fromString("windowStart")) && rs.getObject("windowStart") != null
            && rs.hasColumn(FieldKey.fromString("windowEnd")) && rs.getObject("windowEnd") != null)
        {
            addRow(sb, "Window", rs.getString("windowStart") + " to " + rs.getString("windowEnd"));
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

    @Override
    protected Set<String> getColumnNames()
    {
        return PageFlowUtil.set("Id", "date", "windowStart", "windowEnd", "procedure/name", "remark");
    }
}
