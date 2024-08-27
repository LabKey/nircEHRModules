/*
 * Copyright (c) 2012-2019 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */

require("ehr/triggers").initScript(this);

var triggerHelper = new org.labkey.nirc_ehr.query.NIRC_EHRTriggerHelper(LABKEY.Security.currentUser.id, LABKEY.Security.currentContainer.id);

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_UPSERT, 'study', 'cases', function(helper, errors, row, oldRow){
    if (!helper.isETL()) {
        if (row.enddate && !triggerHelper.canCloseCase(row.category)) {
                EHR.Server.Utils.addError(errors, 'enddate', 'Veterinarian permission required to close a case.', 'ERROR');
        }
    }
});
