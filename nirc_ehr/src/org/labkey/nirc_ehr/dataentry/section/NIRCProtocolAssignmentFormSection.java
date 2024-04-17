package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.view.template.ClientDependency;

public class NIRCProtocolAssignmentFormSection extends BaseFormSection
{
    public NIRCProtocolAssignmentFormSection(boolean allowAnyId, boolean collapsible, boolean initCollapsed)
    {
        super("study", "protocolAssignment", "Assignments","ehr-gridpanel", collapsible, initCollapsed);
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