package org.labkey.nirc_ehr.history;

import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;
import org.labkey.api.util.PageFlowUtil;

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

        sb.append(safeAppend(rs, "Category", "category"));
        sb.append(safeAppend(rs, "Drug/Treatment", "code/meaning"));
        sb.append(safeAppend(rs, "Route", "route"));
        boolean amountExists = rs.hasColumn(FieldKey.fromString("amount")) && rs.getObject("amount") != null;
        boolean volumeExists = rs.hasColumn(FieldKey.fromString("volume")) && rs.getObject("volume") != null;
        if (amountExists && volumeExists)
        {
            String amountAndVolume = rs.getString("amount") + " " + rs.getString("amount_units")
                    + " / " + rs.getString("volume") + " " + rs.getString("vol_units");
            sb.append(safeAppend(rs, "Amount/Volume", amountAndVolume));
        }
        else if (amountExists)
        {
            sb.append(safeAppend(rs, "Amount", rs.getString("amount") + " " + rs.getString("amount_units")));
        }
        else if (volumeExists)
        {
            sb.append(safeAppend(rs, "Volume", rs.getString("volume") + " " + rs.getString("volume_units")));
        }
        sb.append(safeAppend(rs, "Performed By", "performedby/displayName"));
        sb.append(safeAppend(rs, "Remark", "remark"));

        return sb.toString();
    }

    private void addRow(StringBuilder sb, String displayLabel, String value)
    {
        sb.append(PageFlowUtil.filter(displayLabel));
        sb.append(": ");
        sb.append(PageFlowUtil.filter(value));
        sb.append(" ");
    }
}
