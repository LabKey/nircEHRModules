package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.ehr.EHRService;

public class NIRCFlagsFormSection extends BaseFormSection
{
    public NIRCFlagsFormSection(String label)
    {
        super("study", "flags", label, "ehr-gridpanel", true, false, false);
    }
}
