package org.labkey.nirc_ehr.history;

import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;
import org.labkey.api.util.Formats;
import org.labkey.api.util.PageFlowUtil;

import java.sql.SQLException;

public class NIRCVitalsDataSource extends AbstractDataSource
{
    public NIRCVitalsDataSource(Module module)
    {
        super("study", "Vitals", "Vitals", "NIRC Vitals", module);
        setShowTime(true);
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        StringBuilder sb = new StringBuilder();

        if (rs.hasColumn(FieldKey.fromString("bloodPressure")) && rs.getObject("bloodPressure") != null)
        {
            addVital(sb, "Blood Pressure: ", "",rs.getString("bloodPressure"));
        }

        if (rs.hasColumn(FieldKey.fromString("temp")) && rs.getObject("temp") != null)
        {
            addVital(sb, "Temperature: ", " f", Formats.f1.format(rs.getDouble("temp")));
        }

        if (rs.hasColumn(FieldKey.fromString("heartRate")) && rs.getObject("heartRate") != null)
        {
            addVital(sb, "Heart Rate: ", " bpm", Formats.f0.format( rs.getDouble("heartRate")));
        }

        if (rs.hasColumn(FieldKey.fromString("respRate")) && rs.getObject("respRate") != null)
        {
            addVital(sb, "Respiration Rate: ", " bpm", Formats.f0.format(rs.getInt("respRate")));
        }

        if (rs.hasColumn(FieldKey.fromString("pulseRate")) && rs.getObject("pulseRate") != null)
        {
            addVital(sb, "Pulse Rate: ", " bpm", Formats.f0.format(rs.getInt("pulseRate")));
        }

        if (rs.hasColumn(FieldKey.fromString("ekg")) && rs.getObject("ekg") != null)
        {
            addVital(sb, "EKG: ", "", Formats.f0.format(rs.getInt("ekg")));
        }

        if (rs.hasColumn(FieldKey.fromString("pulseOximetry")) && rs.getObject("pulseOximetry") != null)
        {
            addVital(sb, "Pulse Oximetry: ", "%", Formats.f0.format(rs.getInt("pulseOximetry")));
        }


        return sb.toString();
    }

    private void addVital(StringBuilder sb, String displayLabel, String suffix, String value)
    {
        sb.append(displayLabel);
        sb.append(PageFlowUtil.filter(value));
        sb.append(suffix);
        sb.append("\n");
    }
}
