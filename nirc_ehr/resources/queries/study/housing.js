require("ehr/triggers").initScript(this);

var locationsData = {};

function onInit (event, helper){

    LABKEY.Query.selectRows({
        schemaName: 'nirc_ehr',
        queryName: 'locations',
        columns: ['locationId', 'Name'],
        scope: this,
        success: function (data) {
            if (data && data.rows.length > 0) {
                for (let i = 0; i < data.rows.length; i++) {
                    let rec = data.rows[i];
                    locationsData[rec.Name] = rec.LocationId;
                }
            }
        }
    });

}

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_INSERT, 'study', 'housing', function (helper, scriptErrors, row, oldRow) {

    if (helper.isETL()) {
        // format - From Location: UL Lafayette - NIRC; To Location: 28 - F2/CW - Rm 205 - B03;
        if (row.locationTransferText) {
            // replaceAll doesn't work
            let locationTransferText = row.locationTransferText.replace(';',':').replace(';',':').split(':');
            // [0] = From Location,
            // [1] =  UL Lafayette - NIRC,
            // [2] =  To Location,
            // [3] = 28 - F2/CW - Rm 205 - B03
            // translate [3] to locationId
            var location;
            if (locationsData) {
                location = locationsData[locationTransferText[3].trim()];
            }
            if (location) {
                row.location = location;
            }
            else {
                console.log("location not found for animal - " + row.Id);
            }
        }
    }
});
