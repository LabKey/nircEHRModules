package org.labkey.nirc_ehr.demographics;

import org.apache.commons.lang3.StringUtils;
import org.labkey.api.data.Container;
import org.labkey.api.ehr.demographics.AbstractListDemographicsProvider;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class CagematesDemographicsProvider extends AbstractListDemographicsProvider
{
    public CagematesDemographicsProvider(Module module)
    {
        super(module, "study", "demographicsCagemates", "cagemates");
        _supportsQCState = false;
    }

    @Override
    public boolean isAsync()
    {
        return true;
    }

    @Override
    protected Set<FieldKey> getFieldKeys()
    {
        Set<FieldKey> keys = new HashSet<>();
        keys.add(FieldKey.fromString("total"));
        keys.add(FieldKey.fromString("animals"));

        return keys;
    }

    @Override
    public Set<String> getIdsToUpdate(Container c, String id, Map<String, Object> originalProps, Map<String, Object> newProps)
    {
        List<Map<String, Object>> oldList = originalProps == null ? null : (List)originalProps.get(_propName);
        List<Map<String, Object>> newList = newProps == null ? null : (List)newProps.get(_propName);
        Set<String> ret = new TreeSet<>();

        List<String> oldAnimals = oldList == null || oldList.isEmpty() ? Collections.emptyList() : toList(oldList.get(0).get("animals"));
        List<String> newAnimals = newList == null || newList.isEmpty() ? Collections.emptyList() : toList(newList.get(0).get("animals"));

        ret.addAll(newAnimals);
        ret.addAll(oldAnimals);
        ret.remove(id);

        if (!ret.isEmpty())
        {
            _log.info(id + ": Triggered additional housing updates for " + ret.size() + " ids: " + StringUtils.join(ret, ";"));
        }

        return ret;
    }

    private List<String> toList(Object input)
    {
        if (input == null)
            return Collections.emptyList();

        if (input instanceof  List)
        {
            return (List)input;
        }
        else if (input instanceof String)
        {
            return Arrays.asList(StringUtils.split((String) input, ", "));
        }
        else
        {
            _log.error("Unknown type: " + input.getClass().getName() + ", " + input);
            return Collections.emptyList();
        }
    }

    @Override
    public boolean requiresRecalc(String schema, String query)
    {
        return ("study".equalsIgnoreCase(schema) && "Housing".equalsIgnoreCase(query)) ||
                ("study".equalsIgnoreCase(schema) && "Demographics".equalsIgnoreCase(query));
    }

}