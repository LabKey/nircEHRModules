require("ehr/triggers").initScript(this);
var triggerHelper = new org.labkey.nirc_ehr.query.NIRC_EHRTriggerHelper(LABKEY.Security.currentUser.id, LABKEY.Security.currentContainer.id);

function onUpsert(helper, scriptErrors, row, oldRow) {

    if (!helper.isETL()) {

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