package org.labkey.nirc_ehr.dataentry.section;

import org.json.JSONObject;
import org.labkey.api.data.TableInfo;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.ParentFormPanelSection;
import org.labkey.api.query.FieldKey;
import org.labkey.api.view.template.ClientDependency;

import java.util.List;

public class NIRCCasesFormPanelSection extends ParentFormPanelSection
{
    private boolean isVetTech;
    private boolean isVet;
    private boolean isFolderAdmin;

    public NIRCCasesFormPanelSection(String label, boolean isVetTech, boolean isVet, boolean isFolderAdmin)
    {
        super("study", "cases", label);
        this.isVetTech = isVetTech;
        this.isVet = isVet;
        this.isFolderAdmin = isFolderAdmin;
        setSupportFormSort(false);

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/field/AnimalIdCases.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/field/EditCase.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/buttons/SelectCaseButton.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/panel/SelectCasePanel.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/window/SelectCaseWindow.js"));
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

        // only Vets and Folder Admins can see the enddate ('Close date') field to be able to close the case.
        if (!isVet && (!isFolderAdmin || isVetTech))
        {
            keys.remove(FieldKey.fromString("enddate"));
            keys.remove(FieldKey.fromString("closeRemark"));
        }

        return keys;
    }
}
