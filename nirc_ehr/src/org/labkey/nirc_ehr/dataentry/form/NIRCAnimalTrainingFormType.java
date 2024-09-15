package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.EHRService;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.module.Module;
import org.labkey.nirc_ehr.dataentry.section.BaseFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTaskFormSection;

import java.util.Arrays;

public class NIRCAnimalTrainingFormType extends NIRCBaseTaskFormType
{
    public static final String NAME = "Animal Training";

    public NIRCAnimalTrainingFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, NAME, "Behavior", Arrays.asList(
                new NIRCTaskFormSection(),
                new NIRCAnimalDetailsFormSection(),
                new BaseFormSection("study", "nhpTraining", NAME, "ehr-gridpanel", EHRService.FORM_SECTION_LOCATION.Body, true, false)
        ));
    }
}
