package org.labkey.nirc_ehr.history;

import org.labkey.api.module.Module;

public class NIRCClinicalObservationsDataSource extends NIRCObservationsDataSource
{
    public NIRCClinicalObservationsDataSource(Module module)
    {
        super("study", "clinicalObservations", "Clinical Observations", "Clinical Observations", module);
    }
}
