package org.labkey.nirc_ehr.dataentry.section;

import org.json.JSONObject;
import org.labkey.api.ehr.EHRService;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.SimpleFormSection;
import org.labkey.api.view.template.ClientDependency;

import java.util.List;

public class BaseFormSection extends SimpleFormSection
{
    private boolean _collapsible = false;
    private boolean _initCollapsed = false;
    private boolean _addCopyFromSection = false;

    public BaseFormSection(String schemaName, String queryName, String label)
    {
        this(schemaName, queryName, label, "ehr-gridpanel");
    }

    public BaseFormSection(String schemaName, String queryName, String label, String xtype, boolean collapsible, boolean initCollapsed, boolean addCopyFromSection)
    {
        this(schemaName, queryName, label, xtype, EHRService.FORM_SECTION_LOCATION.Body);

        if (!collapsible && initCollapsed)
            throw new IllegalArgumentException("Cannot set initCollapsed to true if collapsible is false");

        _collapsible = collapsible;
        _initCollapsed = initCollapsed;
        _addCopyFromSection = addCopyFromSection;
    }

    public BaseFormSection(String schemaName, String queryName, String label, String xtype)
    {
        this(schemaName, queryName, label, xtype, EHRService.FORM_SECTION_LOCATION.Body);
    }

    public BaseFormSection(String schemaName, String queryName, String label, EHRService.FORM_SECTION_LOCATION location, boolean collapsible, boolean initCollapsed)
    {
        this(schemaName, queryName, label, "ehr-gridpanel", location);

        if (!collapsible && initCollapsed)
            throw new IllegalArgumentException("Cannot set initCollapsed to true if collapsible is false");

        _collapsible = collapsible;
        _initCollapsed = initCollapsed;
    }

    public BaseFormSection(String schemaName, String queryName, String label, EHRService.FORM_SECTION_LOCATION location)
    {
        this(schemaName, queryName, label, "ehr-gridpanel", location);
    }

    public BaseFormSection(String schemaName, String queryName, String label, String xtype, EHRService.FORM_SECTION_LOCATION location, boolean collapsible, boolean initCollapsed)
    {
        this(schemaName, queryName, label, xtype, location);

        if (!collapsible && initCollapsed)
            throw new IllegalArgumentException("Cannot set initCollapsed to true if collapsible is false");

        _collapsible = collapsible;
        _initCollapsed = initCollapsed;
    }

    public BaseFormSection(String schemaName, String queryName, String label, String xtype, EHRService.FORM_SECTION_LOCATION location)
    {
        super(schemaName, queryName, label, xtype, location);
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/plugin/RowEditor.js"));
        setSupportFormSort(false);
    }

    @Override
    public List<String> getTbarButtons()
    {
        List<String> defaultButtons = super.getTbarButtons();
        if (!_addCopyFromSection)
            defaultButtons.remove("COPYFROMSECTION");

        return defaultButtons;
    }

    @Override
    public List<String> getTbarMoreActionButtons()
    {
        List<String> defaultButtons = super.getTbarMoreActionButtons();
        defaultButtons.remove("GUESSPROJECT");
//        defaultButtons.add("FORM_BULK_ADD");
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
