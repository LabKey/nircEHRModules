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
        addClientDependency(ClientDependency.supplierFromPath("ehr/data/ClinicalObservationsClientStore.js"));
        addClientDependency(ClientDependency.supplierFromPath("ehr/grid/ClinicalObservationGridPanel.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/buttons/clinicalObsGridButton.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/buttons/addClinicalObsButton.js"));
        setClientStoreClass("EHR.data.ClinicalObservationsClientStore");
    }

    public NIRCObservationOrdersFormSection(String dailyObsOption, boolean isChild, String parentQueryName)
    {
        this(dailyObsOption, true);

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

        if (_dailyObsOption != null)
        {
            defaults.add(_dailyObsOption);
        }

        return defaults;

    }
}
