
Ext4.define('NIRC_EHR.field.DrugVolumeField', {
    extend: 'EHR.form.field.DrugVolumeField',
    alias: 'widget.nirc_ehr-drugvolumefield',

    triggerCls: 'x4-form-search-trigger',
    triggerToolTip: 'Click to calculate amount based on concentration and volume',

    initComponent: function(){
        this.callParent(arguments);
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