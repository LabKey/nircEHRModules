require("ehr/triggers").initScript(this);
var console = require("console");
var LABKEY = require("labkey");


var prevRow;

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.INIT, 'nirc_ehr', 'casesTemp', function(event, helper) {
    // Get boundary rows for ETL batches. Trigger will lose scope between batches so need to load up previous row from
    // prior batch.
    if (helper.isETL()) {
        LABKEY.Query.executeSql({
            schemaName: 'nirc_ehr',
            sql: "SELECT Id, date, category FROM nirc_ehr.casesTemp ORDER BY objectid DESC LIMIT 1",
            maxRows: 1,
            scope: this,
            failure: LABKEY.Utils.getCallbackWrapper(function (response) {
                if (response && response.errors) {
                    console.log("Error querying boundary case: " + response.errors);
                }
            }),
            success: function (results) {
                if (results.rows && results.rows.length > 0) {

                    var resultRow = results.rows[0];
                    prevRow = {};
                    prevRow.category = resultRow.category;
                    prevRow.Id = resultRow.Id;
                    prevRow.date = resultRow.date;
                }
            }
        });
    }
});

function getEnddate(row, enddate){
    var closeDate = new Date(enddate);

    if (row.enddate) {
        var deathOrDep = new Date(row.enddate);

        if (deathOrDep < closeDate)
            return deathOrDep;
    }
    return EHR.Server.Utils.datetimeToString(closeDate);
}

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_INSERT, 'nirc_ehr', 'casesTemp', function (helper, scriptErrors, row, oldRow) {

    // Align resolved cases with case openings
    if (helper.isETL()) {
        if (prevRow && prevRow.Id == row.Id) {
            if (row.category == "Presenting Diagnosis" && prevRow.category == "Presenting Diagnosis") {
                row.openRemark = "Error: Consecutive Presenting Diagnosis"
            }
            else if (row.category == "Clinical Resolution" && prevRow.category == "Clinical Resolution") {
                row.openRemark = "Error: Consecutive Clinical Resolution"
            }
            else if (row.category == "Presenting Diagnosis") {
                row.closeRemark = prevRow.closeRemark || null;
                row.closeDiagnosis = prevRow.closeDiagnosis || null;
                row.enddate = getEnddate(row, prevRow.date);
            }
        }

        prevRow = row;
    }
});