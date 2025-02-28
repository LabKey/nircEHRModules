package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.view.template.ClientDependency;

import java.util.List;

public class NIRCTreatmentOrderFormSection extends BaseFormSection
{
    public static final String LABEL = "Medications/Treatments Orders";

    public NIRCTreatmentOrderFormSection()
    {
        super("study", "treatment_order", LABEL, "ehr-gridpanel", true, true, true);
        setClientStoreClass("NIRC_EHR.data.DrugAdministrationRunsClientStore");
        addClientDependency(ClientDependency.supplierFromPath("ehr/data/DrugAdministrationRunsClientStore.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/data/DrugAdministrationRunsClientStore.js"));
    }

    public NIRCTreatmentOrderFormSection(boolean isChild, String parentQueryName)
    {
        this();
        if (isChild)
        {
            addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/ParentChild.js"));
            addConfigSource("ParentChild");

            addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/data/DrugAdministrationRunsChildClientStore.js"));
            setClientStoreClass("NIRC_EHR.data.DrugAdministrationRunsChildClientStore");
            addExtraProperty("parentQueryName", parentQueryName);
        }
    }

    @Override
    public List<String> getTbarButtons()
    {
        List<String> defaultButtons = super.getTbarButtons();
        int idx = defaultButtons.indexOf("SELECTALL");
        if (idx > -1)
            defaultButtons.add(idx + 1, "NIRC_DRUG_AMOUNT_HELPER");
        else
            defaultButtons.add("DRUGAMOUNTHELPER");
        return defaultButtons;
    }
}