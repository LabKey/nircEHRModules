package org.labkey.nirc_ehr.history;

import org.labkey.api.module.Module;

public class NIRCBehaviorObservationsDataSource extends NIRCObservationsDataSource
{
    public NIRCBehaviorObservationsDataSource(Module module)
    {
        super("study", "behaviorObservations", "Behavior Observations", "Behavior Observations", module);
    }
}

