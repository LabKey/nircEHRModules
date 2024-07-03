package org.labkey.nirc_ehr.security;

import org.labkey.api.ehr.security.EHRDataEntryPermission;
import org.labkey.api.security.permissions.InsertPermission;
import org.labkey.api.security.permissions.ReadPermission;
import org.labkey.api.security.permissions.UpdatePermission;
import org.labkey.api.security.roles.AbstractRole;

public class EHRVetTechRole extends AbstractRole
{
    public EHRVetTechRole()
    {
        super("EHR Veterinarian Technician", "Users with this role are permitted to submit and update records but not close, approve, or delete them.",
                ReadPermission.class,
                InsertPermission.class,
                UpdatePermission.class,
                EHRDataEntryPermission.class,
                EHRVetTechPermission.class
        );
    }
}