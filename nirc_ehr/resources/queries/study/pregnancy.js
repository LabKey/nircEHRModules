require("ehr/triggers").initScript(this);

var triggerHelper = new org.labkey.nirc_ehr.query.NIRC_EHRTriggerHelper(LABKEY.Security.currentUser.id, LABKEY.Security.currentContainer.id);

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.ON_BECOME_PUBLIC, 'study', 'pregnancy', function(scriptErrors, helper, row, oldRow) {
    if (!helper.isETL()) {

        var outcomeRec = {
            Id: row.Id,
            date: row.date,
            result: row.result
        }
        triggerHelper.sendPregnancyOutcomeNotification(row.Id, outcomeRec);
    }
});