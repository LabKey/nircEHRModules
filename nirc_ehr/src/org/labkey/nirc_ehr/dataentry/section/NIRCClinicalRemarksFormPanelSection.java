package org.labkey.nirc_ehr.dataentry.section;

import org.json.JSONObject;
import org.labkey.api.data.TableInfo;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.ParentFormPanelSection;
import org.labkey.api.query.FieldKey;
import org.labkey.api.view.template.ClientDependency;

import java.util.List;

public class NIRCClinicalRemarksFormPanelSection extends ParentFormPanelSection
{
    private boolean isVetTech;
    private boolean isVet;
    private boolean isFolderAdmin;

    public NIRCClinicalRemarksFormPanelSection(String label)
    {
        super("study", "clinremarks", label);
        setSupportFormSort(false);
    }

    public NIRCClinicalRemarksFormPanelSection(boolean isChild, String parentQueryName, String label, boolean isVetTech, boolean isVet, boolean isFolderAdmin)
    {
        this(label);
        this.isVetTech = isVetTech;
        this.isVet = isVet;
        this.isFolderAdmin = isFolderAdmin;
        if (isChild)
        {
            addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/ParentChild.js"));
            addConfigSource("ParentChild");

            addClientDependency(ClientDependency.supplierFromPath("ehr/data/ChildClientStore.js"));
            setClientStoreClass("EHR.data.ChildClientStore");
            addExtraProperty("parentQueryName", parentQueryName);
        }

    }

    @Override
    public JSONObject toJSON(DataEntryFormContext ctx, boolean includeFormElements)
    {
        JSONObject json = super.toJSON(ctx, includeFormElements);
        json.put("collapsible", true);
        json.put("initCollapsed", false);
        json.put("dataDependentCollapseHeader", true);
        return json;
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
