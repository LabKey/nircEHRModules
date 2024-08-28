package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.data.TableInfo;
import org.labkey.api.query.FieldKey;

import java.util.List;

public class NIRCClinicalRemarksFormSection extends BaseFormSection
{
    public static final String LABEL = "Clinical Remarks";
    private boolean isVetTech;
    private boolean isVet;
    private boolean isFolderAdmin;

    public NIRCClinicalRemarksFormSection(boolean isVetTech, boolean isVet, boolean isFolderAdmin)
    {
        super("study", "clinremarks", LABEL, "ehr-gridpanel", true, true, true);
        this.isVetTech = isVetTech;
        this.isVet = isVet;
        this.isFolderAdmin = isFolderAdmin;
    }

    @Override
    protected List<FieldKey> getFieldKeys(TableInfo ti)
    {
        List<FieldKey> keys = super.getFieldKeys(ti);

        // only Vets and Folder Admins S.O.A.P fields.
        if (!isVet && (!isFolderAdmin || isVetTech))
        {
            keys.remove(FieldKey.fromString("s"));
            keys.remove(FieldKey.fromString("o"));
            keys.remove(FieldKey.fromString("a"));
            keys.remove(FieldKey.fromString("p"));
        }

        return keys;
    }
}
