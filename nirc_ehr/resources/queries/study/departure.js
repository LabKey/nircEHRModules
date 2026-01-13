require("ehr/triggers").initScript(this);

var triggerHelper = new org.labkey.nirc_ehr.query.NIRC_EHRTriggerHelper(LABKEY.Security.currentUser.id, LABKEY.Security.currentContainer.id);
var departures = [];

function onInit(event, helper){
    helper.setScriptOptions({
        requiresStatusRecalc: false
    });

}

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.AFTER_INSERT, 'study', 'departure', function(helper, scriptErrors, row, oldRow) {

    if (row.id) {
        departures.push(row.id);
    }
});

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.COMPLETE, 'study', 'departure', function(event, errors, helper){

    if (!helper.isETL() && helper.isEHRDataEntry()) {
        triggerHelper.updateProcedureOrdersToCompleted(departures);
    }
});

