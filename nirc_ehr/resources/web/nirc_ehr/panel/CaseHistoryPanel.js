/*
 * Copyright (c) 2026 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * @cfg caseId
 * @cfg maxGridHeight
 * @cfg autoLoadRecords
 */
Ext4.define('NIRC_EHR.panel.CaseHistoryPanel', {
    extend: 'NIRC_EHR.panel.ClinicalHistoryPanel',
    alias: 'widget.nirc_ehr-casehistorypanel',

    getStoreConfig: function(){
        return {
            type: 'ehr-clinicalhistorystore',
            containerPath: this.containerPath,
            actionName: 'getCaseHistory',
            sorters: [{property: 'group'}, {property: 'timeString'}]
        };
    }
});
