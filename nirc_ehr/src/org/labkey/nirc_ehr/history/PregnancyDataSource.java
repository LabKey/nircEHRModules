package org.labkey.nirc_ehr.history;

import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;
import org.labkey.api.util.PageFlowUtil;

import java.sql.SQLException;
import java.util.Set;

public class PregnancyDataSource extends AbstractDataSource
{
    public PregnancyDataSource(Module module)
    {
        super("study", "pregnancy", "Pregnancy", "Pregnancy", module);
    }

    @Override
    protected Set<String> getColumnNames()
    {
        return PageFlowUtil.set("Id", "date", "result/title");
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        StringBuilder sb = new StringBuilder();

        FieldKey outcome = FieldKey.fromString("result/title");
        if (rs.hasColumn(outcome) && rs.getObject(outcome) != null)
        {
            addRow(sb, "Outcome", rs.getString(outcome));
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
    }
}

