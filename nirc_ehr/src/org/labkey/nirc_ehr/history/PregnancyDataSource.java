package org.labkey.nirc_ehr.history;

import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;
import org.labkey.api.util.PageFlowUtil;

import java.sql.SQLException;

public class PregnancyDataSource extends AbstractDataSource
{
    public PregnancyDataSource(Module module)
    {
        super("study", "pregnancy", "Pregnancy", "Pregnancy", module);
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        StringBuilder sb = new StringBuilder();

        if (rs.hasColumn(FieldKey.fromString("type")) && rs.getObject("type") != null)
        {
            addRow(sb, "Type", rs.getString("type"));
        }

        if (rs.hasColumn(FieldKey.fromString("result/title")) && rs.getObject(FieldKey.fromString("result/title")) != null)
        {
            safeAppend(rs, "Outcome", "result/title");
        }
        else
        {
            addRow(sb, "Outcome", "Unknown");
        }

        return sb.toString();
    }

    private void addRow(StringBuilder sb, String displayLabel, String value)
    {
        sb.append(displayLabel);
        sb.append(": ");
        sb.append(PageFlowUtil.filter(value));
        sb.append(", ");
    }
}

