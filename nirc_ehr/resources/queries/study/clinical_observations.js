/*
 * Copyright (c) 2024 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */

const console = require("console");
require("ehr/triggers").initScript(this);

var animalIdCasesMap = {};
var triggerHelper = new org.labkey.nirc_ehr.query.NIRC_EHRTriggerHelper(LABKEY.Security.currentUser.id, LABKEY.Security.currentContainer.id);

function onInit(event, helper) {
    helper.decodeExtraContextProperty('orderTasksInTransaction');
}

function onUpsert(helper, scriptErrors, row, oldRow) {

    if (!helper.isETL() && !helper.isGeneratedByServer()) {

        if (!row.observation && !row.remark) {
            EHR.Server.Utils.addError(scriptErrors, 'observation', 'Must enter an observation or remark', 'WARN');
            EHR.Server.Utils.addError(scriptErrors, 'remark', 'Must enter an observation or remark', 'WARN');
        }

        if (row.category === "Verified Id?" && row.observation === "No" && !row.remark) {
            EHR.Server.Utils.addError(scriptErrors, 'remark', "You selected 'No' for 'Verified Id?', please enter Remark", "WARN");
        }

        // Handle scheduled observations
        if (!helper.isValidateOnly() && row.scheduledDate) {
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
            else {
                var orderTasks = helper.getProperty('orderTasksInTransaction');
                if (orderTasks && orderTasks.length > 0) {
                    var orderData = triggerHelper.handleScheduledObservations(row, qc.RowId, orderTasks[0]);

                    if (orderData) {
                        row.caseid = orderData.caseId;
                        row.orderid = orderData.orderId;
                        row.area = orderData.area;
                    }
                }
            }
        }
    }
}