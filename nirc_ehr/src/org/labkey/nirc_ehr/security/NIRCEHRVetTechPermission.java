package org.labkey.nirc_ehr.security;

import org.labkey.api.ehr.security.AbstractEHRPermission;

public class NIRCEHRVetTechPermission extends AbstractEHRPermission
{
    public NIRCEHRVetTechPermission()
    {
        super("NIRCEHRVetTechPermission", "This is the base permission for vet techs. It grants the ability to submit and update records but not close, approve, or delete them.");
    }
}