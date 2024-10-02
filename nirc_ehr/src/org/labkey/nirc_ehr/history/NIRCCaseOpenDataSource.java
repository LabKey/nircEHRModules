package org.labkey.nirc_ehr.history;

import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;
import org.labkey.api.util.DateUtil;
import org.labkey.api.util.PageFlowUtil;

import java.sql.SQLException;
import java.util.Set;

public class NIRCCaseOpenDataSource extends AbstractDataSource
{
    public NIRCCaseOpenDataSource(Module module)
    {
        super("study", "Cases", "Case Opened", "Clinical", module);
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        StringBuilder sb = new StringBuilder();
        sb.append(safeAppend(rs, "Category", "category"));
        sb.append(safeAppend(rs, "Problem Area", "problemCategory"));
        sb.append(safeAppend(rs, "Problem", "problemSubCategory"));
        sb.append(safeAppend(rs, "Open Remark", "openRemark"));
        sb.append(safeAppend(rs, "Case Plan", "plan"));

        if (!redacted)
        {
            sb.append(safeAppend(rs, "Opened By", "performedby/displayName"));
        }

        if (rs.getObject(FieldKey.fromString("enddate")) != null)
        {
            sb.append("Closed On: ").append(DateUtil.formatDate(c, rs.getDate(FieldKey.fromString("enddate"))));
        }

        return sb.toString();
    }

    @Override
    protected Set<String> getColumnNames()
    {
        return PageFlowUtil.set("Id",
                "date",
                "enddate",
                "caseno",
                "objectid",
                "performedby/displayName",
                "category",
                "problemCategory",
                "problemSubcategory",
                "openRemark",
                "plan");
    }
}
