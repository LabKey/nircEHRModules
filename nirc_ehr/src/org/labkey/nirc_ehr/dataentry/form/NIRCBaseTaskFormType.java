package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.ehr.dataentry.TaskForm;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;

import java.util.ArrayList;
import java.util.List;

public class NIRCBaseTaskFormType extends TaskForm
{
    protected NIRCBaseTaskFormType(DataEntryFormContext ctx, Module owner, String name, String label, String category, List<FormSection> sections)
    {
        super(ctx, owner, name, label, category, sections);

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/NIRCDefault.js"));
    }

    @Override
    protected List<String> getButtonConfigs()
    {
        List<String> defaultButtons = new ArrayList<>();
        defaultButtons.add("SAVEDRAFT");
        defaultButtons.add("REVIEW");
        defaultButtons.add("SUBMIT");

        return defaultButtons;
    }

    @Override
    protected List<String> getMoreActionButtonConfigs()
    {
        List<String> configs = super.getMoreActionButtonConfigs();
        configs.remove("REVIEW");
        return configs;
    }
}
