package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.ehr.EHRService;
import org.labkey.api.view.template.ClientDependency;

public class NIRCWeightFormSection extends BaseFormSection
{
    public NIRCWeightFormSection(boolean initCollapsed, boolean addCopyFromSection)
    {
        super("study", "weight", "Weights", "ehr-gridpanel", true, initCollapsed, addCopyFromSection);
    }

    public NIRCWeightFormSection(boolean initCollapsed, boolean addCopyFromSection, boolean isChild, String parentQueryName)
    {
        this(initCollapsed, addCopyFromSection);
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
