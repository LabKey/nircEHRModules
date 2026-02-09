
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
