
var console = require("console");
var LABKEY = require("labkey");

// For more complicated logic, I highly recommend implementing this in java.  This provides much better debugging, and the full use of the server-side java APIs
// Methods on this TriggerHelper can be called from JS, and can return values to the JS code.  In general, I try to only pass primitives; however, that may not always be required

// NOTE: NIRC specific script options should be set here in INIT triggers

exports.init = function (EHR) {


    EHR.Server.TriggerManager.registerHandler(EHR.Server.TriggerManager.Events.INIT, function (event, helper, EHR) {

        EHR.Server.TriggerManager.unregisterAllHandlersForQueryNameAndEvent('ehr_lookups', 'cage', EHR.Server.TriggerManager.Events.BEFORE_UPSERT);
        EHR.Server.TriggerManager.unregisterAllHandlersForQueryNameAndEvent('study', 'assignment', EHR.Server.TriggerManager.Events.BEFORE_UPSERT);
    });

    EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.INIT, 'study', 'assignment', function(event, helper) {
        helper.setScriptOptions({
            allowAnyId: false,
            requiresStatusRecalc: true,
            allowDatesInDistantPast: true,
            skipAssignmentCheck: true,
            removeTimeFromDate: false,
            doStandardProtocolCountValidation: false
        });

        helper.decodeExtraContextProperty('assignmentsInTransaction', []);

        helper.registerRowProcessor(function(helper, row){
            if (!row)
                return;

            if (!row.Id || !row.project){
                return;
            }

            var assignmentsInTransaction = helper.getProperty('assignmentsInTransaction');
            assignmentsInTransaction = assignmentsInTransaction || [];

            var shouldAdd = true;
            if (row.objectid){
                LABKEY.ExtAdapter.each(assignmentsInTransaction, function(r){
                    if (r.objectid == row.objectid){
                        shouldAdd = false;
                        return false;
                    }
                }, this);
            }

            if (shouldAdd){
                assignmentsInTransaction.push({
                    Id: row.Id,
                    objectid: row.objectid,
                    date: row.date,
                    enddate: row.enddate,
                    qcstate: row.QCState,
                    project: row.project
                });
            }

            helper.setProperty('assignmentsInTransaction', assignmentsInTransaction);
        });
    });

    EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.INIT, 'study', 'alias', function(event, helper) {
        helper.setScriptOptions({
            allowDatesInDistantPast: true,
            removeTimeFromDate: false,
        });
    });

    EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.INIT, 'study', 'deaths', function(event, helper) {
        helper.setScriptOptions({
            datasetsToClose: ['Assignment', 'Protocol Assignments' , 'Housing']
        });
    });
}
