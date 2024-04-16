package org.labkey.nirc_ehr.table;

import org.labkey.api.data.AbstractTableInfo;
import org.labkey.api.data.ColumnInfo;
import org.labkey.api.data.Container;
import org.labkey.api.data.HtmlDisplayColumnFactory;
import org.labkey.api.data.JdbcType;
import org.labkey.api.data.MutableColumnInfo;
import org.labkey.api.data.SQLFragment;
import org.labkey.api.data.TableInfo;
import org.labkey.api.data.WrappedColumn;
import org.labkey.api.ehr.EHRService;
import org.labkey.api.exp.api.StorageProvisioner;
import org.labkey.api.exp.property.Domain;
import org.labkey.api.gwt.client.FacetingBehaviorType;
import org.labkey.api.ldk.table.AbstractTableCustomizer;
import org.labkey.api.query.AliasedColumn;
import org.labkey.api.query.DetailsURL;
import org.labkey.api.query.ExprColumn;
import org.labkey.api.query.FilteredTable;
import org.labkey.api.query.QueryForeignKey;
import org.labkey.api.query.UserIdQueryForeignKey;
import org.labkey.api.query.UserSchema;
import org.labkey.api.study.DatasetTable;
import org.labkey.api.util.StringExpressionFactory;

import java.util.Calendar;

public class NIRC_EHRCustomizer extends AbstractTableCustomizer
{
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
        if (table instanceof AbstractTableInfo ti)
        {
            doSharedCustomization(ti);
            doTableSpecificCustomizations(ti);
            if (ti instanceof DatasetTable)
            {
                ti.addTriggerFactory(new NIRC_EHRTriggerScriptFactory());
            }
        }
    }

    public void doTableSpecificCustomizations(AbstractTableInfo ti)
    {
        if (matches(ti, "study", "housing"))
        {
            customizeHousingTable(ti);
        }
        if (matches(ti, "study", "Animal"))
        {
            customizeAnimalTable(ti);
        }
        if (matches(ti, "ehr", "protocol"))
        {
            customizeProtocolTable(ti);
        }
    }

    public void doSharedCustomization(AbstractTableInfo ti)
    {
        for (var col : ti.getMutableColumns())
        {
            if ("performedby".equalsIgnoreCase(col.getName()) && null == col.getFk())
            {
                col.setLabel("Performed By");
                col.setFk(new UserIdQueryForeignKey(ti.getUserSchema(), true));
            }
            if ("species".equalsIgnoreCase(col.getName()) && null == col.getFk())
            {
                UserSchema us = getEHRUserSchema(ti, "ehr_lookups");
                col.setLabel("Species");
                col.setFk(new QueryForeignKey(ti.getUserSchema(), ti.getContainerFilter(), us, null, "species_codes", "code", "scientific_name"));
            }
            if ("protocol".equalsIgnoreCase(col.getName()) && null == col.getFk() && !"protocol".equalsIgnoreCase(ti.getName()))
            {
                UserSchema us = getEHRUserSchema(ti, "ehr");
                col.setLabel("Protocol");
                col.setFk(new QueryForeignKey(ti.getUserSchema(), ti.getContainerFilter(), us, null, "protocol", "protocol", "title"));
            }
            if ("gender".equalsIgnoreCase(col.getName()) && null == col.getFk())
            {
                UserSchema us = getEHRUserSchema(ti, "ehr_lookups");
                col.setLabel("Gender");
                col.setFk(new QueryForeignKey(ti.getUserSchema(), ti.getContainerFilter(), us, null, "gender_codes", "code", "meaning"));
            }
            if ("cage".equalsIgnoreCase(col.getName()) && !ti.getName().equalsIgnoreCase("cage"))
            {
                UserSchema us = getEHRUserSchema(ti, "ehr_lookups");
                col.setLabel("Cage");
                col.setFk(new QueryForeignKey(ti.getUserSchema(), ti.getContainerFilter(), us, null, "cage", "location", "cage"));
                col.setURL(StringExpressionFactory.createURL("/nirc_ehr/cageDetails.view?room=${cage/room}&cage=${cage}"));
                col.setFacetingBehaviorType(FacetingBehaviorType.AUTOMATIC);
            }
            if ("room".equalsIgnoreCase(col.getName()) && !ti.getName().equalsIgnoreCase("rooms"))
            {
                UserSchema us = getEHRUserSchema(ti, "ehr_lookups");
                col.setLabel("Room");
                col.setFk(new QueryForeignKey(ti.getUserSchema(), ti.getContainerFilter(), us, null, "rooms", "room", "name"));
                col.setURL(StringExpressionFactory.createURL("/nirc_ehr/cageDetails.view?room=${room}"));
            }
            if ("building".equalsIgnoreCase(col.getName()) && !ti.getName().equalsIgnoreCase("buildings"))
            {
                UserSchema us = getEHRUserSchema(ti, "ehr_lookups");
                col.setLabel("Building");
                col.setFk(new QueryForeignKey(ti.getUserSchema(), ti.getContainerFilter(), us, null, "buildings", "name", "description"));
            }
            if ("floor".equalsIgnoreCase(col.getName()) && !ti.getName().equalsIgnoreCase("floors"))
            {
                UserSchema us = getEHRUserSchema(ti, "ehr_lookups");
                col.setLabel("Floor");
                col.setFk(new QueryForeignKey(ti.getUserSchema(), ti.getContainerFilter(), us, null, "floors", "floor", "name"));
            }
            if ("area".equalsIgnoreCase(col.getName()) && !ti.getName().equalsIgnoreCase("areas"))
            {
                UserSchema us = getEHRUserSchema(ti, "ehr_lookups");
                col.setLabel("Area");
                col.setFk(new QueryForeignKey(ti.getUserSchema(), ti.getContainerFilter(), us, null, "areas", "area", "description"));
            }
            if ("remark".equalsIgnoreCase(col.getName()) && null == col.getFk())
            {
                col.setLabel("Remark");
            }
            if ("description".equalsIgnoreCase(col.getName()) && null == col.getFk())
            {
                col.setLabel("Description");
            }
            if ("units".equalsIgnoreCase(col.getName()) && null == col.getFk())
            {
                UserSchema us = getEHRUserSchema(ti, "ehr_lookups");
                col.setLabel("Units");
                col.setFk(new QueryForeignKey(ti.getUserSchema(), ti.getContainerFilter(), us, null, "numeric_unit", "value", "title"));
            }
        }
    }

    private void ensureSortColumn(AbstractTableInfo ti, ColumnInfo baseColumn)
    {
        String sortName = baseColumn.getName() + "_sortValue";
        if (ti.getColumn(sortName, false) == null)
        {
            AliasedColumn sortCol = new AliasedColumn(ti, sortName, baseColumn);
            sortCol.setKeyField(false);
            sortCol.setHidden(true);
            sortCol.setCalculated(true);
            sortCol.setUserEditable(false);
            sortCol.setNullable(true);
            sortCol.setPropertyURI(sortCol.getPropertyURI() + "_sortValue");
            sortCol.setRequired(false);
            sortCol.setShownInDetailsView(false);
            sortCol.setShownInInsertView(false);
            sortCol.setShownInUpdateView(false);
            sortCol.setLabel(baseColumn.getLabel() + " - Sort Field");
            ti.addColumn(sortCol);
        }
    }

    private void customizeHousingTable(AbstractTableInfo ti)
    {
        if (ti.getColumn("room") == null && ti.getColumn("cage") != null)
        {
            UserSchema us = getUserSchema(ti, "ehr_lookups");
            if (us != null)
            {
                SQLFragment roomSql = new SQLFragment("(SELECT room FROM ehr_lookups.cage WHERE location = " + ExprColumn.STR_TABLE_ALIAS + ".cage)");
                ExprColumn roomCol = new ExprColumn(ti, "room", roomSql, JdbcType.VARCHAR, ti.getColumn("cage"));
                ti.addColumn(roomCol);
            }

            ensureSortColumn(ti, ti.getColumn("room"));
        }
        if (ti.getColumn("daysInRoom") == null)
        {
            TableInfo realTable = getRealTable(ti);
            if (realTable != null && realTable.getColumn("participantid") != null && realTable.getColumn("date") != null && realTable.getColumn("enddate") != null)
            {
                SQLFragment roomSql = new SQLFragment(realTable.getSqlDialect().getDateDiff(Calendar.DATE, "{fn curdate()}", "(SELECT max(h2.enddate) as d FROM " + realTable.getSelectName() + " h2 WHERE h2.enddate IS NOT NULL AND h2.enddate <= " + ExprColumn.STR_TABLE_ALIAS + ".date AND h2.participantid = " + ExprColumn.STR_TABLE_ALIAS + ".participantid and h2.room != " + ExprColumn.STR_TABLE_ALIAS + ".room)"));
                ExprColumn roomCol = new ExprColumn(ti, "daysInRoom", roomSql, JdbcType.INTEGER, realTable.getColumn("participantid"), realTable.getColumn("date"), realTable.getColumn("enddate"));
                roomCol.setLabel("Days In Room");
                ti.addColumn(roomCol);

            }
        }

        if (ti.getColumn("previousLocation") == null)
        {
            UserSchema us = getUserSchema(ti, "study");
            if (us != null)
            {
                ColumnInfo lsidCol = ti.getColumn("lsid");
                var col = ti.addColumn(new WrappedColumn(lsidCol, "previousLocation"));
                col.setLabel("Previous Location");
                col.setUserEditable(false);
                col.setIsUnselectable(true);
                col.setFk(new QueryForeignKey(QueryForeignKey.from(us, ti.getContainerFilter())
                        .table("housingPreviousLocation")
                        .key("lsid")
                        .display("location")));
            }
        }
    }

    private void customizeAnimalTable(AbstractTableInfo ds)
    {
        UserSchema us = getUserSchema(ds, "study");

        if (us == null)
        {
            return;
        }

        if (ds.getColumn("parents") == null)
        {
            var col = getWrappedCol(us, ds, "parents", "demographicsParents", "Id", "Id");
            col.setLabel("Parents");
            ds.addColumn(col);
        }
        if (ds.getColumn("totalOffspring") == null)
        {
            var col15 = getWrappedCol(us, ds, "totalOffspring", "demographicsTotalOffspring", "Id", "Id");
            col15.setLabel("Number of Offspring");
            col15.setDescription("Shows the total offspring of each animal");
            ds.addColumn(col15);
        }
        if (ds.getColumn("flags") == null)
        {
            var col = getWrappedCol(us, ds, "Flags", "demographicsActiveFlags", "Id", "Id");
            col.setLabel("Active Flags");
            col.setURL(DetailsURL.fromString("/query/executeQuery.view?schemaName=ehr_lookups&queryName=flag_values&query.Id~eq=${Id}", ds.getContainerContext()));
            ds.addColumn(col);
        }
        if (ds.getColumn("demographicsActiveAssignment") == null)
        {
            var col21 = getWrappedCol(us, ds, "activeAssignments", "demographicsActiveAssignment", "Id", "Id");
            col21.setLabel("Active Protocol Assignments");
            col21.setDescription("Shows all protocols to which the animal is actively assigned on the current date");
            ds.addColumn(col21);
        }
        if (ds.getColumn("alias") == null)
        {
            var col = getWrappedCol(us, ds, "alias", "demographicsAliases", "Id", "Id");
            col.setLabel("Alias");
            ds.addColumn(col);
        }
    }

    private void customizeProtocolTable(AbstractTableInfo ti)
    {
        var descriptionCol = ti.getMutableColumn("description");
        if (null != descriptionCol)
            descriptionCol.setDisplayColumnFactory(new HtmlDisplayColumnFactory());
    }

    private MutableColumnInfo getWrappedCol(UserSchema us, AbstractTableInfo ds, String name, String queryName, String colName, String targetCol)
    {

        WrappedColumn col = new WrappedColumn(ds.getColumn(colName), name);
        col.setReadOnly(true);
        col.setIsUnselectable(true);
        col.setUserEditable(false);
        col.setFk(new QueryForeignKey(QueryForeignKey.from(us, ds.getContainerFilter())
                .table(queryName)
                .key(targetCol)
                .display(targetCol)));

        return col;
    }

    private TableInfo getRealTable(TableInfo targetTable)
    {
        TableInfo realTable = null;
        if (targetTable instanceof FilteredTable)
        {
            if (targetTable instanceof DatasetTable)
            {
                Domain domain = targetTable.getDomain();
                if (domain != null)
                {
                    realTable = StorageProvisioner.createTableInfo(domain);
                }
            }
            else if (targetTable.getSchema() != null)
            {
                realTable = targetTable.getSchema().getTable(targetTable.getName());
            }
        }
        return realTable;
    }
}
