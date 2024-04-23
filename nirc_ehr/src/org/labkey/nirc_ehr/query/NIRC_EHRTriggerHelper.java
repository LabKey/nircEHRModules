package org.labkey.nirc_ehr.query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.labkey.api.collections.CaseInsensitiveHashMap;
import org.labkey.api.data.CompareType;
import org.labkey.api.data.Container;
import org.labkey.api.data.ContainerManager;
import org.labkey.api.data.ConvertHelper;
import org.labkey.api.data.SimpleFilter;
import org.labkey.api.data.Sort;
import org.labkey.api.data.TableInfo;
import org.labkey.api.data.TableSelector;
import org.labkey.api.ehr.EHRDemographicsService;
import org.labkey.api.ehr.demographics.AnimalRecord;
import org.labkey.api.ehr.notification.TriggerScriptNotification;
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
import org.labkey.api.util.GUID;
import org.labkey.api.util.PageFlowUtil;
import org.labkey.nirc_ehr.DeathNotification;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NIRC_EHRTriggerHelper
{
    private Container _container = null;
    private User _user = null;
    private static final Logger _log = LogManager.getLogger(NIRC_EHRTriggerHelper.class);
    private Integer _nextProjectId = null;
    private Integer _nextProtocolId = null;

    private SimpleDateFormat _dateFormat;

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

    public String createBirthHousingRecord(String id, Map<String, Object> row) throws QueryUpdateServiceException, DuplicateKeyException, SQLException, BatchValidationException, InvalidKeyException
    {
        BatchValidationException errors = new BatchValidationException();
        Date date = ConvertHelper.convert(row.get("date"), Date.class);
        String room = ConvertHelper.convert(row.get("room"), String.class);
        if (id == null || date == null || room == null)
            return "Attempting to create a housing record with no id, date, or room";

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
        if (taskId == null) {
            return "Attempting to create a birth record with no taskid";
        }

        // If updating an existing birth record with housing info, check if the housing record should be closed
        if (enddate == null)
        {
            SimpleFilter nextFilter = new SimpleFilter(FieldKey.fromString("Id"), id);
            nextFilter.addCondition(FieldKey.fromString("date"), date, CompareType.DATE_GTE);
            nextFilter.addCondition(FieldKey.fromString("taskid"), taskId, CompareType.NEQ); // Don't include the current record
            TableSelector ts = new TableSelector(ti, PageFlowUtil.set("date"), nextFilter, new Sort("date"));
            List<Date> dates = ts.getArrayList(Date.class);
            if (dates.size() > 0)
            {
                enddate = dates.get(0);
            }
        }

        String qcstate = ConvertHelper.convert(row.get("qcstate"), String.class);
        if (qcstate == null) {
            return "Attempting to create a birth record with no qcstate";
        }

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
        saveRow.put("room", room);
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

        String cond = ConvertHelper.convert(row.get("cond"), String.class);
        if (cond != null)
            saveRow.put("cond", cond);

        if (enddate != null)
            saveRow.put("enddate", enddate);

        String cage = ConvertHelper.convert(row.get("cage"), String.class);
        if (cage != null)
            saveRow.put("cage", cage);

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

    public String deleteDatasetRecord(String dataset, String taskid) throws SQLException, BatchValidationException, QueryUpdateServiceException, InvalidKeyException
    {
        if (dataset == null || taskid == null)
        {
             return "Failed deleting record. Incomplete information.";
        }

        TableInfo ti = getTableInfo("study", dataset);
        if (ti == null)
        {
            return "Failed deleting record. Table not found: study." + dataset;
        }

        List<Map<String, Object>> lsids = Arrays.asList(new TableSelector(ti, Collections.singleton("lsid"), new SimpleFilter(FieldKey.fromString("taskid"), taskid), null).getMapArray());

        if (ti.getUpdateService() != null && lsids.size() > 0)
        {
            ti.getUpdateService().deleteRows(_user, _container, lsids, null, null);
        }

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

    public void sendDeathNotification(final String animalId, final Date recordedDeathDate, final String taskid) throws Exception
    {
        //check whether Death Notification is enabled
        if (!NotificationService.get().isActive(new DeathNotification(), _container) || !NotificationService.get().isServiceEnabled())
        {
            _log.info("Death notification service is not enabled, will not send death notification.");
            return;
        }

        // get recipients
        Set<UserPrincipal> recipients = NotificationService.get().getRecipients(new DeathNotification(), _container);
        if (recipients.size() == 0)
        {
            _log.warn("No recipients set, skipping death notification");
            return;
        }

        String subject = "Death Notification: " + animalId;

        final StringBuilder html = new StringBuilder();
        html.append("<p>Animal ").append(PageFlowUtil.filter(animalId)).append(" has been marked as dead on '").append(_dateFormat.format(recordedDeathDate)).append("'.<p>");
        appendAnimalDetails(html, animalId, _container);

        String url = AppProps.getInstance().getBaseServerUrl() + AppProps.getInstance().getContextPath() + "/ehr" + _container.getPath() + "/dataEntryForm.view?formType=Necropsy&taskid=" + taskid;
        html.append("<a href='").append(PageFlowUtil.filter(url)).append("'>");
        html.append("Click here to record Necropsy</a>.  <p>");

        // send Death Notification
        _log.debug("NIRC Death notification job sending email for animal " + animalId + " in container " + _container.getPath());
        TriggerScriptNotification.sendMessage(subject, html.toString(), recipients, _container, _user);
    }

    private void appendAnimalDetails(StringBuilder html, String id, Container container)
    {
        String url = AppProps.getInstance().getBaseServerUrl() + AppProps.getInstance().getContextPath() + "/ehr" + _container.getPath() + "/participantView.view?participantId=" + id;
        html.append("<a href='").append(url).append("'>");
        html.append("Click here to view this animal's clinical details</a>.  <p>");

        AnimalRecord ar = EHRDemographicsService.get().getAnimal(container, id);
        html.append("Species: ").append(PageFlowUtil.filter(ar.getSpecies())).append("<br>");
        html.append("Sex: ").append(PageFlowUtil.filter(ar.getGenderMeaning())).append("<br>");
        html.append("Date of birth: ").append(PageFlowUtil.filter(null != ar.getBirth() ? _dateFormat.format(ar.getBirth()) : null)).append("<br>");
        html.append("Age: ").append(PageFlowUtil.filter(ar.getAgeInYearsAndDays())).append("<br>");
        html.append("Dam: ").append(PageFlowUtil.filter(ar.getDam())).append("<br>");
        html.append("Sire: ").append(PageFlowUtil.filter(ar.getSire())).append("<br>");
        if (ar.getActiveHousing() != null && !ar.getActiveHousing().isEmpty())
        {
//            ar.getActiveHousing().forEach(h -> {
//                html.append("Housing Location: ").append(PageFlowUtil.filter(h.get("room")));
//                if (h.get("cage/enclosureId") != null)
//                {
//                    html.append(", ").append(PageFlowUtil.filter(h.get("cage/enclosureId")));
//                }
//                html.append("<br>");
//            });
        }
    }
}