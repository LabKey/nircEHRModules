package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.view.template.ClientDependency;

import java.util.List;

public class NIRCTreatmentGivenFormSection extends BaseFormSection
{
    public static final String LABEL = "Medications/Treatments Given";

    public NIRCTreatmentGivenFormSection()
    {
        super("study", "drug", LABEL, "ehr-gridpanel", true, true, true);
        setClientStoreClass("EHR.data.DrugAdministrationRunsClientStore");
        addClientDependency(ClientDependency.supplierFromPath("ehr/data/DrugAdministrationRunsClientStore.js"));
    }

    public NIRCTreatmentGivenFormSection(boolean isChild, String parentQueryName)
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
            defaultButtons.add(idx + 1, "DRUGAMOUNTHELPER");
        else
            defaultButtons.add("DRUGAMOUNTHELPER");
        return defaultButtons;
    }
}
