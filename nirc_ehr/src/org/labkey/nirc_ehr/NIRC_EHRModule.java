/*
 * Copyright (c) 2021 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.labkey.nirc_ehr;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.labkey.api.data.UpgradeCode;
import org.labkey.api.ehr.EHRService;
import org.labkey.api.ehr.SharedEHRUpgradeCode;
import org.labkey.api.resource.Resource;
import org.labkey.nirc_ehr.demographics.ActiveFlagsDemographicsProvider;
import org.labkey.api.ehr.history.DefaultClinicalRemarksDataSource;
import org.labkey.api.ldk.ExtendedSimpleModule;
import org.labkey.api.module.Module;
import org.labkey.api.module.ModuleContext;
import org.labkey.api.query.DefaultSchema;
import org.labkey.api.query.QuerySchema;
import org.labkey.api.view.WebPartFactory;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.history.DefaultBiopsyDataSource;
import org.labkey.nirc_ehr.history.DefaultBloodDrawDataSource;
import org.labkey.nirc_ehr.query.NIRC_EHRUserSchema;
import org.labkey.nirc_ehr.table.NIRC_EHRCustomizer;

import java.util.Collection;
import java.util.Collections;

public class NIRC_EHRModule extends ExtendedSimpleModule
{
    public static final String NAME = "NIRC_EHR";

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public @Nullable Double getSchemaVersion()
    {
        return 22.008;
    }

    @Override
    public boolean hasScripts()
    {
        return true;
    }

    @Override
    @NotNull
    protected Collection<WebPartFactory> createWebPartFactories()
    {
        return Collections.emptyList();
    }

    @Override
    protected void init()
    {
        addController(NIRC_EHRController.NAME, NIRC_EHRController.class);

        EHRService ehrService = EHRService.get();
        ehrService.registerClientDependency(ClientDependency.supplierFromPath("nirc_ehr/nircReports.js"), this);
        ehrService.registerClientDependency(ClientDependency.supplierFromPath("nirc_ehr/panel/SnapshotPanel.js"), this);
        ehrService.registerClientDependency(ClientDependency.supplierFromPath("nirc_ehr/panel/BloodSummaryPanel.js"), this);
    }

    @Override
    protected void doStartupAfterSpringConfig(ModuleContext moduleContext)
    {
        EHRService ehrService = EHRService.get();
        ehrService.registerModule(this);

        Resource r = getModuleResource("/scripts/nirc_triggers.js");
        assert r != null;
        EHRService.get().registerTriggerScript(this, r);

        EHRService.get().registerTableCustomizer(this, NIRC_EHRCustomizer.class);

        EHRService.get().registerDemographicsProvider(new ActiveFlagsDemographicsProvider(this));

        EHRService.get().registerHistoryDataSource(new DefaultClinicalRemarksDataSource(this));
        EHRService.get().registerHistoryDataSource(new DefaultBiopsyDataSource(this));
        EHRService.get().registerHistoryDataSource(new DefaultBloodDrawDataSource(this));

        ehrService.registerActionOverride("animalHistory", this, "views/animalHistory.html");
        ehrService.registerActionOverride("participantView", this, "views/participantView.html");
    }

    @Override
    protected void registerSchemas()
    {
        DefaultSchema.registerProvider(NIRC_EHRSchema.NAME, new DefaultSchema.SchemaProvider(this)
        {
            @Override
            public @Nullable QuerySchema createSchema(DefaultSchema schema, Module module)
            {
                return new NIRC_EHRUserSchema(NIRC_EHRSchema.NAME, null, schema.getUser(), schema.getContainer(), NIRC_EHRSchema.get_instance().getSchema());
            }
        });
    }

    @Override
    public @NotNull Collection<String> getSchemaNames()
    {
        return Collections.singleton(NIRC_EHRSchema.NAME);
    }

    @Override
    public @NotNull UpgradeCode getUpgradeCode()
    {
        return SharedEHRUpgradeCode.getInstance(this);
    }
}