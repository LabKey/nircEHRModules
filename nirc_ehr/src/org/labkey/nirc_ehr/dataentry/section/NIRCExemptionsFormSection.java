package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.ehr.EHRService;

public class NIRCExemptionsFormSection extends BaseFormSection
{
    public NIRCExemptionsFormSection(String label)
    {
        super("study", "exemptions", label, "ehr-gridpanel", true, false, false);
    }
}
