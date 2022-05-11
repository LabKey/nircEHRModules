require("ehr/triggers").initScript(this);
var protocolData = {};
var prevAnimalId;
var prevDate;

function onInit(event, helper){
    LABKEY.Query.selectRows({
        schemaName: 'ehr',
        queryName: 'protocol',
        columns: 'protocol,objectid',
        success: function(results){
            if (results.rows.length){
                for (var i = 0; i < results.rows.length; i++) {
                    let rec = results.rows[i];
                    protocolData[rec.objectid]  = rec.protocol;
                }
            }
        },
        scope: this
    });
}

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_INSERT, 'study', 'protocolAssignment', function (helper, scriptErrors, row, oldRow) {

    var isTransfer = prevAnimalId === row.Id;

    if (row.remark) {
        var remarkTextArr = row.remark.split(':');
        var toProtocol = remarkTextArr[3];
        var protocolId;

        protocolId = getProtocolIdByName(toProtocol);
        if (!protocolId || protocolId === 'undefined') {
            console.log("Protocol not found for animal event - " + row.animalEventId + ", protocol - " + toProtocol);
        }
        else {
            row.protocol = protocolId;
        }
        if (isTransfer) {
            row.endDate = prevDate;
        }

        if (row.endDate === 'undefined') {
            console.log("end date not found for animal event - " + row.animalEventId);
        }
    }

    prevAnimalId = row.Id;
    prevDate = row.date;

});

function getProtocolIdByName(protocolName) {
    var protocols = Object.keys(protocolData);
    for (var i = 0; i< protocols.length; i++) {
        var pName = protocolData[protocols[i]] ;
        if (protocolName.trim().indexOf(pName.toString()) === 0) {
            return protocols[i];
        }
    }
}
