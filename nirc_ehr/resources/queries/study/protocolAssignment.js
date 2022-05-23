require("ehr/triggers").initScript(this);
var protocolData = {};
var prevAnimalId;
var prevDate;

var missing = [];

function onInit(event, helper){
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

                    if (prev < death) {
                        row.enddate = prevDate;
                    }
                }
                else {
                    row.enddate = prevDate;
                }
            }

            if (row.enddate === 'undefined') {
                console.log("end date not found for animal event - " + row.animalEventId);
            }
        }

        prevAnimalId = row.Id;
        prevDate = row.date;
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
