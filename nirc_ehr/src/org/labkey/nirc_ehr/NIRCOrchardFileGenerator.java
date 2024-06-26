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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NIRCOrchardFileGenerator
{
    public static final String NIRCOrchardFileLocation = "NIRCOrchardFileLocation";
    private static final String orchardFileName = "orchardFile.txt";

    public void generateOrchardFile(Container c, User u, String taskid)
    {
        String orchardFileLocation = getOrchardFileLocation(c);
        // Generate the Orchard file
        if (orchardFileLocation == null)
        {
            return;
        }

        try (DbScope.Transaction transaction = StudyService.get().getDatasetSchema().getScope().ensureTransaction())
        {
            // This will add a post commit task to write the file. The post commit task will only be added once for every taskid
            transaction.addCommitTask(new FileWriteTask(c, u, taskid, orchardFileLocation), DbScope.CommitTaskOption.POSTCOMMIT);
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

    private static class FileWriteTask implements Runnable
    {
        private final Container c;
        private final User u;
        private final String taskid;
        private final String orchardFileLocation;

        public FileWriteTask(Container c, User u, String taskid, String orchardFileLocation)
        {
            this.c = c;
            this.u = u;
            this.taskid = taskid;
            this.orchardFileLocation = orchardFileLocation;
        }

        @Override
        public void run()
        {
            JobRunner.getDefault().execute(() ->
            {
                String filterList = String.join(",", getAnimalIds(c, u, taskid));

                TableInfo ti = getTableInfo(c, u, "study", "orchardData");
                SimpleFilter filter = new SimpleFilter(FieldKey.fromString("Id"), filterList, CompareType.CONTAINS_ONE_OF);
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
        }

        // Overwritten to prevent duplicate tasks being added for a given taskid
        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof FileWriteTask fwt)
            {
                return taskid.equals(fwt.taskid);
            }

            return super.equals(obj);
        }

        // Overwritten to prevent duplicate tasks being added for a given taskid
        @Override
        public int hashCode()
        {
            return Objects.hash(taskid);
        }

        private TableInfo getTableInfo(Container c, User u, String schemaName, String queryName)
        {
            UserSchema us = QueryService.get().getUserSchema(u, c, schemaName);
            if (us == null)
                throw new IllegalArgumentException("Unable to find schema: " + schemaName);

            TableInfo ti = us.getTable(queryName);
            if (ti == null)
                throw new IllegalArgumentException("Unable to find table: " + schemaName + "." + queryName);

            return ti;
        }

        // Gets a list of animal ids for a given taskid using the query orchardIdsForTaskid
        private List<String> getAnimalIds(Container c, User u, String taskid)
        {
            TableInfo ti = getTableInfo(c, u, "study", "orchardIdsForTaskid");
            TableSelector selector = new TableSelector(ti, null, null);

            Map<String, Object> params = new HashMap<>();
            params.put("MYTASKID", taskid);
            selector.setNamedParameters(params);

            return selector.getArrayList(String.class);
        }
    }
}
