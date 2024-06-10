package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.view.template.ClientDependency;

public class NIRCTreatmentOrderFormSection extends BaseFormSection
{
    public static final String LABEL = "Medications/Treatments Orders";

    public NIRCTreatmentOrderFormSection()
    {
        super("study", "treatment_order", LABEL, "ehr-gridpanel", true, true, true);
    }

    public NIRCTreatmentOrderFormSection(boolean isChild, String parentQueryName)
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