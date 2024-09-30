package org.labkey.nirc_ehr.history;

import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;

import java.sql.SQLException;

public class NIRCHousingDataSource extends AbstractDataSource
{
    public NIRCHousingDataSource(Module module)
    {
        super("study", "Housing", "Housing Transfer", "Housing Transfers", module);
        setShowTime(true);
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        StringBuilder sb = new StringBuilder();

        FieldKey room = FieldKey.fromString("room");
        FieldKey cage = FieldKey.fromString("cage/fullRoom");
        String value = "";
        if (rs.hasColumn(room) && rs.getObject(room) != null)
            value = rs.getString(room);

        if (rs.hasColumn(cage) && rs.getObject(cage) != null)
            value += " / " + rs.getString(cage);

        sb.append("Moved to: " + value).append("\n");

        sb.append(safeAppend(rs, "Reason", "reason"));
        sb.append(safeAppend(rs, "Remark", "remark"));

        return sb.toString();
    }
}
