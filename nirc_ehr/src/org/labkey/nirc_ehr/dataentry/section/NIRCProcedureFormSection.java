package org.labkey.nirc_ehr.dataentry.section;

public class NIRCProcedureFormSection extends BaseFormSection
{
    public static final String LABEL = "Procedures";

    public NIRCProcedureFormSection()
    {
        super("study", "prc", LABEL, "ehr-gridpanel", true, true, true);
    }
}
