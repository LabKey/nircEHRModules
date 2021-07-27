require("ehr/triggers").initScript(this);

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_INSERT, 'study', 'housing', function (helper, scriptErrors, row, oldRow) {

    if (helper.isETL()) {
        // format - From Location: UL Lafayette - NIRC; To Location: 28 - F2/CW - Rm 205 - B03;
        if (row.locationTransferText) {
            // replaceAll doesn't work
            let locationTransferText = row.locationTransferText.replace(';',':').replace(';',':').split(':');
            // [0] = From Location, [1] =  UL Lafayette - NIRC, [1] =  To Location, [2] = 28 - F2/CW - Rm 205 - B03
            // translate [3] to locationId
            LABKEY.Query.selectRows({
                schemaName: 'nirc_ehr',
                queryName: 'locations',
                columns: ['locationId'],
                filterArray: [
                    LABKEY.Filter.create('Name', locationTransferText[3].trim())
                ],
                scope: this,
                success: function (data) {
                    if (data && data.rows.length > 0) {
                        let rec = data.rows[0];
                        row.location = rec.LocationId;
                    }
                }
            });
        }
    }
});
