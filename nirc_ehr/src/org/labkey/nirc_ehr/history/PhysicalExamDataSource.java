package org.labkey.nirc_ehr.history;

import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;
import org.labkey.api.util.PageFlowUtil;

import java.sql.SQLException;

public class PhysicalExamDataSource extends AbstractDataSource
{

    public PhysicalExamDataSource(Module module)
    {
        super("study", "physicalExam", "Physical Exam", "Physical Exam", module);
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        StringBuilder sb = new StringBuilder();

        if (rs.hasColumn(FieldKey.fromString("exam")) && rs.getObject("exam") != null)
        {
            addRow(sb, "Type", rs.getString("exam"));
        }

        if (rs.hasColumn(FieldKey.fromString("result")) && rs.getObject("result") != null)
        {
            addRow(sb, "Result", rs.getString("result"));
        }

        if (rs.hasColumn(FieldKey.fromString("Units")) && rs.getObject("Units") != null)
        {
            addRow(sb, "Units", rs.getString("Units"));
        }

        if (rs.hasColumn(FieldKey.fromString("Remark")) && rs.getObject("Remark") != null)
        {
            addRow(sb, "Remark", rs.getString("Remark"));
        }
        return sb.toString();
    }

    private void addRow(StringBuilder sb, String displayLabel, String value)
    {
        sb.append(displayLabel);
        sb.append(": ");
        sb.append(PageFlowUtil.filter(value));
        sb.append(" ");
    }
}
