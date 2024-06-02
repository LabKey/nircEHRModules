package org.labkey.nirc_ehr.dataentry.section;

public class NIRCClinicalRemarksFormSection extends BaseFormSection
{
    public static final String LABEL = "Clinical Remarks";

    public NIRCClinicalRemarksFormSection()
    {
        super("study", "clinremarks", LABEL, "ehr-gridpanel", true, true, true);
    }
}
