require("ehr/triggers").initScript(this);
var triggerHelper = new org.labkey.nirc_ehr.query.NIRC_EHRTriggerHelper(LABKEY.Security.currentUser.id, LABKEY.Security.currentContainer.id);

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_UPSERT, 'study', 'necropsy', function(helper, scriptErrors, row, oldRow) {


    //add/update a weight record
    // var weightRecord = {
    //     Id: row.Id,
    //     date: row.date,
    //     weight: row.weight,
    //     taskid: row.taskid,
    //     QCStateLabel: row.QCStateLabel
    // };
    // triggerHelper.upsertWeightRecord(weightRecord);

    // if (!helper.isETL()) {
    //
    //     if (row.QCStateLabel) {
    //         row.qcstate = helper.getJavaHelper().getQCStateForLabel(row.QCStateLabel).getRowId();
    //     }
    //
    //     if (!helper.isGeneratedByServer() && !helper.isValidateOnly()) {
    //
    //         // this allows demographic records in qcstates other than completed
    //         var extraDemographicsFieldMappings = {
    //             'qcstate': helper.getJavaHelper().getQCStateForLabel(row.QCStateLabel).getRowId()
    //         }
    //
    //         var weightObj = {
    //             Id: row.Id,
    //             date: row.date,
    //             weight: row['Id/MostRecentWeight/MostRecentWeight'],
    //             taskid: row.taskid,
    //             QCStateLabel: row.QCStateLabel
    //         };
    //
    //         helper.getJavaHelper().updateWeightRecord(weightObj, extraDemographicsFieldMappings);
    //         helper.cacheDemographics(row.Id, row);
    //     }
    // }
})