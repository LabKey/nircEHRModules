package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCClinicalObservationsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTaskFormSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NIRCClinicalObservationsFormType extends NIRCBaseTaskFormType
{
    public static final String NAME = "Clinical Observations";

    public NIRCClinicalObservationsFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, NAME, "Clinical", Arrays.asList(
                new NIRCTaskFormSection(),
                new NIRCAnimalDetailsFormSection(),
                new NIRCClinicalObservationsFormSection(true, false)
        ));

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/ClinicalDefaults.js"));

        for (FormSection s : this.getFormSections())
        {
            s.addConfigSource("ClinicalDefaults");
        }
    }

    @Override
    protected List<String> getButtonConfigs()
    {
        List<String> defaultButtons = new ArrayList<String>();
        defaultButtons.add("SAVEDRAFT");
        defaultButtons.add("SUBMIT"); //submit final
        return defaultButtons;
    }
}