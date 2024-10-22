package org.labkey.nirc_ehr.demographics;

import org.labkey.api.ehr.demographics.AbstractListDemographicsProvider;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class NecropsyStatusDemographicsProvider extends AbstractListDemographicsProvider
{

    public NecropsyStatusDemographicsProvider(Module module)
    {
        super(module, "study", "necropsyStatus", "calculated_status");
        _supportsQCState = false;
    }

    @Override
    public boolean isAsync()
    {
        return true;
    }

    @Override
    protected Collection<FieldKey> getFieldKeys()
    {
        Set<FieldKey> keys = new HashSet<FieldKey>();
        keys.add(FieldKey.fromString("Id"));
        keys.add(FieldKey.fromString("calculated_status"));

        return keys;
    }
}