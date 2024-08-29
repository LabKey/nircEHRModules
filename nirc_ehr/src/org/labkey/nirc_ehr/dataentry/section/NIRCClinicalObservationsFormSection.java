package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.view.template.ClientDependency;

import java.util.List;

public class NIRCClinicalObservationsFormSection extends BaseFormSection
{
    public static final String LABEL = "Clinical Observations";
    public static boolean useDefaultButtons = true;

    public NIRCClinicalObservationsFormSection(boolean useDefaultBtns)
    {
        super("study", "clinical_observations", LABEL, "ehr-clinicalobservationgridpanel", true, false, true);

        useDefaultButtons = useDefaultBtns;
        addClientDependency(ClientDependency.supplierFromPath("ehr/plugin/ClinicalObservationsCellEditing.js"));
        addClientDependency(ClientDependency.supplierFromPath("ehr/data/ClinicalObservationsClientStore.js"));
        addClientDependency(ClientDependency.supplierFromPath("ehr/grid/ClinicalObservationGridPanel.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/buttons/clinicalObsGridButton.js"));
        setClientStoreClass("EHR.data.ClinicalObservationsClientStore");
    }
    @Override
    public List<String> getTbarButtons()
    {
        List<String> defaults = super.getTbarButtons();
        defaults.add("ADDREQUIREDCLINICALOBS");

        if (!useDefaultButtons)
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
        if (!useDefaultButtons)
        {
            moreActionButtons.clear();
        }

        return moreActionButtons;
    }
}
