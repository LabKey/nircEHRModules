/*
 * Copyright (c) 2024-2026 LabKey Corporation
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
package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.view.template.ClientDependency;

import java.util.List;

public class NIRCObservationOrdersFormSection extends BaseFormSection
{

    public static final String LABEL = "Observation Orders";
    private final String _dailyObsOption;

    public NIRCObservationOrdersFormSection(String dailyObsOption, boolean initCollapsed)
    {
        super("study", "observation_order", LABEL, "ehr-clinicalobservationgridpanel", true, initCollapsed, true);

        _dailyObsOption = dailyObsOption;
        addClientDependency(ClientDependency.supplierFromPath("ehr/plugin/ClinicalObservationsCellEditing.js"));
        addClientDependency(ClientDependency.supplierFromPath("ehr/grid/ClinicalObservationGridPanel.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/buttons/clinicalObsGridButton.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/buttons/addClinicalObsButton.js"));

        setClientStoreClass("NIRC_EHR.data.ObsOrdersClientStore");
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/data/ObsOrdersClientStore.js"));
    }

    public NIRCObservationOrdersFormSection(String dailyObsOption, boolean isChild, String parentQueryName)
    {
        this(dailyObsOption, true);

        if (isChild && null != parentQueryName)
        {
            addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/ParentChild.js"));
            addConfigSource("ParentChild");

            addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/data/ObsOrderChildClientStore.js"));
            setClientStoreClass("NIRC_EHR.data.ObsOrderChildClientStore");
            addExtraProperty("parentQueryName", parentQueryName);
        }
    }

    @Override
    public List<String> getTbarButtons()
    {
        List<String> defaults = super.getTbarButtons();

        if (_dailyObsOption != null)
        {
            defaults.add(_dailyObsOption);
        }

        return defaults;

    }
}
