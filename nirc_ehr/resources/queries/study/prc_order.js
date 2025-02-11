
require("ehr/triggers").initScript(this);

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_INSERT, 'study', 'prc_order', function (helper, scriptErrors, row, oldRow) {
    if (!helper.isValidateOnly()) {
        row.qcstate = helper.getJavaHelper().getQCStateForLabel('Request: Approved').getRowId();
        row.qcstateLabel = 'Request: Approved';
    }
});