var LABKEY = require("labkey");

var triggerHelper = new org.labkey.nirc_ehr.query.NIRC_EHRTriggerHelper(LABKEY.Security.currentUser.id, LABKEY.Security.currentContainer.id);

function beforeDelete(row, errors) {
    if (extraContext.dataSource != "etl") {
        if (!row.name) {
            errors[null] = 'Building name is required.';
        }
        else {
            let totalRecords = triggerHelper.totalRecords("ehr_lookups", "floors", "building", row.name);
            if (totalRecords > 0) {
                errors[null] = 'Cannot delete. There are ' + totalRecords + ' floors currently registered in this building. Delete floors before deleting this building.';
            }
        }
    }
}