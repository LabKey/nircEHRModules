package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.ehr.dataentry.AbstractFormSection;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormElement;
import org.labkey.api.view.template.ClientDependency;

import java.util.Collections;
import java.util.List;

public class NIRCDocumentArchiveFormSection extends AbstractFormSection
{
    public NIRCDocumentArchiveFormSection()
    {
        super("ArrivalDocumentArchiveInstructions", "Document Archive", "nirc_ehr-documentarchivepanel");

        addClientDependency(ClientDependency.supplierFromPath("ehr/panel/DocumentArchivePanel.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/panel/DocumentArchivePanel.js"));
    }

    @Override
    protected List<FormElement> getFormElements(DataEntryFormContext ctx)
    {
        return Collections.emptyList();
    }
}