package org.labkey.nirc_ehr.history;

import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;

import java.sql.SQLException;

public class DrugAdminDataSource extends AbstractDataSource
{

    public DrugAdminDataSource(Module module)
    {
        super("study", "drug", "Drug Admin", "Drug Admin", module);
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        StringBuilder sb = new StringBuilder();

        if (rs.hasColumn(FieldKey.fromString("type")) && rs.getObject("type") != null)
        {
            addRow(sb, "Type", rs.getString("type"));
        }

        if (rs.hasColumn(FieldKey.fromString("amount")) && rs.getObject("amount") != null)
        {
            addRow(sb, "Amount", rs.getString("amount"));
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
        sb.append(" ");
    }
}
