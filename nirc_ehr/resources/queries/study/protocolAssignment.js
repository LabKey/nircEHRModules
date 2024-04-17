require("ehr/triggers").initScript(this);
var protocolData = {};
var prevAnimalId;
var prevDate;

var missing = [];

var count = 0;

var triggerHelper = new org.labkey.nirc_ehr.query.NIRC_EHRTriggerHelper(LABKEY.Security.currentUser.id, LABKEY.Security.currentContainer.id);

function getLastAssignment(id){
    var batchLastDate;

    LABKEY.Query.selectRows({
        schemaName: 'study',
        queryName: 'protocolAssignment',
        columns: 'Id,date',
        filterArray: [LABKEY.Filter.create('Id', id)],
        sort: '-date',
        success: function (results) {
            if (results.rows.length) {
                batchLastDate = results.rows[0].date;
            }
        },
        scope: this
    });

    return batchLastDate;
}

function onInit(event, helper){
    helper.decodeExtraContextProperty('skipCreateAssignmentRecord');

    helper.setScriptOptions({
        allowAnyId: true,
        requiresStatusRecalc: true,
        allowDatesInDistantPast: true
    });
    if (helper.isETL()) {
        LABKEY.Query.selectRows({
            schemaName: 'ehr',
            queryName: 'protocol',
            columns: 'title,objectid',
            success: function (results) {
                if (results.rows.length) {
                    for (var i = 0; i < results.rows.length; i++) {
                        let rec = results.rows[i];
                        protocolData[rec.objectid] = rec.title;
                    }
                }
            },
            scope: this
        });
    }
}

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_INSERT, 'study', 'protocolAssignment', function (helper, scriptErrors, row, oldRow) {

    if (helper.isETL()) {
        var isTransfer = prevAnimalId === row.Id;

        if (row.remark) {
            var remarkTextArr = row.remark.split(':');
            var toProtocol = remarkTextArr[3].split(' (')[0]; // Get "To Protocol:" value without segment
            var protocolId;

            protocolId = getProtocolIdByName(toProtocol);
            if (!protocolId || protocolId === 'undefined') {
                if (missing.indexOf(toProtocol) === -1)
                    missing.push(toProtocol)
            }
            else {
                row.protocol = protocolId;
            }
            if (isTransfer) {
                if (row.enddate) {
                    // End date will initially have animal death/departure. Override if the transfer is older.
                    var death = new Date(row.enddate);
                    var prev = new Date(prevDate);

                    // Sanity check
                    if (prev < death) {
                        row.enddate = prevDate;
                    }
                }
                else {
                    row.enddate = prevDate;
                }
            }
            else if (count === 0) {
                // This handles batch boundary row for full truncate ETL, which is the only ETL setup for this currently.
                // Gets previous date from db for first row in batch
                var batchLastDate = getLastAssignment(row.Id);
                if (batchLastDate) {
                    row.enddate = batchLastDate;
                }
            }

            if (row.enddate === 'undefined') {
                console.log("end date not found for animal event - " + row.animalEventId);
            }
        }

        prevAnimalId = row.Id;
        prevDate = row.date;
        count++;
    }

});

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_UPSERT, 'study', 'protocolAssignment', function(helper, scriptErrors, row, oldRow) {
    if (!helper.isETL()) {

        if (helper.getProperty('skipCreateAssignmentRecord')['form'] === 'assignment') {
            return;
        }

        if (row.project && row.Id && row.date) {

            if (row.QCStateLabel) {
                row.qcstate = helper.getJavaHelper().getQCStateForLabel(row.QCStateLabel).getRowId();
            }

            let assignmentRec = {
                Id: row.Id,
                date: row.date,
                project: row.project,
                taskid: row.taskid,
                remark: row.remark,
                qcstate: row.qcstate
            }

            triggerHelper.createProjectAssignmentRecord(row.Id, assignmentRec);
        }
    }
});

function onComplete(event, helper){
    if (missing.length > 0)
        console.log("Missing Protocols: " + missing);
}

function getProtocolIdByName(protocolName) {
    var protocols = Object.keys(protocolData);
    for (var i = 0; i< protocols.length; i++) {
        var pName = protocolData[protocols[i]] ;
        if (pName && protocolName.trim().indexOf(pName.toString()) === 0) {
            return protocols[i];
        }
    }
}
