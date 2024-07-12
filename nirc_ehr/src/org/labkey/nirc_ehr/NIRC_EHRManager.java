package org.labkey.nirc_ehr;

public class NIRC_EHRManager
{
    private static NIRCOrchardFileGenerator _orchardFileGenerator = new NIRCOrchardFileGenerator();
    public static NIRCOrchardFileGenerator getOrchardFileGenerator()
    {
        return _orchardFileGenerator;
    }
}
