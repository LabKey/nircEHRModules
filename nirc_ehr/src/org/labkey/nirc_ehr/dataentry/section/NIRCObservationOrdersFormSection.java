package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.view.template.ClientDependency;

import java.util.List;

public class NIRCObservationOrdersFormSection extends BaseFormSection
{

    public static final String LABEL = "Observation Orders";
    private String _dailyObsOption;

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
