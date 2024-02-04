package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.SimpleGridPanel;
import org.labkey.api.ehr.dataentry.TaskFormSection;
import org.labkey.api.ehr.dataentry.UnsaveableTask;
import org.labkey.api.ehr.dataentry.forms.DocumentArchiveFormSection;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;

import java.util.Arrays;

/**
 * User: bimber
 * Date: 7/29/13
 * Time: 5:03 PM
 */
public class DepartureFormType extends UnsaveableTask
{
    public static final String NAME = "departure";

    public DepartureFormType(DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, NAME, "Departure", "Colony Management", Arrays.asList(
                new TaskFormSection(),
                new DocumentArchiveFormSection(),
                new NIRCAnimalDetailsFormSection(),
                new SimpleGridPanel("study", "departure", "Departures")
        ));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/plugin/RowEditor.js"));
    }
}

