package org.labkey.nirc_ehr;

import org.labkey.api.data.Container;
import org.labkey.api.data.SimpleFilter;
import org.labkey.api.data.TableInfo;
import org.labkey.api.data.TableSelector;
import org.labkey.api.module.Module;
import org.labkey.api.module.ModuleLoader;
import org.labkey.api.module.ModuleProperty;
import org.labkey.api.query.QueryService;
import org.labkey.api.query.UserSchema;
import org.labkey.api.security.User;
import org.labkey.api.util.JobRunner;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class NIRCOrchardFileGenerator
{
    private Container _container = null;
    private User _user = null;

    public static final String NIRCOrchardFileLocation = "NIRCOrchardFileLocation";

    public NIRCOrchardFileGenerator(Container c, User u)
    {
        _container = c;
        _user = u;
    }

    public void generateOrchardFile(Container c, String animalId)
    {
        String orchardFileLocation = getOrchardFileLocation(c);
        // Generate the Orchard file
        if (orchardFileLocation == null)
        {
            return;
        }
        try
        {
            // call the orchard data query
            JobRunner.getDefault().execute(TimeUnit.MINUTES.toMillis(1), () -> {

                TableInfo ti = getTableInfo("study", "orchardData");
                SimpleFilter filter = new SimpleFilter();
                filter.addCondition("Id", animalId);
                new TableSelector(ti, ti.getColumnNameSet()).forEachResults(rs -> {
                    // Generate the Orchard file
                    // ...
                });

            });

        }
        catch (Exception e)
        {
            throw new RuntimeException("Error generating Orchard file", e);
        }
    }

    public String getOrchardFileLocation(Container c)
    {
        Module ehr = ModuleLoader.getInstance().getModule(NIRC_EHRModule.NAME);
        if (ehr == null)
        {
            throw new IllegalStateException("Could not find the NIRC EHR module");
        }
        ModuleProperty mp = ehr.getModuleProperties().get(NIRCOrchardFileLocation);
        return mp.getEffectiveValue(c);
    }

    private TableInfo getTableInfo(String schemaName, String queryName)
    {
        UserSchema us = QueryService.get().getUserSchema(_user, _container, schemaName);
        if (us == null)
            throw new IllegalArgumentException("Unable to find schema: " + schemaName);

        TableInfo ti = us.getTable(queryName);
        if (ti == null)
            throw new IllegalArgumentException("Unable to find table: " + schemaName + "." + queryName);

        return ti;
    }
}
