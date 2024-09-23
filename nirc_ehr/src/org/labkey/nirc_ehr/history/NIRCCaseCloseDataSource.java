package org.labkey.nirc_ehr.history;

import org.apache.commons.lang3.time.DateUtils;
import org.jetbrains.annotations.NotNull;
import org.labkey.api.data.CompareType;
import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.data.SimpleFilter;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.ehr.history.HistoryRow;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;
import org.labkey.api.security.User;
import org.labkey.api.util.DateUtil;
import org.labkey.api.util.PageFlowUtil;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
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
    protected @NotNull List<HistoryRow> getRows(Container c, User u, SimpleFilter filter, boolean redacted)
    {
        filter.addCondition(FieldKey.fromString(getDateField()), null, CompareType.NONBLANK);
        return super.getRows(c, u, filter, redacted);
    }

    @Override
    protected Set<String> getColumnNames()
    {
        return PageFlowUtil.set("Id", "date", "enddate", "problemCategory", "closeRemark");
    }
}
