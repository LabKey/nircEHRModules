
require("ehr/triggers").initScript(this);

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_UPSERT, 'study', 'treatment_order', function(helper, scriptErrors, row, oldRow) {
    if (row.volume && !row.vol_units) {
        EHR.Server.Utils.addError(scriptErrors, 'volume', 'Units required for volume.', 'ERROR');
    }
});