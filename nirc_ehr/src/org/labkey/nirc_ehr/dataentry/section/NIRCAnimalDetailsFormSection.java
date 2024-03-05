package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.ehr.dataentry.NonStoreFormSection;

public class NIRCAnimalDetailsFormSection extends NonStoreFormSection
{
    public NIRCAnimalDetailsFormSection()
    {
        this("nirc_ehr-animaldetailspanel");
    }

    public NIRCAnimalDetailsFormSection(String xtype)
    {
        super("AnimalDetails", "Animal Details", xtype);
    }
}
