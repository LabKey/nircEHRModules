Ext4.define('NIRC_EHR.plugin.RowEditor', {
    extend: 'EHR.plugin.RowEditor',

    getDetailsPanelCfg: function () {
        return {
            xtype: 'nirc_ehr-animaldetailspanel',
            itemId: 'detailsPanel',
            showDisableButton: false
        }
    },

    getWindowCfg: function(){
        return {
            modal: true,
            width: 1000,
            border: false,
            items: [{
                items: [this.getDetailsPanelCfg(), this.getFormPanelCfg()]
            }],
            buttons: this.getWindowButtons(),
            closeAction: 'destroy',
            listeners: {
                scope: this,
                close: this.onWindowClose,
                destroy: this.onWindowClose,
                beforerender: function(win){
                    var cols = win.down('#formPanel').items.get(0).items.getCount();
                    if (cols > 1){
                        var newWidth = cols * (EHR.form.Panel.defaultFieldWidth + 20);
                        if (newWidth > win.width) {
                            win.setWidth(newWidth);
                        }
                    }
                },
                afterrender: function(editorWin){
                    this.keyNav = new Ext4.util.KeyNav({
                        target: editorWin.getId(),
                        scope: this,
                        up: function(e){
                            if (e.ctrlKey){
                                this.loadPreviousRecord();
                            }
                        },
                        down: function (e){
                            if (e.ctrlKey){
                                this.loadNextRecord();
                            }
                        }
                    });
                },
                animalchange: {
                    fn: function(id){
                        this.getEditorWindow().down('#detailsPanel').loadAnimal(id);
                    },
                    scope: this,
                    buffer: 200
                }
            }
        }
    },
})