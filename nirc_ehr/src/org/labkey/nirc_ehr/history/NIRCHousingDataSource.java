package org.labkey.nirc_ehr.history;

import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;
import org.labkey.api.util.PageFlowUtil;

import java.sql.SQLException;
import java.util.Set;

public class NIRCHousingDataSource extends AbstractDataSource
{
    public NIRCHousingDataSource(Module module)
    {
        super("study", "Housing", "Housing Transfer", "Housing Transfers", module);
        setShowTime(true);
    }

    @Override
    protected Set<String> getColumnNames()
    {
        return PageFlowUtil.set("Id", "date", "cage/cage", "room/fullRoom");
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        StringBuilder sb = new StringBuilder();

        FieldKey room = FieldKey.fromString("room/fullRoom");
        FieldKey cage = FieldKey.fromString("cage/cage");
        String value = "Unknown";
        if (rs.hasColumn(cage) && rs.getObject(cage) != null)
        {
            value = rs.getString(cage);
        }
        else if(rs.hasColumn(room) && rs.getObject(room) != null)
        {
            value = rs.getString(room);
        }

        sb.append(safeAppend(rs, "Moved to", value));
        sb.append(safeAppend(rs, "Reason", "reason"));
        sb.append(safeAppend(rs, "Remark", "remark"));

        return sb.toString();
    }
}
