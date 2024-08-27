package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.view.template.ClientDependency;

public class NIRCVitalsFormSection extends BaseFormSection
{
    public static final String LABEL = "Vitals";

    public NIRCVitalsFormSection()
    {
        super("study", "vitals", LABEL, "ehr-gridpanel", true, true, true);
    }

    public NIRCVitalsFormSection(boolean isChild, String parentQueryName)
    {
        this();
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
