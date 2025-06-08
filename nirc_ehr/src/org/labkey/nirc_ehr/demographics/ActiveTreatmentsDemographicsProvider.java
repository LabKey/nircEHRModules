package org.labkey.nirc_ehr.demographics;

import org.labkey.api.data.SimpleFilter;
import org.labkey.api.ehr.demographics.AbstractListDemographicsProvider;
import org.labkey.api.module.Module;
import org.labkey.api.query.FieldKey;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ActiveTreatmentsDemographicsProvider extends AbstractListDemographicsProvider
{
    public ActiveTreatmentsDemographicsProvider(Module owner)
    {
        super(owner, "study", "Treatment Orders", "activeTreatments");
        _supportsQCState = false;
    }

    @Override
    protected Set<FieldKey> getFieldKeys()
    {
        Set<FieldKey> keys = new HashSet<>();
        keys.add(FieldKey.fromString("lsid"));
        keys.add(FieldKey.fromString("Id"));
        keys.add(FieldKey.fromString("code"));
        keys.add(FieldKey.fromString("code/meaning"));
        keys.add(FieldKey.fromString("date"));
        keys.add(FieldKey.fromString("enddate"));
        keys.add(FieldKey.fromString("performedby"));
        keys.add(FieldKey.fromString("route"));

        keys.add(FieldKey.fromString("dosage"));
        keys.add(FieldKey.fromString("dosage_units"));
        keys.add(FieldKey.fromString("amount"));
        keys.add(FieldKey.fromString("amount_units"));
        keys.add(FieldKey.fromString("concentration"));
        keys.add(FieldKey.fromString("concentration_units"));
        keys.add(FieldKey.fromString("volume"));
        keys.add(FieldKey.fromString("vol_units"));
        keys.add(FieldKey.fromString("amountAndVolume"));

        keys.add(FieldKey.fromString("remark"));
        keys.add(FieldKey.fromString("frequency"));
        keys.add(FieldKey.fromString("frequency/meaning"));

        keys.add(FieldKey.fromString("amountWithUnits"));
        keys.add(FieldKey.fromString("category"));

        return keys;
    }

    @Override
    public Collection<String> getKeysToTest()
    {
        //for now, simply skip the whole provider.  because different records can be active from day to day, this makes validation tricky
        Set<String> keys = new HashSet<>(super.getKeysToTest());
        keys.remove(_propName);

        return keys;
    }

    @Override
    protected SimpleFilter getFilter(Collection<String> ids)
    {
        SimpleFilter filter = super.getFilter(ids);
        filter.addCondition(FieldKey.fromString("isOpen"), true);

        return filter;
    }
}