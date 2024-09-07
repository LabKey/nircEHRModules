package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.view.template.ClientDependency;

import java.util.List;

public class NIRCObservationOrdersFormSection extends BaseFormSection
{

    public static final String LABEL = "Observation Orders";
    private boolean _autoPopulateDailyObs = true;

    public NIRCObservationOrdersFormSection(boolean autoPopulateDailyObs, boolean initCollapsed)
    {
        super("study", "observation_order", LABEL, "ehr-clinicalobservationgridpanel", true, initCollapsed, true);

        _autoPopulateDailyObs = autoPopulateDailyObs;
        addClientDependency(ClientDependency.supplierFromPath("ehr/plugin/ClinicalObservationsCellEditing.js"));
        addClientDependency(ClientDependency.supplierFromPath("ehr/data/ClinicalObservationsClientStore.js"));
        addClientDependency(ClientDependency.supplierFromPath("ehr/grid/ClinicalObservationGridPanel.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/buttons/clinicalObsGridButton.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/buttons/addClinicalObsButton.js"));
        setClientStoreClass("EHR.data.ClinicalObservationsClientStore");
    }

    public NIRCObservationOrdersFormSection(boolean isChild, String parentQueryName)
    {
        this(false, true);

        if (isChild && null != parentQueryName)
        {
            addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/ParentChild.js"));
            addConfigSource("ParentChild");

            addClientDependency(ClientDependency.supplierFromPath("ehr/data/ChildClientStore.js"));
            setClientStoreClass("EHR.data.ChildClientStore");
            addExtraProperty("parentQueryName", parentQueryName);
        }
    }

    @Override
    public List<String> getTbarButtons()
    {
        List<String> defaults = super.getTbarButtons();

        if (_autoPopulateDailyObs)
        {
            defaults.add("NIRC_AUTO_POPULATE_DAILY_OBS");
        }
        else {
            defaults.add("NIRC_DAILY_CLINICAL_OBS");
        }

        return defaults;

    }
}
