package org.labkey.nirc_ehr.history;

import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;

import java.sql.SQLException;

public class ObservationsDataSource extends AbstractDataSource
{

    public ObservationsDataSource(Module module)
    {
        super("study", "obs", "Observations", "Observations", module);
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        StringBuilder sb = new StringBuilder();

        if (rs.hasColumn(FieldKey.fromString("category")) && rs.getObject("category") != null)
        {
            addRow(sb, "Category", rs.getString("category"));
        }

        if (rs.hasColumn(FieldKey.fromString("diagnosis")) && rs.getObject("diagnosis") != null)
        {
            addRow(sb, "Diagnosis", rs.getString("diagnosis"));
        }

        if (rs.hasColumn(FieldKey.fromString("remark")) && rs.getObject("remark") != null)
        {
            addRow(sb, "Remark", rs.getString("remark"));
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
