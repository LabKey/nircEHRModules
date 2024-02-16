require("ehr/triggers").initScript(this);

function onInit(event, helper){
    helper.setScriptOptions({
        allowAnyId: true,
        requiresStatusRecalc: true,
        allowDatesInDistantPast: true
    });
}
EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_INSERT, 'study', 'arrival', function (helper, scriptErrors, row, oldRow) {
    // if born at center acq date and birth date is same (row.description contains ACQ date)
    // if brought from outside aqc date and birth date may differ

    if (row.eventDate) {
        row.date = row.eventDate;
    }
});