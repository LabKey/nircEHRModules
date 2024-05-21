package org.labkey.nirc_ehr.dataentry.section;

public class NIRCTreatmentGivenFormSection extends BaseFormSection
{
    public static final String LABEL = "Medications/Treatments Given";

    public NIRCTreatmentGivenFormSection()
    {
        super("study", "drug", LABEL, "ehr-gridpanel", true, true, true);
    }
}
