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
import org.labkey.api.ehr.dataentry.DefaultDataEntryFormFactory;
import org.labkey.api.ehr.demographics.ActiveTreatmentsDemographicsProvider;
import org.labkey.api.ehr.demographics.ParentsDemographicsProvider;
import org.labkey.api.ehr.demographics.SourceDemographicsProvider;
import org.labkey.api.ehr.history.DefaultAlopeciaDataSource;
import org.labkey.api.ehr.history.DefaultAnimalRecordFlagDataSource;
import org.labkey.api.ehr.history.DefaultClinicalRemarksDataSource;
import org.labkey.api.ehr.history.DefaultNotesDataSource;
import org.labkey.api.ehr.history.DefaultVitalsDataSource;
import org.labkey.api.ehr.security.EHRDataAdminPermission;
import org.labkey.api.ldk.ExtendedSimpleModule;
import org.labkey.api.ldk.buttons.ShowEditUIButton;
import org.labkey.api.ldk.notification.NotificationService;
import org.labkey.api.module.Module;
import org.labkey.api.module.ModuleContext;
import org.labkey.api.query.DefaultSchema;
import org.labkey.api.query.QuerySchema;
import org.labkey.api.resource.Resource;
import org.labkey.api.security.roles.RoleManager;
import org.labkey.api.util.NetworkDrive;
import org.labkey.api.view.WebPartFactory;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.buttons.MarkTreatmentCompletedButton;
import org.labkey.nirc_ehr.dataentry.form.*;
import org.labkey.nirc_ehr.demographics.ActiveAssignmentsDemographicsProvider;
import org.labkey.nirc_ehr.demographics.ActiveCasesDemographicsProvider;
import org.labkey.nirc_ehr.demographics.ActiveFlagsDemographicsProvider;
import org.labkey.nirc_ehr.demographics.CagematesDemographicsProvider;
import org.labkey.nirc_ehr.demographics.HousingDemographicsProvider;
import org.labkey.nirc_ehr.demographics.ProtocolAssignmentDemographicsProvider;
import org.labkey.nirc_ehr.history.*;
import org.labkey.nirc_ehr.query.NIRC_EHRUserSchema;
import org.labkey.nirc_ehr.security.NIRCEHRVetTechRole;
import org.labkey.nirc_ehr.table.NIRC_EHRCustomizer;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
        return 24.016;
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
        ehrService.registerClientDependency(ClientDependency.supplierFromPath("nirc_ehr/window/NIRCRecentCasesWindow.js"), this);
        ehrService.registerClientDependency(ClientDependency.supplierFromPath("nirc_ehr/window/NIRCRecentRemarksWindow.js"), this);
        ehrService.registerClientDependency(ClientDependency.supplierFromPath("ehr/sharedReports.js"), this);
        ehrService.registerClientDependency(ClientDependency.supplierFromPath("nirc_ehr/panel/SnapshotPanel.js"), this);
        ehrService.registerClientDependency(ClientDependency.supplierFromPath("nirc_ehr/panel/BloodSummaryPanel.js"), this);
        ehrService.registerClientDependency(ClientDependency.supplierFromPath("nirc_ehr/panel/AnimalDetailsPanel.js"), this);
        ehrService.registerClientDependency(ClientDependency.supplierFromPath("nirc_ehr/window/NIRCClinicalHistoryWindow.js"), this);
        ehrService.registerClientDependency(ClientDependency.supplierFromPath("nirc_ehr/window/NIRCCaseHistoryWindow.js"), this);
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

        ehrService.addModulePreferringTaskFormEditUI(this);

        ehrService.registerDemographicsProvider(new ActiveFlagsDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new ParentsDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new ActiveAssignmentsDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new ProtocolAssignmentDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new HousingDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new CagematesDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new CagematesDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new ActiveCasesDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new ActiveTreatmentsDemographicsProvider(this));
        ehrService.registerDemographicsProvider(new SourceDemographicsProvider(this));

        EHRService.get().registerHistoryDataSource(new ArrivalDataSource(this));
        EHRService.get().registerHistoryDataSource(new BiopsyDataSource(this));
        EHRService.get().registerHistoryDataSource(new BirthDataSource(this));
        EHRService.get().registerHistoryDataSource(new BloodDrawDataSource(this));
        EHRService.get().registerHistoryDataSource(new BreederDataSource(this));
        EHRService.get().registerHistoryDataSource(new DeathDataSource(this));
        EHRService.get().registerHistoryDataSource(new DefaultAlopeciaDataSource(this));
        EHRService.get().registerHistoryDataSource(new DefaultAnimalRecordFlagDataSource(this));
        EHRService.get().registerHistoryDataSource(new DefaultClinicalRemarksDataSource(this));
        EHRService.get().registerHistoryDataSource(new DefaultNotesDataSource(this));
        EHRService.get().registerHistoryDataSource(new DefaultVitalsDataSource(this));
        EHRService.get().registerHistoryDataSource(new DepartureDataSource(this));
        EHRService.get().registerHistoryDataSource(new DrugAdminDataSource(this));
        EHRService.get().registerHistoryDataSource(new FlagsDataSource(this));
        EHRService.get().registerHistoryDataSource(new FosteringDataSource(this));
        EHRService.get().registerHistoryDataSource(new ExemptionsDataSource(this));
        EHRService.get().registerHistoryDataSource(new HistopathologyDataSource(this));
        EHRService.get().registerHistoryDataSource(new NIRCCaseCloseDataSource(this));
        EHRService.get().registerHistoryDataSource(new NIRCCaseOpenDataSource(this));
        EHRService.get().registerHistoryDataSource(new NIRCClinicalObservationsDataSource(this));
        EHRService.get().registerHistoryDataSource(new NIRCClinicalRemarksDataSource(this));
        EHRService.get().registerHistoryDataSource(new NIRCEndTreatmentOrderDataSource(this));
        EHRService.get().registerHistoryDataSource(new NIRCHousingDataSource(this));
        EHRService.get().registerHistoryDataSource(new NIRCObservationOrdersDataSource(this));
        EHRService.get().registerHistoryDataSource(new NIRCVitalsDataSource(this));
        EHRService.get().registerHistoryDataSource(new ObservationsDataSource(this));
        EHRService.get().registerHistoryDataSource(new PairingsDataSource(this));
        EHRService.get().registerHistoryDataSource(new PhysicalExamDataSource(this));
        EHRService.get().registerHistoryDataSource(new PregnancyDataSource(this));
        EHRService.get().registerHistoryDataSource(new ProceduresDataSource(this));
        EHRService.get().registerHistoryDataSource(new ProjectAssignmentDataSource(this));
        EHRService.get().registerHistoryDataSource(new ProtocolDataSource(this));
        EHRService.get().registerHistoryDataSource(new SerologyDataSource(this));

        ehrService.registerClientDependency(ClientDependency.supplierFromPath("nirc_ehr/nirc_ehr_api"), this);
        ehrService.registerClientDependency(ClientDependency.supplierFromPath("nirc_ehr/nircOverrides.js"), this);
        ehrService.registerActionOverride("animalHistory", this, "views/animalHistory.html");
        ehrService.registerActionOverride("participantView", this, "views/participantView.html");
        ehrService.registerActionOverride("enterData", this, "views/enterData.html");

        ehrService.registerTriggerScriptOption("datasetsToCloseOnNewEntry", List.of("assignment", "protocolAssignment"));
        RoleManager.registerRole(new NIRCEHRVetTechRole());

        EHRService.get().registerMoreActionsButton(new ShowEditUIButton(this, "ehr", "observation_types", EHRDataAdminPermission.class), "ehr", "observation_types");

        EHRService.get().unregisterMoreActionsButtons("study", "treatment_order");
        EHRService.get().registerMoreActionsButton(new MarkTreatmentCompletedButton(this, "study", "treatment_order", "Set End Date"), "study", "treatment_order");
        EHRService.get().registerMoreActionsButton(new MarkTreatmentCompletedButton(this, "study", "observation_order", "Set End Date"), "study", "observation_order");

        registerDataEntry();
        NotificationService.get().registerNotification(new NIRCDeathNotification());

        // Ensure N: is mounted if it's configured, as it's being mapped in via a symlink/shortcut, so we can't
        // recognize paths using it based solely on their drive letter and mount just-in-time
        if (NetworkDrive.getNetworkDrive("N:\\") != null)
        {
            NetworkDrive.exists(new File("N:\\"));
        }
    }

    private void registerDataEntry()
    {
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCAliasFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCAssignmentFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCArrivalFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCBirthFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCBulkClinicalFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCDepartureFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCDeathNecropsyFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCHousingFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCMedicationTreatmentFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCProjectFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCProtocolFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCPregnancyFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCWeightFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCFlagsFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCExemptionsFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCNotesFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCCasesFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCBuildingFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCRoomFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCFloorFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCCageFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCClinicalObservationsFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCClinicalRoundsFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCAnimalTrainingFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCPairingsFormType.class, this));
        EHRService.get().registerFormType(new DefaultDataEntryFormFactory(NIRCBulkBehaviorFormType.class, this));
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