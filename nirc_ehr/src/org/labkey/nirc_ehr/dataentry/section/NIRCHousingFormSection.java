package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.view.template.ClientDependency;

public class NIRCHousingFormSection extends BaseFormSection
{
    public NIRCHousingFormSection(boolean collapsible, boolean initCollapsed)
    {
        super("study", "housing", "Housing Transfers", "ehr-gridpanel", collapsible, initCollapsed, true);
    }

    public NIRCHousingFormSection(boolean collapsible, boolean initCollapsed, boolean isChild, String parentQueryName)
    {
        this(collapsible, initCollapsed);
        if (isChild)
        {
            addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/ParentChild.js"));
            addConfigSource("ParentChild");

            addClientDependency(ClientDependency.supplierFromPath("ehr/data/ChildClientStore.js"));
            setClientStoreClass("EHR.data.ChildClientStore");
            addExtraProperty("parentQueryName", parentQueryName);
        }
    }
}