package org.labkey.nirc_ehr.history;

import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;

import java.sql.SQLException;

public class DefaultBiopsyDataSource extends AbstractDataSource
{
    public DefaultBiopsyDataSource(Module module)
    {
        super("study", "Biopsy", "Biopsy", "Biopsy", module);
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        StringBuilder sb = new StringBuilder();
//        sb.append(safeAppend(rs, "Cause", "cause"));

        if (rs.hasColumn(FieldKey.fromString("type")) && rs.getObject("type") != null)
        {
            addRow(sb, "Type", rs.getString("type"));
        }

        if (rs.hasColumn(FieldKey.fromString("remark")) && rs.getObject("remark") != null)
        {
            addRow(sb, "Remark", rs.getString("remark"));
        }

        if (rs.hasColumn(FieldKey.fromString("description")) && rs.getObject("description") != null)
        {
            addRow(sb, "Description", rs.getString("description"));
        }

        return sb.toString();
    }

    private void addRow(StringBuilder sb, String displayLabel, String value)
    {
        sb.append(displayLabel);
        sb.append(": ");
        sb.append(value);
        sb.append("\n");
    }
}

