package org.labkey.nirc_ehr.dataentry.section;

public class NIRCHousingFormSection extends BaseFormSection
{
    public NIRCHousingFormSection(boolean collapsible, boolean initCollapsed)
    {
        super("study", "housing", "Housing Transfers", "ehr-gridpanel", collapsible, initCollapsed, true);
    }
}