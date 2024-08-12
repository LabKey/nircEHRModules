var LABKEY = require("labkey");

var triggerHelper = new org.labkey.nirc_ehr.query.NIRC_EHRTriggerHelper(LABKEY.Security.currentUser.id, LABKEY.Security.currentContainer.id);

function beforeDelete(row, errors) {
    if (extraContext.dataSource != "etl") {
        if (!row.floor) {
            errors[null] = 'Floor is required.';
        }
        else {
            let totalRecords = triggerHelper.totalRecords("ehr_lookups", "rooms", "floor", row.floor);
            if (totalRecords > 0) {
                errors[null] = 'Cannot delete. There are ' + totalRecords + ' rooms currently registered on this floor. Delete rooms before deleting this floor.';
            }
        }
    }
}