package org.labkey.nirc_ehr.dataentry.section;

public class NIRCTreatmentOrderFormSection extends BaseFormSection
{
    public static final String LABEL = "Medications/Treatments Orders";

    public NIRCTreatmentOrderFormSection()
    {
        super("study", "treatment_order", LABEL, "ehr-gridpanel", true, true, true);
    }
}
