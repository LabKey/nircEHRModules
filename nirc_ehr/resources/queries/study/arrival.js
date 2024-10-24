require("ehr/triggers").initScript(this);

var triggerHelper = new org.labkey.nirc_ehr.query.NIRC_EHRTriggerHelper(LABKEY.Security.currentUser.id, LABKEY.Security.currentContainer.id);

function onInit(event, helper){
    helper.setScriptOptions({
        allowAnyId: true,
        requiresStatusRecalc: true,
        allowDatesInDistantPast: true,
        skipAssignmentCheck: true,
    });
}
EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_UPSERT, 'study', 'Arrival', function(helper, scriptErrors, row, oldRow) {

    if (row.eventDate) {
        row.date = row.eventDate;
    }

    helper.registerArrival(row.Id, row.date);

    //Insert or update demographic and birth records
    if (!helper.isETL() && !helper.isGeneratedByServer() && !helper.isValidateOnly()) {

        // this allows demographic records in qcstates other than completed
        var extraDemographicsFieldMappings = {
            'taskid': row.taskid,
            'qcstate': helper.getJavaHelper().getQCStateForLabel(row.QCStateLabel).getRowId()
        }

        // null (not undefined) required for call to java trigger helper
        row.dam = row['Id/demographics/dam'] || null;
        row.sire = row['Id/demographics/sire'] || null;
        row.species = row['Id/demographics/species'] || null;
        row.birth = row['Id/demographics/birth'] || null;
        row.gender = row['Id/demographics/gender'] || null;
        row.geographic_origin = row['Id/demographics/geographic_origin'] || null;
        row.source = row.sourceFacility || null;

        if (row.QCStateLabel) {
            row.qcstate = helper.getJavaHelper().getQCStateForLabel(row.QCStateLabel).getRowId();
        }

        if (row.birth) {
            var birthInfo = {
                Id: row.Id,
                date: row.birth,
                qcstate: row.qcstate,
                taskid: row.taskid
            }

            var birthErrors = triggerHelper.saveBirthRecord(row.Id, birthInfo);
            if (birthErrors){
                EHR.Server.Utils.addError(scriptErrors, 'birth', birthErrors, 'ERROR');
            }
        }

        if (row.Id && row.date) {

            let assignmentRec = {
                Id: row.Id,
                date: row.date,
                taskid: row.taskid,
                remark: row.remark,
                qcstate: row.qcstate
            }

            if (row.project) {
                assignmentRec['project'] = row.project;
                triggerHelper.createAssignmentRecord("assignment", row.Id, assignmentRec);
            }

            if (row.arrivalProtocol) {
                assignmentRec['protocol'] = row.arrivalProtocol;
                triggerHelper.createAssignmentRecord("protocolAssignment", row.Id, assignmentRec);
            }
        }

        // if 'cage', labeled as "Initial Location" is provided, then insert into housing.
        if (row.cage && row.Id && row.date) {
            var housingRec = {
                Id: row.Id,
                date: row.date,
                cage: row.cage,
                taskid: row.taskid,
                qcstate: row.qcstate,
                reason: row.arrivalType
            }

            var housingErrors = triggerHelper.createHousingRecord(row.Id, housingRec, "arrival");
            if (housingErrors) {
                EHR.Server.Utils.addError(scriptErrors, 'Id', housingErrors, 'ERROR');
            }
        }

        if(!oldRow) {
            //if not already present, insert into demographics
            helper.getJavaHelper().createDemographicsRecord(row.Id, row, extraDemographicsFieldMappings);
        }
        else {
            //Update demographics records
            var ar = helper.getJavaHelper().getDemographicRecord(row.id);
            var data = ar || {};

            var obj = {};
            var hasUpdates = false;

            if (row.gender && row.gender !== data.gender )
            {
                obj.gender = row.gender;
                hasUpdates = true;
            }

            if (row.species && row.species !== data.species )
            {
                obj.species = row.species;
                hasUpdates = true;
            }

            if (row.geographic_origin && row.geographic_origin !== data.geographic_origin )
            {
                obj.geographic_origin = row.geographic_origin;
                hasUpdates = true;
            }

            if (row.birth && row.birth !== data.birth)
            {
                obj.birth = row.birth;
                hasUpdates = true;
            }

            if (row.sire && row.sire !== data.sire)
            {
                obj.sire = row.sire;
                hasUpdates = true;
            }

            if (row.dam && row.dam !== data.dam)
            {
                obj.dam = row.dam;
                hasUpdates = true;
            }

            if (row.QCStateLabel && row.QCStateLabel !== data.QCStateLabel)
            {
                obj.QCStateLabel = row.QCStateLabel;
                hasUpdates = true;
            }

            if (hasUpdates)
            {
                console.info("Arrival update for animal Id " + row.Id + " included demographic changes Demographic record updated.");
                obj.Id = row.Id;
                var demographicsUpdates = [obj];
                helper.getJavaHelper().updateDemographicsRecord(demographicsUpdates);
                helper.cacheDemographics(row.Id, row);
            }
        }
    }
});