require("ehr/triggers").initScript(this);

var triggerHelper = new org.labkey.nirc_ehr.query.NIRC_EHRTriggerHelper(LABKEY.Security.currentUser.id, LABKEY.Security.currentContainer.id);

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_UPSERT, 'study', 'pairings', function(helper, scriptErrors, row, oldRow) {

    if (row.cage && row.Id && row.date && !triggerHelper.validateHousing(row.Id, row.cage, row.date)){
        EHR.Server.Utils.addError(scriptErrors, 'cage', 'Not housed in this location on this date', 'INFO');
    }
});