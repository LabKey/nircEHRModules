
Ext4.define('NIRC_EHR.field.DrugVolumeField', {
    extend: 'EHR.form.field.DrugVolumeField',
    alias: 'widget.nirc_ehr-drugvolumefield',

    triggerCls: 'x4-form-search-trigger',
    triggerToolTip: 'Click to calculate amount based on concentration and volume',

    initComponent: function(){
        this.callParent(arguments);
        this.getSnomedStore();
    },

    getSnomedStore: function(){
        if (NIRC_EHR._snomedStore)
            return NIRC_EHR._snomedStore;

        var storeId = ['ehr_lookups', 'snomed', 'code', 'meaning'].join('||');

        NIRC_EHR._snomedStore = Ext4.create('LABKEY.ext4.data.Store', {
            type: 'labkey-store',
            schemaName: 'ehr_lookups',
            queryName: 'snomed',
            columns: 'code,meaning',
            sort: 'meaning',
            storeId: storeId,
            autoLoad: true,
            getRecordForCode: function(code){
                var recIdx = this.findExact('code', code);
                if (recIdx != -1){
                    return this.getAt(recIdx);
                }
            }
        });

        return NIRC_EHR._snomedStore;
    },

    onTriggerClick: function(){
        var record = EHR.DataEntryUtils.getBoundRecord(this);
        if (!record){
            return;
        }

        if (!record.get('code') || !record.get('Id')){
            Ext4.Msg.alert('Error', 'Must enter the Animal Id and treatment');
            return;
        }

        Ext4.create('NIRC_EHR.window.DrugAmountWindow', {
            targetStore: record.store,
            formConfig: record.sectionCfg,
            boundRecord: record
        }).show();
    }
});