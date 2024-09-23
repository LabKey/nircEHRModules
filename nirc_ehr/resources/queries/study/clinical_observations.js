/*
 * Copyright (c) 2024 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */

const console = require("console");
require("ehr/triggers").initScript(this);

var animalIdCasesMap = {};
var triggerHelper = new org.labkey.nirc_ehr.query.NIRC_EHRTriggerHelper(LABKEY.Security.currentUser.id, LABKEY.Security.currentContainer.id);


function onUpsert(helper, scriptErrors, row, oldRow) {

    if (!helper.isETL() && !helper.isGeneratedByServer()) {

        if (!row.observation && !row.remark) {
            EHR.Server.Utils.addError(scriptErrors, 'observation', 'Must enter an observation or remark', 'WARN');
            EHR.Server.Utils.addError(scriptErrors, 'remark', 'Must enter an observation or remark', 'WARN');
        }

        if (row.category === "Verified Id?" && row.observation === "No" && !row.remark) {
            EHR.Server.Utils.addError(scriptErrors, 'remark', "You selected 'No' for 'Verified Id?', please enter Remark", "WARN");
        }

        // Cases propagate observations to other cases
        if (!helper.isValidateOnly() && row.caseid) {
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
                row.orderId = triggerHelper.propagateClinicalObs(row, qc.RowId); //Add clinical obs for all open cases associated with an animal
            }
        }
    }
}