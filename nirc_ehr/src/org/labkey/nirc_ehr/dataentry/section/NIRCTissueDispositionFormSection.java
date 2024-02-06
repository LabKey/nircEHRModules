package org.labkey.nirc_ehr.dataentry.section;

import java.util.List;

public class NIRCTissueDispositionFormSection extends BaseFormSection
{
    public NIRCTissueDispositionFormSection()
    {
        super("study", "tissueDisposition", "Tissue Disposition", "ehr-gridpanel", true, true);
    }

    @Override
    public List<String> getTbarButtons()
    {
        List<String> defaults = super.getTbarButtons();
        defaults.remove("COPYFROMSECTION");
        defaults.remove("ADDANIMALS");
        defaults.remove("TEMPLATE");
        return defaults;
    }

    @Override
    public List<String> getTbarMoreActionButtons()
    {
        List<String> defaultButtons = super.getTbarMoreActionButtons();
        defaultButtons.remove("GUESSPROJECT");
        defaultButtons.remove("COPY_IDS");
        return defaultButtons;
    }
}
