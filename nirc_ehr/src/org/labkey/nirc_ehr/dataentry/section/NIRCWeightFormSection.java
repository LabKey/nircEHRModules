package org.labkey.nirc_ehr.dataentry.section;

public class NIRCWeightFormSection extends BaseFormSection
{
    public NIRCWeightFormSection(boolean initCollapsed, boolean addCopyFromSection)
    {
        super("study", "weight", "Weights", "ehr-gridpanel", true, initCollapsed, addCopyFromSection);
    }
}
