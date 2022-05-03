require("ehr/triggers").initScript(this);
var prevAnimalId;
var prevDate;

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_INSERT, 'study', 'housing', function (helper, scriptErrors, row, oldRow) {

        if (helper.isETL()) {
            var isBatch = prevAnimalId === row.Id;

            if (isBatch) {
                row.endDate = prevDate;
            }

            prevAnimalId = row.Id;
            prevDate = row.date;
        }
    });

function onComplete(event, errors, helper){

    // Similar to EHR housing trigger onComplete with minor differences to handle ETL
    if (!helper.isValidateOnly()) {
        var updateRows = helper.getRows();
        if (updateRows && updateRows.length > 0) {
            // Only need boundary rows for batch. Changing batch size on ETL may require change here. Incremental updates
            // should update all incoming rows so don't do this.
            if (helper.isETL() && updateRows.length > 4000) {
                console.log("Batch import: only closing boundary housing record")
                updateRows = [updateRows[0]];
            }
            var idsToClose = [];
            for (var i = 0; i < updateRows.length; i++) {
                if (EHR.Server.Security.getQCStateByLabel(updateRows[i].row.QCStateLabel).PublicData && updateRows[i].row.date) {
                    updateRows[i].row.date.setHours(12);  // Necessary to clear EHR warning
                    idsToClose.push({
                        Id: updateRows[i].row.Id,
                        date: EHR.Server.Utils.datetimeToString(updateRows[i].row.date),  //stringify to serialize properly
                        objectid: updateRows[i].row.objectid
                    });
                }
            }
            if (idsToClose.length){
                helper.getJavaHelper().closeHousingRecords(idsToClose);
            }
        }
    }
};


