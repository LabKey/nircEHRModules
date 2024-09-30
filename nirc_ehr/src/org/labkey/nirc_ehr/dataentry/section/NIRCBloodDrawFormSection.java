package org.labkey.nirc_ehr.dataentry.section;

import org.json.JSONObject;
import org.labkey.api.ehr.EHRService;
import org.labkey.api.ehr.dataentry.BloodDrawFormSection;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.view.template.ClientDependency;

import java.util.List;

public class NIRCBloodDrawFormSection extends BloodDrawFormSection
{
    private boolean _collapsible = false;
    private boolean _initCollapsed = false;
    private boolean _addCopyFromSection = false;

    public NIRCBloodDrawFormSection(boolean collapsible, boolean initCollapsed, boolean addCopyFromSection)
    {
        super(false, EHRService.FORM_SECTION_LOCATION.Body);

        if (!collapsible && initCollapsed)
            throw new IllegalArgumentException("Cannot set initCollapsed to true if collapsible is false");

        _collapsible = collapsible;
        _initCollapsed = initCollapsed;
        _addCopyFromSection = addCopyFromSection;
    }

    public NIRCBloodDrawFormSection(boolean collapsible, boolean initCollapsed, boolean addCopyFromSection, boolean isChild, String parentQueryName)
    {
        this(collapsible, initCollapsed, addCopyFromSection);
        if (isChild)
        {
            addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/ParentChild.js"));
            addConfigSource("ParentChild");

            addClientDependency(ClientDependency.supplierFromPath("ehr/data/ChildClientStore.js"));
            setClientStoreClass("EHR.data.ChildClientStore");
            addExtraProperty("parentQueryName", parentQueryName);
        }
    }

    public List<String> getTbarButtons()
    {
        List<String> defaultButtons = super.getTbarButtons();

        int idx = defaultButtons.indexOf("ADDANIMALS");
        if (idx > -1)
        {
            defaultButtons.remove(idx);
            defaultButtons.add(idx, "NIRC_ADDANIMALS");
        }

        idx = defaultButtons.indexOf("COPYFROMSECTION");
        if (idx > -1)
        {
            if (!_addCopyFromSection)
                defaultButtons.remove(idx);
            else
            {
                defaultButtons.remove(idx);
                defaultButtons.add(idx, "NIRCCOPYFROMSECTION");
            }
        }

        return defaultButtons;
    }

    @Override
    public JSONObject toJSON(DataEntryFormContext ctx, boolean includeFormElements)
    {
        JSONObject json = super.toJSON(ctx, includeFormElements);
        json.put("collapsible", _collapsible);
        json.put("initCollapsed", _initCollapsed);
        json.put("dataDependentCollapseHeader", true);
        return json;
    }
}
