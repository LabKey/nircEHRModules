Ext4.define('NIRC_EHR.panel.InstructionsPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.nirc_ehr-instructionspanel',
    plugins: ['ehr-collapsibleDataEntryPanel'],

    initComponent: function(){
        Ext4.apply(this, {
            defaults: {
                border: false
            },
            items: [this.getItems()]
        });

        this.formConfig.initCollapsed = true;
        this.formConfig.dataDependentCollapseHeader = false;

        this.callParent(arguments);
    },

    getItems: function(){
        return {};
    }
});