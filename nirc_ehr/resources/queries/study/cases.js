/*
 * Copyright (c) 2012-2019 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */

const console = require("console");
require("ehr/triggers").initScript(this);

var triggerHelper = new org.labkey.nirc_ehr.query.NIRC_EHRTriggerHelper(LABKEY.Security.currentUser.id, LABKEY.Security.currentContainer.id);

function onInit(event, helper) {
    helper.decodeExtraContextProperty('ordersInTransaction');
}

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_UPSERT, 'study', 'cases', function(helper, errors, row, oldRow){
    if (!helper.isETL()) {
        if (row.enddate && !triggerHelper.canCloseCase(row.category)) {
                EHR.Server.Utils.addError(errors, 'enddate', 'Veterinarian permission required to close a case.', 'ERROR');
        }

        if (!helper.isValidateOnly() && row.caseid && row.enddate && (row.enddate != oldRow.enddate)) {
            triggerHelper.closeDailyClinicalObs(row.caseid, row.enddate);
        }
    }
});

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.COMPLETE, 'study', 'cases', function(event, errors, helper){
    if (!helper.isETL()) {
        var rows = helper.getRows();
        if (helper.getEvent() == 'insert' && rows.length && !helper.isValidateOnly()) {
            for (var i = 0; i < rows.length; i++) {
                var row = rows[i].row;
                var qc;
                if (row.QCStateLabel) {
                    qc = EHR.Server.Security.getQCStateByLabel(row.QCStateLabel);
                }
                else if (row.QCState) {
                    qc = EHR.Server.Security.getQCStateByRowId(row.QCState);
                }

                if (!qc) {
                    console.error('Unable to find QCState: ' + row.QCState + '/' + row.QCStateLabel);
                }
                else if (qc.Label != 'COMPLETED' && row.caseid && row.Id && qc) {
                    var ordersInTransaction = helper.getProperty('ordersInTransaction');
                    var oit = [];
                    if (ordersInTransaction && ordersInTransaction.length) {
                        oit = ordersInTransaction;
                    }
                    triggerHelper.ensureDailyClinicalObservationOrders(row.Id, row.caseid, row.performedby, qc.RowId, row.taskid, oit);
                }
            }
        }
    }
});
