package org.labkey.nirc_ehr.history;

import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;
import org.labkey.api.util.PageFlowUtil;

import java.sql.SQLException;
import java.util.Set;

public class CasesDataSource extends AbstractDataSource
{

    public CasesDataSource(Module module)
    {
        super("study", "cases", "Cases", "Cases", module);
    }

    @Override
    protected Set<String> getColumnNames()
    {
        return PageFlowUtil.set("Id", "date", "openDiagnosis");
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        StringBuilder sb = new StringBuilder();

        if(rs.hasColumn(FieldKey.fromString("openDiagnosis")) && rs.getObject(FieldKey.fromString("openDiagnosis")) != null)
            sb.append("Reason: " + rs.getString(FieldKey.fromString("openDiagnosis")));

        return sb.toString();
    }
}
