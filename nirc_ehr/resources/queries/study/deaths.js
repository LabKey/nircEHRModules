require("ehr/triggers").initScript(this);

var triggerHelper = new org.labkey.nirc_ehr.query.NIRC_EHRTriggerHelper(LABKEY.Security.currentUser.id, LABKEY.Security.currentContainer.id);
var validIds = [];
var idMap = {};

function onInit(event, helper){
    helper.setScriptOptions({
        requiresStatusRecalc: false,
        datasetsToClose: []
    });
    helper.decodeExtraContextProperty('deathsInTransaction');

    // Cache valid Ids for check on each row
    LABKEY.Query.selectRows({
        requiredVersion: 9.1,
        schemaName: 'study',
        queryName: 'demographics',
        columns: ['Id', 'calculated_status', 'QCState/Label'],
        scope: this,
        success: function (results) {
            if (!results || !results.rows || results.rows.length < 1)
                return;

            for(var i=0; i < results.rows.length; i++) {
                validIds.push(results.rows[i]["Id"]["value"])
                idMap[results.rows[i]["Id"]["value"]] = {calculated_status: results.rows[i]["calculated_status"]["value"], QCStateLabel: results.rows[i]["QCState/Label"]["value"]};
                console.log(idMap[results.rows[i]["Id"]["value"]]);
            }
        },
        failure: function (error) {
            console.log("error getting demographics data in death trigger onInit()\n" + error);
        }
    });
}

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.AFTER_DELETE, 'study', 'Deaths', function(helper, errors, row, oldRow) {

    var demographicsUpdates = [];
    demographicsUpdates.push({
        Id: row.Id,
        death: null
    });

    console.log('removing demographics death date for animal:' + row.Id);
    helper.getJavaHelper().updateDemographicsRecord(demographicsUpdates);
});

function onUpsert(helper, scriptErrors, row, oldRow) {

    var demographicsUpdates = [];

    console.log("row.QCStateLabel = " + row.QCStateLabel);
    console.log("EHR.Server.Security.getQCStateByLabel(row.QCStateLabel) = " +  EHR.Server.Security.getQCStateByLabel(row.QCStateLabel));

    if (!helper.isETL()) {

        console.log("idMap[row.Id]=" + idMap[row.Id]);
        if (idMap[row.Id])
        {
            EHR.Server.Utils.addError(scriptErrors, 'Id', 'Death record already exists for this animal.', 'ERROR');
        }
        // update demographics death date if finalized and not changed from existing value
        if (!helper.isValidateOnly() && row.Id && row.date && row.QCStateLabel && row.QCStateLabel === "In Progress") {

            if (validIds.indexOf(row.id) !== -1) {

                demographicsUpdates.push({
                    Id: row.Id,
                    death: row.date,
                    calculated_status: 'Dead',
                    QCStateLabel: 'Review Required'
                });

                console.log('updating demographics death date for animal: ' + row.Id);
                helper.getJavaHelper().updateDemographicsRecord(demographicsUpdates);
                console.log('updated demographics death date for animal: ' + row.Id);
            }
            else {
                console.log(row.id + " is not a valid animal id");
            }
        }
    }
}

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.AFTER_INSERT, 'study', 'deaths', function(helper, scriptErrors, row, oldRow) {
    console.log("after insert row.QCStateLabel = " + row.QCStateLabel);
    if (!helper.isETL()) {
        console.log("after insert");
        // Sending Death notification on COMPLETE event requires QC State to be "Completed".
        // (Note: EHR.Server.TriggerManager.Events.COMPLETE is called once after all the row updates/inserts have taken place)
        // Since the requirement is to send Death Notification once the Death is recorded, we are sending death notification
        // on one animal after insert instead given that there always will be one Death recorded at a time/one row insert.
        triggerHelper.sendDeathNotification(row.Id, row.date, row.taskid);
    }
});