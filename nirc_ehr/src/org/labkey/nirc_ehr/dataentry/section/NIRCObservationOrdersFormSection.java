package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.view.template.ClientDependency;

import java.util.List;

public class NIRCObservationOrdersFormSection extends BaseFormSection
{

    public static final String LABEL = "Observation Orders";
    private boolean _useDefaultButtons = true;
    private boolean _autoPopulateDailyObs = true;

    public NIRCObservationOrdersFormSection(boolean useDefaultBtns, boolean autoPopulateDailyObs, boolean initCollapsed)
    {
        super("study", "observation_order", LABEL, "ehr-clinicalobservationgridpanel", true, initCollapsed, true);

        _useDefaultButtons = useDefaultBtns;
        _autoPopulateDailyObs = autoPopulateDailyObs;
        addClientDependency(ClientDependency.supplierFromPath("ehr/plugin/ClinicalObservationsCellEditing.js"));
        addClientDependency(ClientDependency.supplierFromPath("ehr/data/ClinicalObservationsClientStore.js"));
        addClientDependency(ClientDependency.supplierFromPath("ehr/grid/ClinicalObservationGridPanel.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/buttons/clinicalObsGridButton.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/buttons/addClinicalObsButton.js"));
        setClientStoreClass("EHR.data.ClinicalObservationsClientStore");
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

        if (!_useDefaultButtons)
        {
            defaults.clear();
            defaults.add("ADDRECORD");
            defaults.add("DELETERECORD");
        }

        return defaults;

    }

    @Override
    public List<String> getTbarMoreActionButtons()
    {
        List<String> moreActionButtons = super.getTbarMoreActionButtons();
        if (!_useDefaultButtons)
        {
            moreActionButtons.clear();
        }

        return moreActionButtons;
    }

}
