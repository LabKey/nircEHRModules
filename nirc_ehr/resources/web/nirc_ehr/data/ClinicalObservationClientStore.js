Ext4.define('NIRC_EHR.data.ClinicalObservationsClientStore', {
    extend: 'EHR.data.DataEntryClientStore',

    constructor: function(){
        this.callParent(arguments);

        this.observationTypesStore = EHR.DataEntryUtils.getObservationTypesStore();
    },

    getExtraContext: function(){
        // Pass through tasks for entering observations from schedule
        const obsTask = LABKEY.ActionURL.getParameter('obsTask');

        return {
            orderTasksInTransaction: [obsTask]

        }
    }
});