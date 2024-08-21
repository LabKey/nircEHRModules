package org.labkey.nirc_ehr.table;

import org.labkey.api.data.AbstractTableInfo;
import org.labkey.api.data.BaseColumnInfo;
import org.labkey.api.data.ColumnInfo;
import org.labkey.api.data.Container;
import org.labkey.api.data.DataColumn;
import org.labkey.api.data.DisplayColumn;
import org.labkey.api.data.DisplayColumnFactory;
import org.labkey.api.data.HtmlDisplayColumnFactory;
import org.labkey.api.data.JdbcType;
import org.labkey.api.data.MutableColumnInfo;
import org.labkey.api.data.RenderContext;
import org.labkey.api.data.SQLFragment;
import org.labkey.api.data.TableInfo;
import org.labkey.api.data.WrappedColumn;
import org.labkey.api.ehr.EHRService;
import org.labkey.api.ehr.security.EHRBehaviorEntryPermission;
import org.labkey.api.ehr.security.EHRClinicalEntryPermission;
import org.labkey.api.ehr.security.EHRDataEntryPermission;
import org.labkey.api.ehr.security.EHRVeterinarianPermission;
import org.labkey.api.ehr.table.FixedWidthDisplayColumn;
import org.labkey.api.exp.api.StorageProvisioner;
import org.labkey.api.exp.property.Domain;
import org.labkey.api.gwt.client.FacetingBehaviorType;
import org.labkey.api.ldk.table.AbstractTableCustomizer;
import org.labkey.api.query.AliasedColumn;
import org.labkey.api.query.DetailsURL;
import org.labkey.api.query.ExprColumn;
import org.labkey.api.query.FieldKey;
import org.labkey.api.query.FilteredTable;
import org.labkey.api.query.QueryForeignKey;
import org.labkey.api.query.UserSchema;
import org.labkey.api.study.Dataset;
import org.labkey.api.study.DatasetTable;
import org.labkey.api.study.StudyService;
import org.labkey.api.util.PageFlowUtil;
import org.labkey.api.util.StringExpressionFactory;
import org.labkey.api.view.ActionURL;

import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Set;

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

            if (matches(ti, "ehr", "tasks") || matches(table, "ehr", "my_tasks"))
            {
                customizeTasks(ti);
            }

            if (matches(ti, "ehr_lookups", "rooms"))
            {
                customizeRooms(ti);
            }

            if (matches(ti, "ehr_lookups", "floors"))
            {
                customizeFloors(ti);
            }

            if (matches(ti, "nirc_ehr", "necropsyTasks"))
            {
                addNecropsyReportLink(ti);
                addNecropsyEnterDataLink(ti);
            }
            else if (matches(ti, "study", "necropsy") ||
                    matches(ti, "study", "grossPathology") ||
                    matches(ti, "study", "tissueDisposition"))
            {
                addNecropsyReportLink(ti);
            }
            else if (matches(ti, "study", "cases"))
            {
                customizeCases(ti);
            }
            else if (matches(ti, "study", "demographics"))
            {
                customizeDemographics(ti);
            }
            else if (matches(table, "study", "clinical_observations"))
            {
                customizeClinicalObservations((AbstractTableInfo) table);
            }
        }
    }

    private void customizeClinicalObservations(AbstractTableInfo ti)
    {
        var categoryCol = ti.getMutableColumn("category");
        if (categoryCol != null)
        {
            UserSchema us = getUserSchema(ti, "ehr");
            if (us != null)
            {
                categoryCol.setFk(QueryForeignKey
                        .from(ti.getUserSchema(), ti.getContainerFilter())
                        .schema(us)
                        .to("observation_types", "value", "value")
                        .raw(true));
            }
        }
    }

    private void customizeDemographics(AbstractTableInfo ti)
    {
        String hxName = "mostRecentHx";
        if (null == ti.getColumn(hxName) && null != ti.getColumn("Id"))
        {
            ColumnInfo idCol = ti.getColumn("Id");
            assert idCol != null;

            int datasetId = StudyService.get().getDatasetIdByName(ti.getUserSchema().getContainer(), "clinRemarks");
            Dataset dataset = StudyService.get().getDataset(ti.getUserSchema().getContainer(), datasetId);
            String realTableName = dataset.getDomain().getStorageTableName();

            SQLFragment sql = new SQLFragment("(SELECT " + ti.getSqlDialect().getGroupConcat(new SQLFragment("r.hx"), true, false, new SQLFragment(getChr(ti) + "(10)")).getSqlCharSequence() + " FROM studydataset." + realTableName +
                    " r WHERE r.participantId = " + ExprColumn.STR_TABLE_ALIAS + ".participantId AND r.hx IS NOT NULL AND r.date = (SELECT max(date) as expr FROM studydataset." + realTableName + " r2 "
                    + " WHERE r2.participantId = r.participantId AND r2.hx is not null))"
            );
            ExprColumn latestHx = new ExprColumn(ti, hxName, sql, JdbcType.VARCHAR, idCol);
            latestHx.setLabel("Most Recent Hx");
            latestHx.setDisplayColumnFactory(new DisplayColumnFactory()
            {
                @Override
                public DisplayColumn createRenderer(ColumnInfo colInfo)
                {
                    return new FixedWidthDisplayColumn(colInfo, 100);
                }
            });

            latestHx.setWidth("200");
            ti.addColumn(latestHx);
        }

        if (ti.getColumn("mostRecentClinicalObservations") == null)
        {
            UserSchema us = getUserSchema(ti, "study");
            var col = getWrappedCol(us, ti, "mostRecentClinicalObservations", "mostRecentObservationsClinical", "Id", "Id");
            col.setLabel("Most Recent Clinical Observations");
            col.setDescription("Displays the most recent set of clinical observations for this animal");
            col.setDisplayWidth("150");
            ti.addColumn(col);
        }
    }

    private void customizeCases(AbstractTableInfo ti)
    {
        if (ti.getUserSchema().getContainer().hasPermission(ti.getUserSchema().getUser(), EHRClinicalEntryPermission.class))
        {
            appendCaseCheckCol(ti, "caseCheck", "Case Update", "Case Update");
        }
    }

    private void appendCaseCheckCol(AbstractTableInfo ti, String name, String linkLabel, String colLabel)
    {
        if (ti.getColumn(name) != null)
            return;

        var ci = new WrappedColumn(ti.getColumn("Id"), name);
        ci.setDisplayColumnFactory(new DisplayColumnFactory()
        {
            @Override
            public DisplayColumn createRenderer(final ColumnInfo colInfo)
            {
                return new DataColumn(colInfo){

                    @Override
                    public void renderGridCellContents(RenderContext ctx, Writer out) throws IOException
                    {
                        String category = (String)ctx.get("category");
                        ActionURL linkAction = new ActionURL("ehr", "dataEntryForm", ti.getUserSchema().getContainer());
                        if (category == null || category.equals("Clinical"))
                        {
                            if (!ti.getUserSchema().getContainer().hasPermission(ti.getUserSchema().getUser(), EHRClinicalEntryPermission.class))
                                return;

                            linkAction.addParameter("formType", "Clinical Cases");
                        }

                        String caseId = (String)ctx.get("objectid");
                        linkAction.addParameter("caseid", caseId);
                        String href = linkAction.toString();
                        out.write(PageFlowUtil.link(linkLabel).href(href).target("_blank").toString());
                    }

                    @Override
                    public void addQueryFieldKeys(Set<FieldKey> keys)
                    {
                        super.addQueryFieldKeys(keys);
                        keys.add(FieldKey.fromString("objectid"));
                        keys.add(FieldKey.fromString("category"));
                    }

                    @Override
                    public boolean isSortable()
                    {
                        return false;
                    }

                    @Override
                    public boolean isFilterable()
                    {
                        return false;
                    }

                    @Override
                    public boolean isEditable()
                    {
                        return false;
                    }
                };
            }
        });
        ci.setIsUnselectable(false);
        ci.setLabel(colLabel);
        ci.setWidth("50px");

        ti.addColumn(ci);
    }

    private void addNecropsyEnterDataLink(AbstractTableInfo ti)
    {
        if (ti.getColumn("enterNecropsyData") == null)
        {
            WrappedColumn enterNecropsy = new WrappedColumn(ti.getColumn("taskid"), "enterNecropsyData");
            enterNecropsy.setLabel("Enter Data");
            enterNecropsy.setDisplayColumnFactory(new DisplayColumnFactory()
            {
                @Override
                public DisplayColumn createRenderer(final ColumnInfo colInfo)
                {
                    return new DataColumn(colInfo){

                        @Override
                        public void renderGridCellContents(RenderContext ctx, Writer out) throws IOException
                        {
                            if (!ti.getUserSchema().getContainer().hasPermission(ti.getUserSchema().getUser(), EHRVeterinarianPermission.class))
                                return;

                            String taskid = (String)getBoundColumn().getValue(ctx);

                            ActionURL linkAction = new ActionURL("ehr", "dataEntryForm", ti.getUserSchema().getContainer());
                            linkAction.addParameter("formType", "Necropsy");
                            linkAction.addParameter("taskid", taskid);

                            ActionURL returnUrl = new ActionURL("query", "executeQuery", ti.getUserSchema().getContainer());
                            returnUrl.addParameter("schemaName", "nirc_ehr");
                            returnUrl.addParameter("queryName", "necropsyTasks");
                            linkAction.addParameter("returnUrl", returnUrl.toString());

                            String href = linkAction.toString();
                            out.write(PageFlowUtil.link("Enter/Update Necropsy").href(href).target("_blank").toString());

                        }

                        @Override
                        public void addQueryFieldKeys(Set<FieldKey> keys)
                        {
                            super.addQueryFieldKeys(keys);
                            keys.add(getBoundColumn().getFieldKey());
                        }

                        @Override
                        public boolean isSortable()
                        {
                            return false;
                        }

                        @Override
                        public boolean isFilterable()
                        {
                            return false;
                        }

                        @Override
                        public boolean isEditable()
                        {
                            return false;
                        }
                    };
                }
            });
            ti.addColumn(enterNecropsy);
        }
    }

    private void addNecropsyReportLink(AbstractTableInfo ti)
    {
        if (ti.getColumn("viewNecropsy") == null && ti.getColumn("taskid") != null)
        {
            WrappedColumn viewNecropsy = new WrappedColumn(ti.getColumn("taskid"), "viewNecropsy");
            viewNecropsy.setLabel("Report");
            viewNecropsy.setDisplayColumnFactory(new DisplayColumnFactory()
            {
                @Override
                public DisplayColumn createRenderer(final ColumnInfo colInfo)
                {
                    return new DataColumn(colInfo){

                        @Override
                        public void renderGridCellContents(RenderContext ctx, Writer out) throws IOException
                        {
                            String taskid = (String)getBoundColumn().getValue(ctx);

                            ActionURL linkAction = new ActionURL("ehr", "dataEntryFormDetails", ti.getUserSchema().getContainer());
                            linkAction.addParameter("formType", "Necropsy");
                            linkAction.addParameter("taskid", taskid);

                            String href = linkAction.toString();
                            out.write(PageFlowUtil.link("View Report").href(href).target("_blank").toString());
                        }

                        @Override
                        public void addQueryFieldKeys(Set<FieldKey> keys)
                        {
                            super.addQueryFieldKeys(keys);
                            keys.add(getBoundColumn().getFieldKey());
                        }

                        @Override
                        public boolean isSortable()
                        {
                            return false;
                        }

                        @Override
                        public boolean isFilterable()
                        {
                            return false;
                        }

                        @Override
                        public boolean isEditable()
                        {
                            return false;
                        }
                    };
                }
            });
            ti.addColumn(viewNecropsy);
        }
    }

    private void customizeRooms(AbstractTableInfo ti)
    {
        ColumnInfo roomCol = ti.getColumn("name");
        ColumnInfo floorCol = ti.getColumn("floor");
        if (roomCol != null && floorCol != null && ti.getColumn("fullRoom") == null)
        {
            ExprColumn col = new ExprColumn(ti, new FieldKey(null, "fullRoom"), new SQLFragment("##ERROR"), JdbcType.VARCHAR, roomCol, floorCol) {
                @Override
                public SQLFragment getValueSql(String tableAlias)
                {
                    // Need to subclass and override this function due to issue using ExprColumn.STR_TABLE_ALIAS with extensible columns
                    SQLFragment sql = new SQLFragment("(SELECT COALESCE(r.room, 'Room N/A') || ', ' || COALESCE(r.floor, 'Floor N/A') || ', ' || COALESCE(r.building, 'Building N/A') \n" +
                            "        FROM (\n" +
                            "            SELECT \n").append(roomCol.getValueSql(tableAlias));
                    sql.append(" AS room,\n" +
                            "    ff.name as floor,\n" +
                            "    bb.description as building\n" +
                            "    FROM ehr_lookups.floors ff \n" +
                            "    JOIN ehr_lookups.buildings bb ON ff.building = bb.name\n" +
                            "    WHERE ff.floor =\n").append(floorCol.getValueSql(tableAlias));
                    sql.append("        ) r\n" +
                            "    )");

                    return sql;
                }
            };
            col.setName("fullRoom");
            col.setLabel("Full Room");
            ti.addColumn(col);
        }
    }

    private void customizeFloors(AbstractTableInfo ti)
    {
        ColumnInfo floorCol = ti.getColumn("name");
        ColumnInfo bldgCol = ti.getColumn("building");
        if (floorCol != null && bldgCol != null && ti.getColumn("fullFloor") == null)
        {
            SQLFragment sql = new SQLFragment("(SELECT COALESCE(r.floor, 'Floor N/A') || ', ' || COALESCE(r.building, 'Building N/A') \n" +
                    "        FROM (\n" +
                    "            SELECT " + ExprColumn.STR_TABLE_ALIAS + ".name\n");
            sql.append(" AS floor,\n" +
                    "    bb.description as building\n" +
                    "    FROM ehr_lookups.buildings bb \n" +
                    "    WHERE bb.name = " + ExprColumn.STR_TABLE_ALIAS + ".building\n");
            sql.append("        ) r\n" +
                    "    )");

            ExprColumn col = new ExprColumn(ti, "fullFloor", sql, JdbcType.VARCHAR, floorCol, bldgCol);
            col.setLabel("Full Floor");
            ti.addColumn(col);
        }
    }

    private void customizeTasks(AbstractTableInfo ti)
    {
        DetailsURL detailsURL = DetailsURL.fromString("/ehr/dataEntryFormDetails.view?formType=${formtype}&taskid=${taskid}");
        ti.setDetailsURL(detailsURL);

        var titleCol = ti.getMutableColumn("title");
        if (titleCol != null)
        {
            titleCol.setURL(detailsURL);
        }

        var rowIdCol = ti.getMutableColumn("rowid");
        if (rowIdCol != null)
        {
            rowIdCol.setURL(detailsURL);
        }

        var updateCol = ti.getMutableColumn("updateTitle");
        if (updateCol == null)
        {
            updateCol = new WrappedColumn(ti.getColumn("title"), "updateTitle");
            ti.addColumn(updateCol);
        }

        var updateTaskId = ti.getMutableColumn("updateTaskId");
        if (updateTaskId == null)
        {
            updateTaskId = new WrappedColumn(ti.getColumn("rowid"), "updateTaskId");
            ti.addColumn(updateTaskId);
        }

        if (ti.getUserSchema().getContainer().hasPermission(ti.getUserSchema().getUser(), EHRDataEntryPermission.class))
        {
            updateCol.setURL(DetailsURL.fromString("/ehr/dataEntryForm.view?formType=${formtype}&taskid=${taskid}"));
            updateTaskId.setURL(DetailsURL.fromString("/ehr/dataEntryForm.view?formType=${formtype}&taskid=${taskid}"));
        }
        else
        {
            updateCol.setURL(detailsURL);
            updateTaskId.setURL(detailsURL);
        }

        updateCol.setLabel("Title");
        updateCol.setHidden(true);

        updateTaskId.setLabel("Task Id");
        updateTaskId.setHidden(true);
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
        if (matches(ti, "study", "protocolAssignment"))
        {
            EHRService.get().addIsActiveCol(ti, false, EHRService.EndingOption.activeAfterMidnightTonight, EHRService.EndingOption.allowSameDay);
        }
        if (matches(ti, "study", "treatment_order"))
        {
            EHRService.get().addIsActiveCol(ti, false, EHRService.EndingOption.activeAfterMidnightTonight, EHRService.EndingOption.allowSameDay);
        }
    }

    public void doSharedCustomization(AbstractTableInfo ti)
    {
        for (var col : ti.getMutableColumns())
        {
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
                col.setFk(new QueryForeignKey(ti.getUserSchema(), ti.getContainerFilter(), us, null, "rooms", "room", "fullRoom"));
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
                col.setFk(new QueryForeignKey(ti.getUserSchema(), ti.getContainerFilter(), us, null, "floors", "floor", "fullFloor"));
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
            if ("performedby".equalsIgnoreCase(col.getName()))
            {
                if (ti.getName().equalsIgnoreCase("treatment_order") || ti.getName().equalsIgnoreCase("drug"))
                {
                    col.setLabel("Ordered By");
                    UserSchema us = getEHRUserSchema(ti, "ehr_lookups");
                    if (us != null)
                    {
                        col.setFk(new QueryForeignKey(QueryForeignKey.from(us, ti.getContainerFilter())
                                .table("veterinarians")
                                .key("UserId")
                                .display("DisplayName")));
                    }
                }
                else
                {
                    col.setLabel("Performed By");

                    UserSchema us = getEHRUserSchema(ti, "core");
                    if (us != null)
                    {
                        col.setFk(new QueryForeignKey(QueryForeignKey.from(us, ti.getContainerFilter())
                                .table("users")
                                .key("UserId")
                                .display("DisplayName")));
                    }
                }
            }
            if ("taskid".equalsIgnoreCase(col.getName()))
            {
                col.setURL(DetailsURL.fromString("/ehr/dataEntryFormDetails.view?formType=${taskid/formtype}&taskId=${taskid}"));
            }
            if ("container".equalsIgnoreCase(col.getName()))
            {
                col.setHidden(true);
            }
            if ("date".equalsIgnoreCase(col.getName()) && !ti.getName().equals("drug"))
            {
                col.setFormat("Date");
            }
            if ("enddate".equalsIgnoreCase(col.getName()) && !ti.getName().equals("encounters"))
            {
                col.setFormat("Date");
            }
            if ("reviewdate".equalsIgnoreCase(col.getName()))
            {
                col.setFormat("Date");
            }
            if ("caseid".equalsIgnoreCase(col.getName()))
            {
                col.setLabel("Case");
                if (col.getFk() == null)
                {
                    UserSchema us = getEHRUserSchema(ti, "study");
                    if (us != null)
                        col.setFk(new QueryForeignKey(us, null, "cases", "objectid", "category"));
                }
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
                SQLFragment roomSql = new SQLFragment(realTable.getSqlDialect().getDateDiff(Calendar.DATE, "{fn curdate()}", "COALESCE((SELECT max(h2.enddate) as d FROM " + realTable.getSelectName() + " h2 LEFT JOIN ehr_lookups.cage cg ON h2.cage = cg.location " +
                        "WHERE h2.enddate IS NOT NULL AND h2.enddate <= " + ExprColumn.STR_TABLE_ALIAS + ".date AND h2.participantid = " + ExprColumn.STR_TABLE_ALIAS + ".participantid AND cg.room != (SELECT room FROM ehr_lookups.cage WHERE location = " + ExprColumn.STR_TABLE_ALIAS + ".cage)), " + ExprColumn.STR_TABLE_ALIAS + ".date)"));
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
