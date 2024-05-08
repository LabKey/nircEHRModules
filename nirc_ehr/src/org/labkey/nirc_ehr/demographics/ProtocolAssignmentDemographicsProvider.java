package org.labkey.nirc_ehr.demographics;

import org.labkey.api.ehr.demographics.AbstractListDemographicsProvider;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProtocolAssignmentDemographicsProvider extends AbstractListDemographicsProvider
{
    public ProtocolAssignmentDemographicsProvider(Module owner)
    {
        super(owner, "study", "protocolAssignment", "protocolAssignments");
    }

    @Override
    protected Collection<FieldKey> getFieldKeys()
    {
        Set<FieldKey> keys = new HashSet<>();

        keys.add(FieldKey.fromString("lsid"));
        keys.add(FieldKey.fromString("Id"));
        keys.add(FieldKey.fromString("date"));
        keys.add(FieldKey.fromString("enddate"));
        keys.add(FieldKey.fromString("protocol"));
        keys.add(FieldKey.fromString("protocol/title"));
        keys.add(FieldKey.fromString("protocol/displayName"));
        keys.add(FieldKey.fromString("protocol/InvestigatorId"));
        keys.add(FieldKey.fromString("protocol/InvestigatorId/firstName"));
        keys.add(FieldKey.fromString("protocol/InvestigatorId/lastName"));
        keys.add(FieldKey.fromString("remark"));

        return keys;
    }

    @Override
    public boolean requiresRecalc(String schema, String query)
    {
        return ("study".equalsIgnoreCase(schema) && "protocolAssignment".equalsIgnoreCase(query));
    }
}
