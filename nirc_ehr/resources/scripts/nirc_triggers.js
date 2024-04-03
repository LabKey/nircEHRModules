
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
            allowAnyId: true,
            requiresStatusRecalc: true,
            allowDatesInDistantPast: true,
            skipAssignmentCheck: true,
            removeTimeFromDate: false,
            doStandardProtocolCountValidation: false
        });
    });
}
