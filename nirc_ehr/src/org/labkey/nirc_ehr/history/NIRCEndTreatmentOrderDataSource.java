package org.labkey.nirc_ehr.history;

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
import org.labkey.api.util.PageFlowUtil;

import java.sql.SQLException;
import java.util.List;

public class NIRCEndTreatmentOrderDataSource extends AbstractDataSource
{
    public NIRCEndTreatmentOrderDataSource(Module module)
    {
        super("study", "treatment_order", "End Treatment Orders", "Clinical", module);
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        StringBuilder sb = new StringBuilder();

        sb.append(safeAppend(rs, "Treatment", "code"));
        sb.append(safeAppend(rs, "Frequency", "frequency/meaning"));
        boolean amountExists = rs.hasColumn(FieldKey.fromString("amount")) && rs.getObject("amount") != null;
        boolean volumeExists = rs.hasColumn(FieldKey.fromString("volume")) && rs.getObject("volume") != null;
        if (amountExists && volumeExists)
        {
            String amountAndVolume = rs.getString("amount") + " " + rs.getString("amount_units")
                    + " / " + rs.getString("volume") + " " + rs.getString("vol_units");
            addRow(sb, "Amount/Volume", amountAndVolume);
        }
        else if (amountExists)
        {
            sb.append(safeAppend(rs, "Amount", rs.getString("amount") + " " + rs.getString("amount_units")));
        }
        else if (volumeExists)
        {
            sb.append(safeAppend(rs, "Volume", rs.getString("volume") + " " + rs.getString("volume_units")));
        }
        sb.append(safeAppend(rs, "End Ordered By", "orderedby/displayName"));

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

    private void addRow(StringBuilder sb, String displayLabel, String value)
    {
        sb.append(PageFlowUtil.filter(displayLabel));
        sb.append(": ");
        sb.append(PageFlowUtil.filter(value));
        sb.append("\n");
    }
}
