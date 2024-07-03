package org.labkey.nirc_ehr.security;

import org.labkey.api.ehr.security.AbstractEHRPermission;

public class EHRVetTechPermission extends AbstractEHRPermission
{
    public EHRVetTechPermission()
    {
        super("EHRVetTechPermission", "This is the base permission for vet techs. It grants the ability to submit and update records but not close, approve, or delete them.");
    }
}