package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.view.template.ClientDependency;

public class NIRCProjectAssignmentFormSection extends BaseFormSection
{
    public NIRCProjectAssignmentFormSection(boolean allowAnyId, boolean collapsible, boolean initCollapsed)
    {
        super("study", "assignment", "Project Assignment", "ehr-gridpanel", collapsible, initCollapsed, true);
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/data/AssignmentsClientStore.js"));

        if (allowAnyId)
        {
            setClientStoreClass("NIRC_EHR.data.AssignmentsAnyIdClientStore");
            addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/data/AssignmentsAnyIdClientStore.js"));
        }
        else
        {
            setClientStoreClass("NIRC_EHR.data.AssignmentsClientStore");
        }
    }
}