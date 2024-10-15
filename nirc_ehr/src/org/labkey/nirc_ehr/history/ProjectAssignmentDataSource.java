package org.labkey.nirc_ehr.history;

import org.labkey.api.ehr.history.DefaultAssignmentDataSource;
import org.labkey.api.module.Module;

public class ProjectAssignmentDataSource extends DefaultAssignmentDataSource
{
    public ProjectAssignmentDataSource(Module module)
    {
        super(module);
        setShowTime(true);
        setCategoryText("Project Assignment");
        setPrimaryGroup("Project Assignments");
    }
}
