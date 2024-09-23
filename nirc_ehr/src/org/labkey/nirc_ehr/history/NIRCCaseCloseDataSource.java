package org.labkey.nirc_ehr.history;

import org.apache.commons.lang3.time.DateUtils;
import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;
import org.labkey.api.util.DateUtil;
import org.labkey.api.util.PageFlowUtil;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Set;

public class NIRCCaseCloseDataSource extends AbstractDataSource
{
    public NIRCCaseCloseDataSource(Module module)
    {
        super("study", "Cases", "Case Closed", "Clinical", module);
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        Date start = rs.getDate(FieldKey.fromString("date"));
        Date end = rs.getDate(FieldKey.fromString("enddate"));
        if (DateUtils.isSameDay(start, end))
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();

        sb.append("Opened Date: ").append(DateUtil.formatDate(c, start));
        sb.append(safeAppend(rs, "Problem Area", "problemCategory"));
        sb.append(safeAppend(rs, "Close Remark", "closeRemark"));

        return sb.toString();
    }

    @Override
    protected String getDateField()
    {
        return "enddate";
    }

    @Override
    protected Set<String> getColumnNames()
    {
        return PageFlowUtil.set("Id", "date", "enddate", "problemCategory", "closeRemark");
    }
}
