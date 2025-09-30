package org.labkey.nirc_ehr.table;

import org.jetbrains.annotations.Nullable;
import org.labkey.api.data.Container;
import org.labkey.api.data.TableInfo;
import org.labkey.api.data.triggers.Trigger;
import org.labkey.api.query.ValidationException;
import org.labkey.api.security.User;

import java.util.Map;

/**
 * Shared dataset trigger to add triggers to act on all the study datasets.
 */
public class NIRC_EHRSharedDatasetTrigger implements Trigger
{
    private void transformAnimalIdToUpperCase(Map<String, Object> row)
    {
        if (row != null && row.containsKey("Id") && row.get("Id") != null)
        {
            row.put("Id", ((String) row.get("Id")).toUpperCase());
        }
    }

    @Override
    public void beforeInsert(TableInfo table, Container c, User user, @Nullable Map<String, Object> newRow, ValidationException errors, Map<String, Object> extraContext) throws ValidationException
    {
        transformAnimalIdToUpperCase(newRow);
        if (newRow != null && newRow.containsKey("performedby") && newRow.get("performedby") == null)
        {
            if (newRow.containsKey("QCStateLabel") && newRow.get("QCStateLabel").equals("Completed"))
                errors.addFieldError("performedby", "Performed by must be entered in all " + table.getTitle() + " records before submitting final.");
        }
    }

    @Override
    public void beforeUpdate(TableInfo table, Container c,
                             User user, @Nullable Map<String, Object> newRow, @Nullable Map<String, Object> oldRow,
                             ValidationException errors, Map<String, Object> extraContext) throws ValidationException
    {
        if (newRow != null && newRow.containsKey("performedby") && newRow.get("performedby") == null)
        {
            if (newRow.containsKey("QCStateLabel") && newRow.get("QCStateLabel").equals("Completed"))
                errors.addFieldError("performedby", "Performed by must be entered in all " + table.getTitle() + " records before submitting final.");
        }
    }
}
