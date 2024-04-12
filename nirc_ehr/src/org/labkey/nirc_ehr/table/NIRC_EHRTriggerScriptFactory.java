package org.labkey.nirc_ehr.table;

import org.jetbrains.annotations.NotNull;
import org.labkey.api.data.Container;
import org.labkey.api.data.TableInfo;
import org.labkey.api.data.triggers.ScriptTriggerFactory;
import org.labkey.api.data.triggers.Trigger;

import javax.script.ScriptException;
import java.util.Collection;
import java.util.Collections;

public class NIRC_EHRTriggerScriptFactory extends ScriptTriggerFactory
{
    @Override
    @NotNull
    protected Collection<Trigger> createTriggerScript(Container c, TableInfo table) throws ScriptException
    {
        return Collections.singleton(new NIRC_EHRSharedDatasetTrigger());
    }
}
