package org.labkey.nirc_ehr.dataentry.section;

public class NIRCVitalsFormSection extends BaseFormSection
{
    public static final String LABEL = "Vitals";

    public NIRCVitalsFormSection()
    {
        super("study", "vitals", LABEL, "ehr-gridpanel", true, true, true);
    }
}
