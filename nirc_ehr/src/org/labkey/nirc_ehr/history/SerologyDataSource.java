package org.labkey.nirc_ehr.history;

import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;
import org.labkey.api.util.PageFlowUtil;

import java.sql.SQLException;
import java.util.Set;

public class SerologyDataSource extends AbstractDataSource
{

    public SerologyDataSource(Module module)
    {
        super("study", "Serology", "Serology", "Serology", module);
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        StringBuilder sb = new StringBuilder();

        if (rs.hasColumn(FieldKey.fromString("type")) && rs.getObject("type") != null)
        {
            addRow(sb, "Type", rs.getString("type"));
        }

        if (rs.hasColumn(FieldKey.fromString("lab")) && rs.getObject("lab") != null)
        {
            addRow(sb, "Lab", rs.getString("lab"));
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
