require("ehr/triggers").initScript(this);
var prevAnimalId;
var prevDate;

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_INSERT, 'study', 'housing', function (helper, scriptErrors, row, oldRow) {

    if (helper.isETL()) {
        var isTransfer = prevAnimalId === row.Id;

        if (isTransfer) {
            row.endDate = prevDate;
        }
    }

        if (row.endDate === 'undefined') {
            console.log("end date not found for animal event - " + row.objectId);
        }

        prevAnimalId = row.Id;
        prevDate = row.date;
    });

function onComplete(event, errors, helper){

    // Similar to EHR housing trigger onComplete with minor differences to handle ETL
    var idsToClose = [];
    if (helper.isETL()) {
        var boundaryRows = helper.getRows();
        if (boundaryRows && boundaryRows.length > 0) {
            boundaryRows = [boundaryRows[0]];  // Just need first boundary row for this extra step
            for (var i = 0; i < boundaryRows.length; i++) {
                if (EHR.Server.Security.getQCStateByLabel(boundaryRows[i].row.QCStateLabel).PublicData && boundaryRows[i].row.date) {
                    boundaryRows[i].row.date.setHours(12);  // This just prevents warnings in EHR
                    idsToClose.push({
                        Id: boundaryRows[i].row.Id,
                        date: EHR.Server.Utils.datetimeToString(boundaryRows[i].row.date),  //stringify to serialize properly
                        objectid: boundaryRows[i].row.objectid
                    });
                }
            }
        }
    }

    else if (!helper.isValidateOnly()) {
        var housingRows = helper.getRows();
        if (housingRows && housingRows.length > 0) {
            console.log("count: " + housingRows.length);
            housingRows = [housingRows[0]]
            for (var i = 0; i < housingRows.length; i++) {
                console.log("id: " + housingRows[i].row.Id);
                console.log("date: " + EHR.Server.Utils.datetimeToString(housingRows[i].row.date))
                if (EHR.Server.Security.getQCStateByLabel(housingRows[i].row.QCStateLabel).PublicData && housingRows[i].row.date) {
                    housingRows[i].row.date.setHours(12);
                    idsToClose.push({
                        Id: housingRows[i].row.Id,
                        date: EHR.Server.Utils.datetimeToString(housingRows[i].row.date),  //stringify to serialize properly
                        objectid: housingRows[i].row.objectid
                    });
                }
            }
        }
    }

    if (idsToClose.length){
        helper.getJavaHelper().closeHousingRecords(idsToClose);
    }
};


