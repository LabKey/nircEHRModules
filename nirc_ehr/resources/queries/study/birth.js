require("ehr/triggers").initScript(this);
EHR.Server.Utils = require("ehr/utils").EHR.Server.Utils;

var triggerHelper = new org.labkey.nirc_ehr.query.NIRC_EHRTriggerHelper(LABKEY.Security.currentUser.id, LABKEY.Security.currentContainer.id);

function onInit(event, helper){
    helper.setScriptOptions({
        allowAnyId: true,
        requiresStatusRecalc: true,
        allowDeadIds: true,
        skipIdFormatCheck: true,
        skipHousingCheck: true,
        announceAllModifiedParticipants: true,
        allowDatesInDistantPast: true,
        removeTimeFromDate: true
    });

    helper.decodeExtraContextProperty('birthsInTransaction');
}

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.AFTER_DELETE, 'study', 'birth', function(helper, scriptErrors, row, oldRow) {
    if (!helper.isETL() && !helper.isValidateOnly()) {
        // cleanup on delete
        if (row.taskid){

            var deleteErrors = triggerHelper.deleteDatasetRecord('demographics', row.taskid);
            if (deleteErrors){
                EHR.Server.Utils.addError(scriptErrors, 'Id', deleteErrors, 'ERROR');
            }
            deleteErrors = triggerHelper.deleteDatasetRecord('housing', row.taskid);
            if (deleteErrors){
                EHR.Server.Utils.addError(scriptErrors, 'Id', deleteErrors, 'ERROR');
            }
        }
    }
});

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_UPSERT, 'study', 'birth', function(helper, scriptErrors, row, oldRow) {

    if (!oldRow && row.Id && triggerHelper.birthExists(row.Id)) {
        EHR.Server.Utils.addError(scriptErrors, 'Id', 'Birth record already exists for this Id, update existing record to change birth information', 'ERROR');
    }

    if (!helper.isETL()) {

        if (row.QCStateLabel) {
            row.qcstate = helper.getJavaHelper().getQCStateForLabel(row.QCStateLabel).getRowId();
        }

        if (!helper.isGeneratedByServer() && !helper.isValidateOnly()) {

            // TODO: This is currently not working since room is required and cage is not
            // //if cage also known as "location" is provided, we insert into housing.
            // if (row.cage && row.Id && row.date) {
            //     var housingRec = {
            //         Id: row.Id,
            //         date: row.date,
            //         cage: row.cage || null,
            //         taskid: row.taskid,
            //         qcstate: row.qcstate
            //     }
            //
            //     var housingErrors = triggerHelper.createBirthHousingRecord(row.Id, housingRec);
            //     if (housingErrors) {
            //         EHR.Server.Utils.addError(scriptErrors, 'Id', housingErrors, 'ERROR');
            //     }
            // }

            // this allows demographic records in qcstates other than completed
            var extraDemographicsFieldMappings = {
                'qcstate': helper.getJavaHelper().getQCStateForLabel(row.QCStateLabel).getRowId()
            }

            var obj = {
                Id: row.Id,
                date: row.date,
                calculated_status: 'Alive',
                dam: row['Id/demographics/dam'] || null,
                sire: row['Id/demographics/sire'] || null,
                species: row['Id/demographics/species'] || null,
                birth: row.date || null,
                gender: row['Id/demographics/gender'] || null,
                taskid: row.taskid,
                remark: row.remark,
                QCStateLabel: row.QCStateLabel
            };

            //find dam, if provided
            if (obj.dam && !obj.origin) {
                obj.origin = helper.getJavaHelper().getGeographicOrigin(obj.dam);
            }

            if (obj.dam && !obj.species) {
                obj.species = helper.getJavaHelper().getSpecies(obj.dam);
            }

            if (!oldRow) {
                //if not already present, we insert into demographics
                helper.getJavaHelper().createDemographicsRecord(row.Id, obj, extraDemographicsFieldMappings);
            }
            else {
                //Update demographics records
                var ar = helper.getJavaHelper().getDemographicRecord(obj.Id);
                var data = ar || {};

                var record = {};
                var hasUpdates = false;

                if (obj.gender && obj.gender !== data.gender) {
                    record.gender = obj.gender;
                    hasUpdates = true;
                }

                if (obj.species && obj.species !== data.species) {
                    record.species = obj.species;
                    hasUpdates = true;
                }

                if (obj.birth && obj.birth !== data.birth) {
                    record.birth = obj.birth;
                    hasUpdates = true;
                }

                if (obj.sire && obj.sire !== data.sire) {
                    record.sire = obj.sire;
                    hasUpdates = true;
                }

                if (obj.dam && obj.dam !== data.dam) {
                    record.dam = obj.dam;
                    hasUpdates = true;
                }

                if (obj.QCStateLabel && obj.QCStateLabel !== data.QCStateLabel) {
                    record.QCStateLabel = obj.QCStateLabel;
                    hasUpdates = true;
                }

                if (hasUpdates) {
                    console.info("Birth update for animal Id " + row.Id + " included demographic changes. Demographic record updated.");
                    record.Id = obj.Id;
                    var demographicsUpdates = [record];
                    helper.getJavaHelper().updateDemographicsRecord(demographicsUpdates);
                    helper.cacheDemographics(row.Id, row);
                }
            }
        }
    }
});