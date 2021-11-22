package org.labkey.nirc_ehr.table;

import org.labkey.api.data.AbstractTableInfo;
import org.labkey.api.data.Container;
import org.labkey.api.data.TableInfo;
import org.labkey.api.ehr.EHRService;
import org.labkey.api.ldk.table.AbstractTableCustomizer;
import org.labkey.api.query.QueryForeignKey;
import org.labkey.api.query.UserSchema;

public class NIRC_EHRCustomizer extends AbstractTableCustomizer
{
    public static final String PERFORMEDBY_CONCEPT_URI = "urn:ehr.labkey.org/#PerformedBy";

    public UserSchema getEHRUserSchema(AbstractTableInfo ds, String name)
    {
        Container ehrContainer = EHRService.get().getEHRStudyContainer(ds.getUserSchema().getContainer());
        if (ehrContainer == null)
            return null;

        return getUserSchema(ds, name, ehrContainer);
    }

    @Override
    public void customize(TableInfo table)
    {
        if (table instanceof AbstractTableInfo)
        {
            doSharedCustomization((AbstractTableInfo) table);
        }
    }

    public void doSharedCustomization(AbstractTableInfo ti)
    {
        for (var col : ti.getMutableColumns())
        {
            if ("performedby".equalsIgnoreCase(col.getName()) && null == col.getFk())
            {
                UserSchema us = getEHRUserSchema(ti, "core");
                col.setLabel("PerformedBy");
                col.setFk(new QueryForeignKey(ti.getUserSchema(), ti.getContainerFilter(), us, null, "SiteUsers", "UserId", "DisplayName"));
            }
            if ("species".equalsIgnoreCase(col.getName()) && null == col.getFk())
            {
                UserSchema us = getEHRUserSchema(ti, "ehr_lookups");
                col.setLabel("Species");
                col.setFk(new QueryForeignKey(ti.getUserSchema(), ti.getContainerFilter(), us, null, "species_codes", "code", "scientific_name"));
            }
            if ("gender".equalsIgnoreCase(col.getName()) && null == col.getFk())
            {
                UserSchema us = getEHRUserSchema(ti, "ehr_lookups");
                col.setLabel("Gender");
                col.setFk(new QueryForeignKey(ti.getUserSchema(), ti.getContainerFilter(), us, null, "gender_codes", "code", "meaning"));
            }
        }
    }
}
