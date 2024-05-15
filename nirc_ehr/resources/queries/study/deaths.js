require("ehr/triggers").initScript(this);

var triggerHelper = new org.labkey.nirc_ehr.query.NIRC_EHRTriggerHelper(LABKEY.Security.currentUser.id, LABKEY.Security.currentContainer.id);
var validIds = [];
var idMap = {};

function onInit(event, helper){
    helper.setScriptOptions({
        allowShippedIds: false,
        allowDeadIds: false,
        requiresStatusRecalc: false,
        datasetsToClose: ['Assignment', 'Protocol Assignment' , 'Housing']
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
                // console.log(idMap[results.rows[i]["Id"]["value"]]);
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
        death: null,
        calculated_status: 'Alive',
        QCStateLabel: 'Completed',
    });

    console.log('removing demographics death date for animal:' + row.Id);
    helper.getJavaHelper().updateDemographicsRecord(demographicsUpdates);
});

function onUpsert(helper, scriptErrors, row, oldRow) {

    var demographicsUpdates = [];

    if (!helper.isETL()) {

        //only allow death record to be created if animal is in demographics table
        if (idMap[row.Id]) {

            // check if death record already exists for this animal
            if (idMap[row.Id].calculated_status.toUpperCase() === 'DEAD' && row.QCStateLabel.toUpperCase() === 'IN PROGRESS') {
                EHR.Server.Utils.addError(scriptErrors, 'Id', 'Death record already exists for this animal.', 'ERROR');
            }
            // check if the animal is at the center
            if (idMap[row.Id].calculated_status.toUpperCase() === 'SHIPPED') {
                EHR.Server.Utils.addError(scriptErrors, 'Id', 'Animal is not at the center.', 'ERROR');
            }

            if (!helper.isValidateOnly() && row.Id && row.date && row.QCStateLabel) {

                if (validIds.indexOf(row.id) !== -1) {

                    // update demographics
                    demographicsUpdates.push({
                        Id: row.Id,
                        death: row.date,
                        calculated_status: 'Dead',
                        QCStateLabel: row.QCStateLabel,
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
}

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.AFTER_INSERT, 'study', 'deaths', function(helper, scriptErrors, row, oldRow) {
    helper.registerDeath(row.Id, row.date);
});

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.COMPLETE, 'study', 'Deaths', function(event, errors, helper){
    var deaths = helper.getDeaths();

    if (deaths) {
        var ids = [];
        for (var id in deaths){
            ids.push(id);
        }
        if (!helper.isETL() && event === 'insert') {
            triggerHelper.sendDeathNotification(ids[0]);
        }
    }
});