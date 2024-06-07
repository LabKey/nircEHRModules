package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.view.template.ClientDependency;

public class NIRCClinicalObservationsFormSection extends BaseFormSection
{
    public static final String LABEL = "Clinical Observations";

    public NIRCClinicalObservationsFormSection()
    {
        super("study", "clinical_observations", LABEL, "ehr-clinicalobservationgridpanel", true, true, true);

        addClientDependency(ClientDependency.supplierFromPath("ehr/plugin/ClinicalObservationsCellEditing.js"));
        addClientDependency(ClientDependency.supplierFromPath("ehr/data/ClinicalObservationsClientStore.js"));
        addClientDependency(ClientDependency.supplierFromPath("ehr/grid/ClinicalObservationGridPanel.js"));
        setClientStoreClass("EHR.data.ClinicalObservationsClientStore");
    }
}
