package org.labkey.nirc_ehr;

import org.labkey.api.data.CompareType;
import org.labkey.api.data.Container;
import org.labkey.api.data.DbScope;
import org.labkey.api.data.SimpleFilter;
import org.labkey.api.data.TableInfo;
import org.labkey.api.data.TableSelector;
import org.labkey.api.module.Module;
import org.labkey.api.module.ModuleLoader;
import org.labkey.api.module.ModuleProperty;
import org.labkey.api.query.FieldKey;
import org.labkey.api.query.QueryService;
import org.labkey.api.query.UserSchema;
import org.labkey.api.security.User;
import org.labkey.api.study.StudyService;
import org.labkey.api.util.JobRunner;
import org.labkey.api.util.PageFlowUtil;
import org.labkey.api.writer.PrintWriters;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class NIRCOrchardFileGenerator
{
    private Container _container = null;
    private User _user = null;

    public static final String NIRCOrchardFileLocation = "NIRCOrchardFileLocation";
    private static final String orchardFileName = "orchardFile.txt";

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
        try (DbScope.Transaction transaction = StudyService.get().getDatasetSchema().getScope().ensureTransaction())
        {
            transaction.addCommitTask(() ->
            {
                JobRunner.getDefault().execute(() ->
                {
                    TableInfo ti = getTableInfo("study", "orchardData");
                    SimpleFilter filter = new SimpleFilter();
                    filter.addCondition(FieldKey.fromString("Id"), Arrays.asList(animalId.split(",")), CompareType.CONTAINS_ONE_OF);
                    StringBuilder sb = new StringBuilder();

                    new TableSelector(ti, PageFlowUtil.set("Id", "date", "birth", "protocols", "housingDate", "cage", "room"), filter, null).forEachResults(rs -> {
                        sb.append(rs.getString("Id"));
                        sb.append(rs.getDate("date"));
                        sb.append(rs.getDate("birth"));
                        sb.append(rs.getString("protocols"));
                        sb.append(rs.getString("cage"));
                        sb.append(rs.getString("room"));
                        sb.append(System.lineSeparator());
                    });

                    try (PrintWriter writer = PrintWriters.getPrintWriter(new File(orchardFileLocation + File.separator + orchardFileName))) {
                        writer.write(sb.toString());
                    }
                    catch (IOException e)
                    {
                        throw new RuntimeException("Error generating Orchard file", e);
                    }
                });
            }, DbScope.CommitTaskOption.POSTCOMMIT);

            transaction.commit();
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
