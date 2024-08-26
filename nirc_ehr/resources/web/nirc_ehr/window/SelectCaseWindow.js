
/**
 * @cfg {String} animalId
 * @cfg {Object} panel
 */
Ext4.define('NIRC_EHR.window.SelectCaseWindow', {
    extend: 'Ext.window.Window',

    width: 1200,
    minHeight: 50,

    initComponent: function(){
        Ext4.apply(this, {
            title: 'Select Case: ' + this.animalId,
            modal: true,
            closeAction: 'destroy',
            items: [{
                xtype: 'nirc_ehr-selectcasepanel',
                animalId: this.animalId,
                hideButtons: true,
                casepanel: this.casepanel
            }],
            buttons: this.getButtonConfig()
        });

        this.callParent(arguments);
    },

    getButtonConfig: function(){
        var buttons = NIRC_EHR.panel.SelectCasePanel.getButtonConfig();
        buttons.push({
            text: 'Close',
            handler: function(btn){
                btn.up('window').close();
            }
        });

        return buttons;
    }
});
