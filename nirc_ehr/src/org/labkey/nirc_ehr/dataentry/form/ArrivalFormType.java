package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.ehr.dataentry.TaskForm;
import org.labkey.api.ehr.dataentry.TaskFormSection;
import org.labkey.api.ehr.dataentry.WeightFormSection;
import org.labkey.api.ehr.dataentry.forms.ArrivalInstructionsFormSection;
import org.labkey.api.ehr.dataentry.forms.DocumentArchiveFormSection;
import org.labkey.api.ehr.dataentry.forms.LockAnimalsFormSection;
import org.labkey.api.ehr.dataentry.forms.NewAnimalFormSection;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;

import java.util.Arrays;
import java.util.List;

public class ArrivalFormType extends TaskForm
{
    public static final String NAME = "arrival";

    public ArrivalFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, "Arrival", "Colony Management", Arrays.asList(
                new LockAnimalsFormSection(),
                new ArrivalInstructionsFormSection(),
                new TaskFormSection(),
                new DocumentArchiveFormSection(),
                new NIRCAnimalDetailsFormSection(),
                new NewAnimalFormSection("study", "arrival", "Arrivals", false),
                new WeightFormSection()));

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/plugin/RowEditor.js"));

    }

    public ArrivalFormType(DataEntryFormContext ctx, Module owner, List<FormSection> sections)
    {
        super(ctx, owner, NAME, "Arrival", "Colony Management", sections);
    }
}
