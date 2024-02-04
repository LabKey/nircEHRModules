package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.SimpleGridPanel;
import org.labkey.api.ehr.dataentry.UnsaveableTask;
import org.labkey.api.ehr.dataentry.forms.DocumentArchiveFormSection;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTaskFormSection;

import java.util.Arrays;

public class NIRCDepartureFormType extends UnsaveableTask
{
    public static final String NAME = "departure";

    public NIRCDepartureFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, "Departure", "Colony Management", Arrays.asList(
                new NIRCTaskFormSection(),
                new DocumentArchiveFormSection(),
                new NIRCAnimalDetailsFormSection(),
                new SimpleGridPanel("study", "departure", "Departures")
        ));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/plugin/RowEditor.js"));
    }
}

