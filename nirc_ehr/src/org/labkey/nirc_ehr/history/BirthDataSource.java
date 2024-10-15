package org.labkey.nirc_ehr.history;

import org.labkey.api.data.Container;
import org.labkey.api.data.Results;
import org.labkey.api.ehr.history.AbstractDataSource;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;
import org.labkey.api.util.PageFlowUtil;

import java.sql.SQLException;
import java.util.Set;

public class BirthDataSource extends AbstractDataSource
{
    public BirthDataSource(Module module)
    {
        super("study", "Birth", "Birth", "Births", module);
        setShowTime(true);
    }

    @Override
    protected Set<String> getColumnNames()
    {
        return PageFlowUtil.set("Id", "date", "Id/Demographics/gender/meaning");
    }

    @Override
    protected String getHtml(Container c, Results rs, boolean redacted) throws SQLException
    {
        StringBuilder sb = new StringBuilder();

        if(rs.hasColumn(FieldKey.fromString("Id/Demographics/gender/meaning")) && rs.getObject(FieldKey.fromString("Id/Demographics/gender/meaning")) != null)
            sb.append("Gender: " + rs.getString(FieldKey.fromString("Id/Demographics/gender/meaning")));

        return sb.toString();
    }
}
