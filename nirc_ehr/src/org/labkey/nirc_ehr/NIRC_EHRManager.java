package org.labkey.nirc_ehr;

import org.labkey.api.query.Queryable;

import java.util.List;

public class NIRC_EHRManager
{
    @Queryable
    public static final String DAILY_CLINICAL_OBS_TITLE = "Daily Clinical Observations";
    public static final List<String> DAILY_CLINICAL_OBS = List.of("Activity", "Appetite", "BCS", "Hydration", "Stool", "Verified Id?");
    public static final String SIB_OBS_TITLE = "SIB Observations";
    public static final List<String> SIB_OBS = List.of("Environmental Change", "Self Biting Observed", "Other Stereotopy", "New Injury Observed", "Special Enrichment", "Wound Status", "Wound Severity");

    private static final NIRCOrchardFileGenerator _orchardFileGenerator = new NIRCOrchardFileGenerator();
    public static NIRCOrchardFileGenerator getOrchardFileGenerator()
    {
        return _orchardFileGenerator;
    }
}
