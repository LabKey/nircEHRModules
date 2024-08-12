var LABKEY = require("labkey");

var triggerHelper = new org.labkey.nirc_ehr.query.NIRC_EHRTriggerHelper(LABKEY.Security.currentUser.id, LABKEY.Security.currentContainer.id);

function onUpsert(row, oldRow, errors){
    if (extraContext.dataSource != "etl") {
        if (!row.room) {
            if (oldRow && oldRow.room && oldRow.room[0]) {
                row.room = oldRow.room[0];
                return;
            }

            row.location = row.room;
            if (row.cage)
                row.location += '-' + row.cage;
        }
    }
}

function beforeInsert(row, errors){
    onUpsert(row, undefined, errors);
}

function beforeUpdate(row, oldRow, errors){
    onUpsert(row, oldRow, errors);
}

function beforeDelete(row, errors) {
    if (extraContext.dataSource != "etl") {
        if (!row.room) {
            errors[null] = 'Room is required.';
        }
        else {
            let totalRecords = triggerHelper.totalRecords("ehr_lookups", "cage", "room", row.room);
            if (totalRecords > 0) {
                errors[null] = 'Cannot delete. There are ' + totalRecords + ' cages currently registered in this room. Delete cages before deleting this room.';
            }
        }
    }
}