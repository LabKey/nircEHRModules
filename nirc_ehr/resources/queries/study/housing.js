require("ehr/triggers").initScript(this);
var prevAnimalId;
var prevDate;

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_INSERT, 'study', 'housing', function (helper, scriptErrors, row, oldRow) {

    var isTransfer = prevAnimalId === row.Id;

    if (isTransfer) {
        row.endDate = prevDate;
    }

    if (row.endDate === 'undefined') {
        console.log("end date not found for animal event - " + row.animalEventId);
    }

    prevAnimalId = row.Id;
    prevDate = row.date;

});


