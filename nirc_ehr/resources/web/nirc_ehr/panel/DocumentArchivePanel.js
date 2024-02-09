Ext4.define('NIRC_EHR.panel.DocumentArchivePanel', {
    extend: 'EHR.panel.DocumentArchivePanel',
    alias: 'widget.nirc_ehr-documentarchivepanel',
    plugins: ['ehr-collapsibleDataEntryPanel'],

    initComponent: function(){

        this.formConfig.initCollapsed = true;
        this.formConfig.dataDependentCollapseHeader = false;

        this.callParent(arguments);
    },
});