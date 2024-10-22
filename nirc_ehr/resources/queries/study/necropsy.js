require("ehr/triggers").initScript(this);
var triggerHelper = new org.labkey.nirc_ehr.query.NIRC_EHRTriggerHelper(LABKEY.Security.currentUser.id, LABKEY.Security.currentContainer.id);

var deathIdMap = {};

function onInit(event, helper){

    if (!helper.isETL() && event != 'truncate') {
        LABKEY.Query.selectRows({
            requiredVersion: 9.1,
            schemaName: 'study',
            queryName: 'deaths',
            columns: ['Id', 'QCState/Label'],
            scope: this,
            success: function (results) {
                if (!results || !results.rows || results.rows.length < 1)
                    return;

                for (var i = 0; i < results.rows.length; i++) {
                    deathIdMap[results.rows[i]["Id"]["value"]] = {QCStateLabel: results.rows[i]["QCState/Label"]["value"]};
                }
            },
            failure: function (error) {
                console.log("error getting death data in death trigger onInit()\n" + error);
            }
        });
    }
}

function onUpsert(helper, scriptErrors, row, oldRow) {

    if (!helper.isETL()) {

        if (deathIdMap[row.Id] && deathIdMap[row.Id].QCStateLabel &&
                (deathIdMap[row.Id].QCStateLabel.toUpperCase() === 'IN PROGRESS' ||
                deathIdMap[row.Id].QCStateLabel.toUpperCase() === 'REQUEST: PENDING' ||
                deathIdMap[row.Id].QCStateLabel.toUpperCase() === 'REVIEW REQUIRED')) {

            if (!row.examReason)
                EHR.Server.Utils.addError(scriptErrors, 'examReason', "'Reason for Examination' is required", 'ERROR');
            if (!row.specimenCondition)
                EHR.Server.Utils.addError(scriptErrors, 'specimenCondition', "'Condition of Specimen' is required", 'ERROR');
            if (!row.physicalCondition)
                EHR.Server.Utils.addError(scriptErrors, 'physicalCondition', "'Physical Condition' is required", 'ERROR');
            if (!row.diagnosis)
                EHR.Server.Utils.addError(scriptErrors, 'diagnosis', "'Diagnosis' is required", 'ERROR');
        }

        if(row.QCStateLabel && EHR.Server.Security.getQCStateByLabel(row.QCStateLabel).PublicData) {
            var qcstate = helper.getJavaHelper().getQCStateForLabel(row.QCStateLabel).getRowId();

            //add/update weight record
            var weightRecord = {
                Id: row.Id,
                date: row.date,
                weight: row.necropsyWeight,
                taskid: row.taskid,
                qcstate: qcstate
            };
            triggerHelper.upsertWeightRecord(weightRecord);
        }
    }
}