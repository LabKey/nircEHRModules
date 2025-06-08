package org.labkey.nirc_ehr.query;

import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.labkey.api.collections.CaseInsensitiveHashMap;
import org.labkey.api.data.ColumnInfo;
import org.labkey.api.data.CompareType;
import org.labkey.api.data.Container;
import org.labkey.api.data.ContainerManager;
import org.labkey.api.data.ConvertHelper;
import org.labkey.api.data.DbScope;
import org.labkey.api.data.Results;
import org.labkey.api.data.ResultsImpl;
import org.labkey.api.data.SimpleFilter;
import org.labkey.api.data.Sort;
import org.labkey.api.data.TableInfo;
import org.labkey.api.data.TableSelector;
import org.labkey.api.ehr.EHRDemographicsService;
import org.labkey.api.ehr.EHRService;
import org.labkey.api.ehr.security.EHRVeterinarianPermission;
import org.labkey.api.ldk.notification.NotificationService;
import org.labkey.api.query.BatchValidationException;
import org.labkey.api.query.DuplicateKeyException;
import org.labkey.api.query.FieldKey;
import org.labkey.api.query.InvalidKeyException;
import org.labkey.api.query.QueryService;
import org.labkey.api.query.QueryUpdateServiceException;
import org.labkey.api.query.UserSchema;
import org.labkey.api.security.User;
import org.labkey.api.security.UserManager;
import org.labkey.api.security.UserPrincipal;
import org.labkey.api.settings.AppProps;
import org.labkey.api.settings.LookAndFeelProperties;
import org.labkey.api.study.StudyService;
import org.labkey.api.util.GUID;
import org.labkey.api.util.JobRunner;
import org.labkey.api.util.PageFlowUtil;
import org.labkey.nirc_ehr.NIRCOrchardFileGenerator;
import org.labkey.nirc_ehr.NIRC_EHRManager;
import org.labkey.nirc_ehr.notification.NIRCClinicalMoveNotification;
import org.labkey.nirc_ehr.notification.NIRCDeathNotification;
import org.labkey.nirc_ehr.notification.NIRCPregnancyOutcomeNotification;
import org.labkey.nirc_ehr.notification.TriggerScriptNotification;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class NIRC_EHRTriggerHelper
{
    private Container _container;
    private User _user;
    private static final Logger _log = LogManager.getLogger(NIRC_EHRTriggerHelper.class);
    private final Map<String,Object> _cachedDrugFormulary = new HashMap<>();

    private final SimpleDateFormat _dateFormat;

    public NIRC_EHRTriggerHelper(int userId, String containerId)
    {
        _user = UserManager.getUser(userId);
        if (_user == null)
            throw new RuntimeException("User does not exist: " + userId);

        _container = ContainerManager.getForId(containerId);
        if (_container == null)
            throw new RuntimeException("Container does not exist: " + containerId);

        _dateFormat = new SimpleDateFormat(LookAndFeelProperties.getInstance(_container).getDefaultDateFormat());

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

    public Map<String, Object> getExtraContext()
    {
        Map<String, Object> map = new HashMap<>();
        map.put("quickValidation", true);
        map.put("generatedByServer", true);

        return map;
    }

    public String createHousingRecord(String id, Map<String, Object> row, String formName) throws QueryUpdateServiceException, DuplicateKeyException, SQLException, BatchValidationException, InvalidKeyException
    {
        BatchValidationException errors = new BatchValidationException();
        Date date = ConvertHelper.convert(row.get("date"), Date.class);
        String location = ConvertHelper.convert(row.get("cage"), String.class);
        String reason = ConvertHelper.convert(row.get("reason"), String.class);
        if (id == null || date == null || location == null)
            return "Attempting to create a housing record with no id, date, or location";

        boolean updateRecord = false;
        Date enddate = ConvertHelper.convert(row.get("enddate"), Date.class);

        //check for a pre-existing death record
        Date deathDate = new TableSelector(getTableInfo("study", "deaths"), Collections.singleton("date"), new SimpleFilter(FieldKey.fromString("Id"), id), null).getObject(Date.class);
        if (deathDate != null)
        {
            if (deathDate.before(date))
            {
                return "Attempting to create a housing record that starts after the death date: " + _dateFormat.format(date);
            }
            else if (enddate == null || enddate.after(deathDate))
            {
                enddate = deathDate;
            }
        }

        TableInfo ti = getTableInfo("study", "housing");

        String taskId = ConvertHelper.convert(row.get("taskid"), String.class);
        if (taskId == null)
            return "Attempting to create " + formName + " record with no taskid";

        // If updating an existing arrival record with housing info, check if the housing record should be closed
        if (enddate == null)
        {
            SimpleFilter nextFilter = new SimpleFilter(FieldKey.fromString("Id"), id);
            nextFilter.addCondition(FieldKey.fromString("date"), date, CompareType.DATE_GTE);
            nextFilter.addCondition(FieldKey.fromString("taskid"), taskId, CompareType.NEQ); // Don't include the current record
            TableSelector ts = new TableSelector(ti, PageFlowUtil.set("date"), nextFilter, new Sort("date"));
            List<Date> dates = ts.getArrayList(Date.class);
            if (!dates.isEmpty())
                enddate = dates.get(0);
        }

        String qcstate = ConvertHelper.convert(row.get("qcstate"), String.class);
        if (qcstate == null)
            return "Attempting to create " + formName + " record with no qcstate";

        // If there is already a housing record for this task, update that record
        SimpleFilter filter = new SimpleFilter(FieldKey.fromString("Id"), id);
        filter.addCondition(FieldKey.fromString("taskid"), taskId);
        TableSelector ts = new TableSelector(ti, PageFlowUtil.set("lsid", "objectid"), filter, null);
        if (ts.exists())
            updateRecord = true;

        Map<String, Object> saveRow = new CaseInsensitiveHashMap<>();
        saveRow.put("Id", id);
        saveRow.put("date", date);
        saveRow.put("cage", location);
        saveRow.put("taskId", taskId);
        saveRow.put("qcstate", qcstate);
        saveRow.put("reason", reason);
        if (updateRecord)
            saveRow.put("objectid", ts.getMap().get("objectid"));
        else
            saveRow.put("objectid", new GUID().toString());

        if (enddate != null)
            saveRow.put("enddate", enddate);

        List<Map<String, Object>> rows = new ArrayList<>();
        rows.add(saveRow);

        if (updateRecord)
        {
            ti.getUpdateService().updateRows(_user, _container, rows, null, null, getExtraContext());
        }
        else
        {
            ti.getUpdateService().insertRows(_user, _container, rows, errors, null, getExtraContext());
        }

        if (errors.hasErrors())
            throw errors;

        return null;
    }

    public String saveBirthRecord(String id, Map<String, Object> row) throws QueryUpdateServiceException, DuplicateKeyException, SQLException, BatchValidationException, InvalidKeyException
    {
        Date date = ConvertHelper.convert(row.get("date"), Date.class);
        if (id == null || date == null)
            return "Attempting to create a birth record with no id or date";

        boolean updateRecord = false;

        //check for a pre-existing death record
        Date deathDate = new TableSelector(getTableInfo("study", "deaths"), Collections.singleton("date"), new SimpleFilter(FieldKey.fromString("Id"), id), null).getObject(Date.class);
        if (deathDate != null)
        {
            if (deathDate.before(date))
            {
                return "Attempting to create a birth record that starts after the death date: " + _dateFormat.format(date);
            }
        }

        String taskId = ConvertHelper.convert(row.get("taskid"), String.class);
        if (taskId == null) {
            return "Attempting to create a birth record with no taskid";
        }

        String qcstate = ConvertHelper.convert(row.get("qcstate"), String.class);
        if (qcstate == null) {
            return "Attempting to create a birth record with no qcstate";
        }

        TableInfo ti = getTableInfo("study", "birth");

        // If there is already a housing record for this task, update that record
        SimpleFilter filter = new SimpleFilter(FieldKey.fromString("Id"), id);
        filter.addCondition(FieldKey.fromString("taskid"), taskId);
        TableSelector ts = new TableSelector(ti, PageFlowUtil.set("lsid", "objectid"), filter, null);
        if (ts.exists())
        {
            updateRecord = true;
        }

        Map<String, Object> saveRow = new CaseInsensitiveHashMap<>();
        saveRow.put("Id", id);
        saveRow.put("date", date);
        saveRow.put("taskId", taskId);
        saveRow.put("qcstate", qcstate);
        if (updateRecord)
        {
            saveRow.put("objectid", ts.getMap().get("objectid"));
        }
        else
        {
            saveRow.put("objectid", new GUID().toString());
        }

        List<Map<String, Object>> rows = new ArrayList<>();
        rows.add(saveRow);
        BatchValidationException errors = new BatchValidationException();

        if (updateRecord)
        {
            ti.getUpdateService().updateRows(_user, _container, rows, null, null, getExtraContext());
        }
        else
        {
            ti.getUpdateService().insertRows(_user, _container, rows, errors, null, getExtraContext());
        }

        if (errors.hasErrors())
            throw errors;

        return null;
    }

    public boolean birthExists(String id)
    {
        TableInfo ti = getTableInfo("study", "birth");
        if (ti != null)
        {
            SimpleFilter filter = new SimpleFilter(FieldKey.fromString("Id"), id);
            TableSelector ts = new TableSelector(ti, PageFlowUtil.set("lsid"), filter, null);
            return ts.exists();
        }
        return false;
    }
    public boolean deathExists(String id)
    {
        TableInfo ti = getTableInfo("study", "deaths");
        if (ti != null)
        {
            SimpleFilter filter = new SimpleFilter(FieldKey.fromString("Id"), id);
            TableSelector ts = new TableSelector(ti, PageFlowUtil.set("lsid"), filter, null);
            return ts.exists();
        }
        return false;
    }

    public void upsertWeightRecord(Map<String, Object> row) throws QueryUpdateServiceException, DuplicateKeyException, SQLException, BatchValidationException, InvalidKeyException
    {
        BatchValidationException errors = new BatchValidationException();
        Date date = ConvertHelper.convert(row.get("date"), Date.class);
        String taskId = ConvertHelper.convert(row.get("taskid"), String.class);

        TableInfo ti = getTableInfo("study", "weight");

        // If there is already a weight record for this task, update that record
        SimpleFilter filter = new SimpleFilter(FieldKey.fromString("Id"), row.get("Id"));
        filter.addCondition(FieldKey.fromString("taskid"), taskId);
        TableSelector ts = new TableSelector(ti, PageFlowUtil.set("lsid", "objectid"), filter, null);
        boolean updateRecord = ts.exists();

        Map<String, Object> saveRow = new CaseInsensitiveHashMap<>();
        saveRow.put("Id", row.get("Id"));
        saveRow.put("date", date);
        saveRow.put("taskid", taskId);
        saveRow.put("qcstate", row.get("qcstate"));
        if (updateRecord)
        {
            saveRow.put("objectid", ts.getMap().get("objectid"));
        }
        else
        {
            saveRow.put("objectid", new GUID().toString());
        }

        Double weight = null;
        if (row.get("weight") != null)
        {
            weight = ConvertHelper.convert(row.get("weight"), Double.class);
        }
        saveRow.put("weight", weight);

        List<Map<String, Object>> rows = new ArrayList<>();
        rows.add(saveRow);

        if (updateRecord)
        {
            ti.getUpdateService().updateRows(_user, _container, rows, null, null, getExtraContext());
        }
        else
        {
            ti.getUpdateService().insertRows(_user, _container, rows, errors, null, getExtraContext());
        }

        if (errors.hasErrors())
            throw errors;
    }

    public void clinicalMoveNotification(final String animalId, final String date)
    {
        //check whether Death Notification is enabled
        if (!NotificationService.get().isActive(new NIRCClinicalMoveNotification(), _container) || !NotificationService.get().isServiceEnabled())
        {
            _log.info("NIRC Clinical Move notification service is not enabled, will not send clinical move notification.");
            return;
        }

        try (DbScope.Transaction transaction = StudyService.get().getDatasetSchema().getScope().ensureTransaction())
        {
            // Add a post commit task to run provider update in another thread once this transaction is complete.
            transaction.addCommitTask(() ->
                    JobRunner.getDefault().execute(() -> {
                        final Container container = _container;
                        final User user = _user;
                        NIRCClinicalMoveNotification notification = new NIRCClinicalMoveNotification();
                        String subject = "Clinical Move Notification: " + animalId;

                        // get recipients
                        Set<UserPrincipal> recipients = NotificationService.get().getRecipients(notification, container);
                        if (recipients.isEmpty())
                        {
                            _log.warn("No NIRC recipients set, skipping clinical move notification");
                            return;
                        }

                        String remark = (String) EHRDemographicsService.get().getAnimal(container, animalId).getActiveHousing().get(0).get("remark");
                        String performedBy = (String) EHRDemographicsService.get().getAnimal(container, animalId).getActiveHousing().get(0).get("performedBy");

                        //construct html for email notification
                        final StringBuilder html = new StringBuilder();
                        html.append("Animal ").append(PageFlowUtil.filter(animalId)).append(" has been moved for Veterinary Treatment on ").append(date).append(".<br>");
                        html.append("Performed By: ").append(PageFlowUtil.filter(performedBy)).append("<br>");
                        html.append("Remark: ").append(PageFlowUtil.filter(remark)).append("<br><br>");

                        //append animal details
                        appendAnimalDetails(html, animalId, container);

                        // send Clinical Move Notification
                        _log.debug("NIRC Clinical Move notification job sending email for animal " + animalId + " in container " + container.getPath());
                        TriggerScriptNotification.sendMessage(subject, html.toString(), recipients, container, user);
                    }), DbScope.CommitTaskOption.POSTCOMMIT);

            transaction.commit();
        }
    }

    public void sendDeathNotification(final String animalId) throws Exception
    {
        //check whether Death Notification is enabled
        if (!NotificationService.get().isActive(new NIRCDeathNotification(), _container) || !NotificationService.get().isServiceEnabled())
        {
            _log.info("NIRC Death notification service is not enabled, will not send death notification.");
            return;
        }

        try (DbScope.Transaction transaction = StudyService.get().getDatasetSchema().getScope().ensureTransaction())
        {
            // Add a post commit task to run provider update in another thread once this transaction is complete.
            transaction.addCommitTask(() ->
                    JobRunner.getDefault().execute(() -> {
                        final Container container = _container;
                        final User user = _user;
                        String subject = "Death Notification: " + animalId;

                        // get recipients
                        Set<UserPrincipal> recipients = NotificationService.get().getRecipients(new NIRCDeathNotification(), container);
                        if (recipients.isEmpty())
                        {
                            _log.warn("No NIRC recipients set, skipping death notification");
                            return;
                        }

                        //get death info
                        TableInfo deaths = getTableInfo("study", "deathNotification");
                        TableSelector deathsTs = new TableSelector(deaths, PageFlowUtil.set("Id", "date", "taskid", "performedBy", "reason"), new SimpleFilter(FieldKey.fromString("Id"), animalId), null);
                        final Mutable<Date> deathDate = new MutableObject<>();
                        final Mutable<String> taskId = new MutableObject<>();
                        final Mutable<String> performedBy = new MutableObject<>();
                        final Mutable<String> disposition = new MutableObject<>();
                        deathsTs.forEach(rs -> {
                            if (rs.getString("date") != null)
                            {
                                Date date = ConvertHelper.convert(rs.getString("date"), Date.class);
                                deathDate.setValue(date);
                                taskId.setValue(rs.getString("taskid"));
                                performedBy.setValue(rs.getString("performedBy"));
                                disposition.setValue(rs.getString("reason"));
                            }
                        });

                        //construct html for email notification
                        final StringBuilder html = new StringBuilder();
                        if (deathDate.getValue() == null)
                        {
                            _log.error("NIRC death notification job found no death date for animal " + animalId + " in container " + _container.getPath());
                            html.append("Death date not found. Please contact system administrator.").append("<br>");
                            return;
                        }
                        html.append("Animal '").append(PageFlowUtil.filter(animalId)).append("' has been declared dead on '").append(_dateFormat.format(deathDate.getValue())).append("'.<br>");
                        html.append("Performed By: ").append(PageFlowUtil.filter(performedBy.getValue())).append("<br>");
                        html.append("Disposition: ").append(PageFlowUtil.filter(disposition.getValue())).append("<br><br>");

                        //append animal details
                        appendAnimalDetails(html, animalId, container);

                        //append link to Necropsy form
                        String url = AppProps.getInstance().getBaseServerUrl() + AppProps.getInstance().getContextPath() + "/ehr" +
                                container.getPath() + "/dataEntryForm.view?formType=Necropsy&taskid=" + taskId.getValue();
                        html.append("<a href='").append(PageFlowUtil.filter(url)).append("'>");

                        html.append("Click here to record Necropsy</a><br>");

                        // send Death Notification
                        _log.debug("NIRC Death notification job sending email for animal " + animalId + " in container " + container.getPath());
                        TriggerScriptNotification.sendMessage(subject, html.toString(), recipients, container, user);
                    }), DbScope.CommitTaskOption.POSTCOMMIT);

            transaction.commit();
        }
    }

    public void generateOrchardFile(final String taskid) throws Exception
    {
        NIRCOrchardFileGenerator orchardFileGenerator = NIRC_EHRManager.getOrchardFileGenerator();
        orchardFileGenerator.generateOrchardFile(_container, _user, taskid);
    }

    private void appendAnimalDetails(StringBuilder html, String id, final Container container)
    {
        String url = AppProps.getInstance().getBaseServerUrl() + AppProps.getInstance().getContextPath() + "/ehr" + container.getPath() + "/participantView.view?participantId=" + id;

        String tdFieldStyle = "\"border: 1px solid #000000;padding:5px;background-color:lightgray\"";
        String tdValueStyle = "\"border: 1px solid #000000;padding:5px;\"";

        String tableStyle = "\"" +
                "        border-collapse: collapse;" +
                "        border: 1px solid #000000;\"";

        String cage;
        List<Map<String, Object>> activeHousing = EHRDemographicsService.get().getAnimal(container, id).getActiveHousing();
        if (null != activeHousing && !activeHousing.isEmpty())
        {
            cage = (String) activeHousing.get(0).get("cage/cage");
        }
        else
        {
            cage = "Not Found";
        }

        html.append("<table style=").append(tableStyle).append(">");
        html.append("<tr><td style=").append(tdFieldStyle).append(">").append("Id").append("</td>").append("<td style=").append(tdValueStyle).append(">").append(PageFlowUtil.filter(id)).append("</td></tr>");
        html.append("<tr><td style=").append(tdFieldStyle).append(">").append("Location").append("</td>").append("<td style=").append(tdValueStyle).append(">").append(PageFlowUtil.filter(cage)).append("</td></tr>");
        html.append("<tr><td style=").append(tdFieldStyle).append(">").append("Project").append("</td>").append("<td style=").append(tdValueStyle).append(">").append(PageFlowUtil.filter(getProject(id))).append("</td></tr>");
        html.append("<tr><td style=").append(tdFieldStyle).append(">").append("Protocol").append("</td>").append("<td style=").append(tdValueStyle).append(">").append(PageFlowUtil.filter(getProtocol(id))).append("</td></tr>");
        html.append("</table>");
        html.append("<br>");
        html.append("<a href='").append(url).append("'>");
        html.append("Click here to view this animal's clinical details</a><br>");
    }

    private String getProject(String id)
    {
        TableInfo ti = getTableInfo("study", "notificationAnimalProject");
        SimpleFilter filter = new SimpleFilter(FieldKey.fromString("Id"), id);
        TableSelector ts = new TableSelector(ti, PageFlowUtil.set("project"), filter, null);
        final Mutable<String> project = new MutableObject<>();
        ts.forEach(rs -> {
            project.setValue(rs.getString("project"));
        });
        return project.getValue();
    }

    private String getProtocol(String id)
    {
        TableInfo ti = getTableInfo("study", "protocolAssignment");
        SimpleFilter filter = new SimpleFilter(FieldKey.fromString("Id"), id);
        filter.addCondition(FieldKey.fromString("enddate"), null, CompareType.ISBLANK);

        Set<FieldKey> keys = new HashSet<>();
        keys.add(FieldKey.fromString("protocol/title"));
        keys.add(FieldKey.fromString("protocol/InvestigatorId/LastName"));
        final Map<FieldKey, ColumnInfo> cols = QueryService.get().getColumns(ti, keys);
        TableSelector ts = new TableSelector(ti, cols.values(), filter, null);

        final Mutable<String> protocol = new MutableObject<>();

        ts.forEach(object -> {
            Results rs = new ResultsImpl(object, cols);
            String title = rs.getString(FieldKey.fromString("protocol/title"));
            String inves = rs.getString(FieldKey.fromString("protocol/InvestigatorId/LastName"));
            if (title == null)
            {
                protocol.setValue("None");
            }
            else
            {
                protocol.setValue(title + (inves == null ? "" : " - " + inves));
            }
        });
        return protocol.getValue();
    }

    public String createAssignmentRecord(String dataset, String id, Map<String, Object> row) throws SQLException, BatchValidationException, QueryUpdateServiceException, InvalidKeyException, DuplicateKeyException
    {
        BatchValidationException errors = new BatchValidationException();
        Date date = ConvertHelper.convert(row.get("date"), Date.class);
        if (id == null || date == null)
            return "Attempting to create a project assignment record with no id or date";

        TableInfo ti = getTableInfo("study", dataset);

        String taskId = ConvertHelper.convert(row.get("taskid"), String.class);
        if (taskId == null) {
            return "Attempting to create a project assignment record with no taskid";
        }

        String qcstate = ConvertHelper.convert(row.get("qcstate"), String.class);
        if (qcstate == null) {
            return "Attempting to create a project assignment record with no qcstate";
        }

        boolean updateRecord = false;

        // If there is already a project assignment record for this task, update that record
        SimpleFilter filter = new SimpleFilter(FieldKey.fromString("Id"), id);
        filter.addCondition(FieldKey.fromString("taskid"), taskId);
        TableSelector ts = new TableSelector(ti, PageFlowUtil.set("lsid", "objectid"), filter, null);
        if (ts.exists())
        {
            updateRecord = true;
        }

        Map<String, Object> saveRow = new CaseInsensitiveHashMap<>();
        saveRow.put("Id", id);
        saveRow.put("date", date);
        saveRow.put("taskId", taskId);
        saveRow.put("qcstate", qcstate);
        if (updateRecord)
        {
            saveRow.put("objectid", ts.getMap().get("objectid"));
        }
        else
        {
            saveRow.put("objectid", new GUID().toString());
        }

        String project = ConvertHelper.convert(row.get("project"), String.class);
        if (project != null)
        {
            saveRow.put("project", project);
        }
        else if (dataset.equalsIgnoreCase("assignment"))
        {
            return "Attempting to create a project assignment record with no project";
        }

        String protocol = ConvertHelper.convert(row.get("protocol"), String.class);
        if (protocol != null)
        {
            saveRow.put("protocol", protocol);
        }
        else if (dataset.equalsIgnoreCase("protocolAssignment"))
        {
            return "Attempting to create a protocol assignment record with no protocol";
        }

        List<Map<String, Object>> rows = new ArrayList<>();
        rows.add(saveRow);

        if (updateRecord)
        {
            ti.getUpdateService().updateRows(_user, _container, rows, null, null, getExtraContext());
        }
        else
        {
            ti.getUpdateService().insertRows(_user, _container, rows, errors, null, getExtraContext());
        }

        if (errors.hasErrors())
            throw errors;

        return null;
    }

    public int getUserId (String displayName)
    {
        User u = UserManager.getUserByDisplayName(displayName);
        return null != u ? u.getUserId() : -1;
    }

    public long totalHousingRecords(String location)
    {
        TableInfo ti = getTableInfo("study", "housing");

        SimpleFilter filter = new SimpleFilter(FieldKey.fromString("isActive"), true);
        filter.addCondition(FieldKey.fromString("cage"), location);
        TableSelector ts = new TableSelector(ti, PageFlowUtil.set("Id"), filter, null);

        return ts.getRowCount();
    }

    public long totalRecords(String schemaName, String queryName, String columnName, String value)
    {
        TableInfo ti = getTableInfo(schemaName, queryName);

        SimpleFilter filter = new SimpleFilter(FieldKey.fromString(columnName), value);
        TableSelector ts = new TableSelector(ti, PageFlowUtil.set(columnName), filter, null);

        return ts.getRowCount();
    }

    public boolean canCloseCase(String category)
    {
        if (_container.hasPermission(_user, EHRVeterinarianPermission.class))
            return true;
        return false;
    }

    public void closeDailyClinicalObs(String caseid, String enddate) throws SQLException
    {
        TableInfo ti = getTableInfo("study", "observation_order");
        SimpleFilter filter = new SimpleFilter(FieldKey.fromString("caseid"), caseid);
        TableSelector ts = new TableSelector(ti, PageFlowUtil.set("objectid"), filter, null);

        Map<String, Object>[] orders = ts.getMapArray();
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Map<String, Object> order : orders)
        {

            Map<String, Object> row = new CaseInsensitiveHashMap<>();
            row.put("objectid", order.get("objectid"));
            row.put("enddate", enddate);
            rows.add(row);
        }
        try
        {
            ti.getUpdateService().updateRows(_user, _container, rows, null, null, getExtraContext());
        }
        catch (Exception e)
        {
            _log.error("Error closing daily clinical observation order", e);
        }
    }

    public void ensureDailyClinicalObservationOrders(String id, String caseid, final Date date, String performedby, String qcstate, String taskid, List<Map<String, Object>> ordersInTransaction) throws SQLException
    {
        TableInfo ti = getTableInfo("study", "observation_order");
        SimpleFilter filter = new SimpleFilter(FieldKey.fromParts("category","value"), "Activity");
        filter.addCondition(FieldKey.fromString("caseid"), caseid);
        filter.addCondition(FieldKey.fromParts("frequency"), "SID");
        TableSelector ts = new TableSelector(ti, PageFlowUtil.set("category","frequency"), filter, null);

        List<String> missing = new ArrayList<>(NIRC_EHRManager.DAILY_CLINICAL_OBS);
        ts.forEach(row -> {
            if (row.getString("category") != null)
                missing.remove(row.getString("category"));
        });

        ordersInTransaction.forEach(row -> {
            if (row.get("category") != null && row.get("frequency") != null && row.get("frequency").equals("SID"))
                missing.remove((String)row.get("category"));
        });

        if (!missing.isEmpty())
        {
            try
            {
                List<Map<String, Object>> rows = new ArrayList<>();

                // Get tomorrow's date at 8:00 AM
                LocalDateTime localDateTime = date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                LocalDateTime nextDayAtEight = localDateTime.plusDays(1)
                        .withHour(8)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0);

                Date obsDate = Date.from(nextDayAtEight.atZone(ZoneId.systemDefault()).toInstant());

                for (String category : missing)
                {
                    Map<String, Object> row = new CaseInsensitiveHashMap<>();
                    row.put("category", category);
                    row.put("frequency", "SID");
                    row.put("caseid", caseid);
                    row.put("date", obsDate);
                    row.put("Id", id);
                    row.put("qcstate", qcstate);
                    row.put("area", "N/A");
                    row.put("performedby", performedby);
                    row.put("taskid", taskid);
                    row.put("type", "Clinical"); // TODO: Will need to update for behavior
                    rows.add(row);
                }

                BatchValidationException errors = new BatchValidationException();
                ti.getUpdateService().insertRows(_user, _container, rows, errors, null, getExtraContext());
                if (errors.hasErrors())
                    throw errors;
            }
            catch (Exception e)
            {
                _log.error("Error adding daily clinical observation orders", e);
            }
        }
    }

    // This helper function propagates clinical observations through clinical cases
    public Map<String, Object> handleScheduledObservations(Map<String, Object> row, String qcstate, String orderTasks) throws SQLException, BatchValidationException, QueryUpdateServiceException, DuplicateKeyException
    {
        Date scheduledDate = ConvertHelper.convert(row.get("scheduledDate"), Date.class);
        Date date = ConvertHelper.convert(row.get("date"), Date.class);
        String category = ConvertHelper.convert(row.get("category"), String.class);
        String observation = ConvertHelper.convert(row.get("observation"), String.class);
        String performedBy = ConvertHelper.convert(row.get("performedBy"), String.class);
        String taskid = ConvertHelper.convert(row.get("taskid"), String.class);

        // Get observation orders for these tasks
        TableInfo ti = getTableInfo("study", "observation_order");
        SimpleFilter filter = new SimpleFilter(FieldKey.fromString("taskid"), orderTasks, CompareType.IN);
        filter.addCondition(FieldKey.fromString("category"), category);
        TableSelector ts = new TableSelector(ti, PageFlowUtil.set("category,caseid,Id,area,objectid"), filter, null);

        Map<String, Object>[] orders = ts.getMapArray();
        Map<String, Object> triggerOrder = null;

        if (orders.length > 0)
        {
            for (int i = 0; i < orders.length; i++)
            {
                Map<String, Object> order = orders[i];

                // First order we find will fill out the information in the row passing through the trigger
                if (i == 0)
                {
                    triggerOrder = new HashMap<>();
                    triggerOrder.put("caseid", order.get("caseid"));
                    triggerOrder.put("area", order.get("area"));
                    triggerOrder.put("orderId", order.get("objectid"));
                    continue;
                }

                // If there are multiple treatment orders that match insert the others here
                Map<String, Object> obsRow = new CaseInsensitiveHashMap<>();
                obsRow.put("caseid", order.get("caseid"));
                obsRow.put("category", order.get("category"));
                obsRow.put("date", date);
                obsRow.put("qcstate", qcstate);
                obsRow.put("Id", order.get("Id"));
                obsRow.put("scheduledDate", scheduledDate);
                obsRow.put("area", order.get("area"));
                obsRow.put("observation", observation);
                obsRow.put("performedBy", performedBy);
                obsRow.put("orderId", order.get("objectid"));
                obsRow.put("taskid", order.get("taskid"));

                List<Map<String, Object>> rows = new ArrayList<>();
                rows.add(obsRow);

                BatchValidationException errors = new BatchValidationException();
                TableInfo obsTi = getTableInfo("study", "clinical_observations");
                obsTi.getUpdateService().insertRows(_user, _container, rows, errors, null, getExtraContext());
                if (errors.hasErrors())
                    throw errors;
            }

        }
        return triggerOrder;
    }

    public boolean validateHousing(String id, String cage, Date date)
    {
        if (id == null || cage == null || date == null)
            return true;

        TableInfo ti = getTableInfo("study", "Housing");
        SimpleFilter filter = new SimpleFilter(FieldKey.fromString("Id"), id);
        filter.addCondition(FieldKey.fromString("cage"), cage, CompareType.EQUAL);

        Date updatedDate = ConvertHelper.convert(date, Date.class);
        updatedDate = DateUtils.addMinutes(updatedDate, 1);  // temp fix
        filter.addCondition(FieldKey.fromString("date"), updatedDate, CompareType.LTE);
        filter.addClause(new SimpleFilter.OrClause(new CompareType.EqualsCompareClause(FieldKey.fromString("enddate"), CompareType.GT, date), new CompareType.CompareClause(FieldKey.fromString("enddate"), CompareType.ISBLANK, null)));
        filter.addCondition(FieldKey.fromString("qcstate/publicdata"), true, CompareType.EQUAL);

        TableSelector ts = new TableSelector(ti, Collections.singleton("Id"), filter, null);
        return ts.exists();
    }

    public void reportDataChange(String schema, String query, final List<String> ids)
    {
        EHRDemographicsService.get().reportDataChange(_container, schema, query, ids);
    }

    public Object getFormularyForDrug(String drugCode) throws SQLException
    {
        if (_cachedDrugFormulary.containsKey(drugCode))
            return _cachedDrugFormulary.get(drugCode);

        TableInfo ti = getTableInfo("ehr_lookups", "drug_defaults");
        SimpleFilter filter = new SimpleFilter(FieldKey.fromString("code"), drugCode);
        TableSelector ts = new TableSelector(ti, PageFlowUtil.set("code", "amount_max"), filter, null);
        Map<String, Object> drugFormulary = new HashMap<>();
        try (Results rs = ts.getResults())
        {
            for (Map<String, Object> r : rs)
            {
                drugFormulary.put("code", r.get("code"));
                drugFormulary.put("maxAmount", r.get("amount_max"));
            }
        }

        _cachedDrugFormulary.put(drugCode, drugFormulary);
        return drugFormulary;
    }

    public boolean isTreatmentOrderEntered(String treatmentid, String date)
    {
        TableInfo ti = getTableInfo("study", "drug");
        SimpleFilter filter = new SimpleFilter(FieldKey.fromString("treatmentid"), treatmentid);
        filter.addCondition(FieldKey.fromString("scheduledDate"), ConvertHelper.convert(date, Date.class), CompareType.EQUAL);
        TableSelector ts = new TableSelector(ti, PageFlowUtil.set("objectid"), filter, null);

        return ts.exists();
    }

    public boolean isProcedureOrderEntered(String orderid)
    {
        TableInfo ti = getTableInfo("study", "prc_order");
        SimpleFilter filter = new SimpleFilter(FieldKey.fromString("objectid"), orderid);
        TableSelector ts = new TableSelector(ti, PageFlowUtil.set("qcstate"), filter, null);
        Integer qcstate = ts.getArrayList(Integer.class).get(0);
        if (EHRService.get().getQCStates(_container).get("Completed").getRowId() == qcstate)
            return true;

        return false;
    }

    public void markProcedureOrderComplete(List<String> orderids)
    {
        TableInfo ti = getTableInfo("study", "prc_order");
        List<Map<String, Object>> rows = new ArrayList<>();
        for (String orderid : orderids)
        {
            Map<String, Object> r = new HashMap<>();
            r.put("objectid", orderid);
            r.put("qcstate", EHRService.get().getQCStates(_container).get("Completed").getRowId());
            rows.add(r);
        }

        try
        {
            ti.getUpdateService().updateRows(_user, _container, rows, null, null, getExtraContext());
        }
        catch (Exception e)
        {
            _log.error("Error marking procedure order complete", e);
        }
    }

    public void sendPregnancyOutcomeNotification(final String animalId, Map<String, Object> row) throws Exception
    {
        //check whether Notification is enabled
        if (!NotificationService.get().isActive(new NIRCPregnancyOutcomeNotification(), _container) || !NotificationService.get().isServiceEnabled())
        {
            _log.info("NIRC Pregnancy Outcome notification service is not enabled, will not send notification.");
            return;
        }

        try (DbScope.Transaction transaction = Objects.requireNonNull(StudyService.get()).getDatasetSchema().getScope().ensureTransaction())
        {
            // Add a post commit task to run provider update in another thread once this transaction is complete.
            transaction.addCommitTask(() ->
                    JobRunner.getDefault().execute(() -> {
                        final Container container = _container;
                        final User user = _user;
                        String subject = "Pregnancy Outcome Notification for: " + animalId;

                        // get recipients
                        Set<UserPrincipal> recipients = NotificationService.get().getRecipients(new NIRCPregnancyOutcomeNotification(), container);
                        if (recipients.isEmpty())
                        {
                            _log.warn("No NIRC recipients set for pregnancy outcome notification, skipping notification");
                            return;
                        }
                        //get pregnancy outcome info
                        Date date = ConvertHelper.convert(row.get("date"), Date.class);
                        String result = ConvertHelper.convert(row.get("result"), String.class);
                        String outcome;
                        try
                        {
                            outcome = getPregnancyResultTitle(result);
                        }
                        catch (SQLException e)
                        {
                            throw new RuntimeException("Unable to find the outcome for value '" + result + "'", e);
                        }

                        //construct html for email notification
                        final StringBuilder html = new StringBuilder();
                        html.append("Pregnancy outcome for animal '").append(PageFlowUtil.filter(animalId)).
                                append("' recorded on '").append(_dateFormat.format(date)).append("': ").
                                append(PageFlowUtil.filter(outcome)).append("<br><br>");

                        //append animal details
                        appendAnimalDetails(html, animalId, container);

                        // send Pregnancy Outcome notification
                        _log.debug("NIRC Pregnancy Outcome notification job sending email for animal " + animalId + " in container " + container.getPath());
                        TriggerScriptNotification.sendMessage(subject, html.toString(), recipients, container, user);
                    }), DbScope.CommitTaskOption.POSTCOMMIT);

            transaction.commit();
        }
    }

    public String getPregnancyResultTitle(String val) throws SQLException
    {
        TableInfo ti = getTableInfo("ehr_lookups", "pregnancy_result");
        SimpleFilter filter = new SimpleFilter(FieldKey.fromString("value"), val);
        TableSelector ts = new TableSelector(ti, PageFlowUtil.set("value", "title"), filter, null);
        String title = null;
        try (Results rs = ts.getResults())
        {
            for (Map<String, Object> r : rs)
            {
                String value = ConvertHelper.convert(r.get("value"), String.class);

                if (null != value && value.equals(val))
                {
                    title = ConvertHelper.convert(r.get("title"), String.class);
                }
            }
        }
        return title;
    }
}