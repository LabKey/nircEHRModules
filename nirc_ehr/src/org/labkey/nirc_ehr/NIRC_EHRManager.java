package org.labkey.nirc_ehr;

import org.labkey.api.query.Queryable;

import java.util.List;

public class NIRC_EHRManager
{
    @Queryable
    public static final String DAILY_CLINICAL_OBS_TITLE = "Daily Clinical Observations";
    public static final List<String> DAILY_CLINICAL_OBS = List.of("Activity", "Appetite", "BCS", "Hydration", "Stool", "Verified Id?");
    public static final String DAILY_CLINICAL_OBS_LIST = String.join(";",NIRC_EHRManager.DAILY_CLINICAL_OBS);
    private static NIRCOrchardFileGenerator _orchardFileGenerator = new NIRCOrchardFileGenerator();
    public static NIRCOrchardFileGenerator getOrchardFileGenerator()
    {
        return _orchardFileGenerator;
    }
}
