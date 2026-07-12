require("ehr/triggers").initScript(this);

var triggerHelper = new org.labkey.nirc_ehr.query.NIRC_EHRTriggerHelper(LABKEY.Security.currentUser.id, LABKEY.Security.currentContainer.id);
var departures = [];

function onInit(event, helper){
    helper.setScriptOptions({
        requiresStatusRecalc: false
    });

}

function onUpsert(helper, scriptErrors, row, oldRow) {

    if (!helper.isETL()) {

        // Do not allow a departure record to be completed while other data for this animal is still in 'Review Required' state.
        // Records belonging to this departure's own task are excluded, since they move to Completed in the same save.
        if (row.QCStateLabel && row.QCStateLabel.toUpperCase() === 'COMPLETED') {
            var reviewRequiredDatasets = triggerHelper.getReviewRequiredDatasets(row.Id, row.taskid || null);
            if (reviewRequiredDatasets) {
                EHR.Server.Utils.addError(scriptErrors, 'Id', 'Departure record cannot be completed. There is still data in Review Required state for this animal in the following dataset(s): ' + reviewRequiredDatasets, 'ERROR');
            }
        }
    }
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

