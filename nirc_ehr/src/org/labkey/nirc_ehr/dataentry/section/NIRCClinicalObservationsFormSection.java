package org.labkey.nirc_ehr.dataentry.section;

public class NIRCClinicalObservationsFormSection extends BaseFormSection
{
    public static final String LABEL = "Clinical Observations";

    public NIRCClinicalObservationsFormSection()
    {
        super("study", "clinical_observations", LABEL, "ehr-gridpanel", true, true, true);
    }
}
