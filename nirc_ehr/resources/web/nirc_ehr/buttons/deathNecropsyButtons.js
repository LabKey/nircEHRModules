/**
 * A button specific to the Death-Necropsy form
 */

EHR.DataEntryUtils.registerDataEntryFormButton('DEATHSUBMIT', {
    text: 'Submit Death',
    name: 'submitDeath',
    errorThreshold: 'ERROR',
    successURL: LABKEY.ActionURL.buildURL('ehr', 'enterData.view'),
    itemId: 'submitDeathBtn',
    disabled: true,
    requiredQC: 'Request: Pending',
    targetQC: 'Request: Pending',
    handler: function(submitDeathBtn){
        Ext4.Msg.confirm('Confirm', 'You are about to submit a death record. This will send an email notification to the appropriate parties. Submit death?', function(val){
            if (val == 'yes') {
                var panel = submitDeathBtn.up('ehr-dataentrypanel');
                panel.onSubmit(submitDeathBtn);
            }
        }, this);
    },
    disableOn: 'ERROR'
});

EHR.DataEntryUtils.registerDataEntryFormButton('DEATH_NECROPSY_VET_REVIEW', {
    text: 'Submit for Review',
    name: 'review',
    requiredQC: 'Review Required',
    targetQC: 'Review Required',
    errorThreshold: 'WARN',
    successURL: LABKEY.ActionURL.getParameter('srcURL') || LABKEY.ActionURL.buildURL('ehr', 'enterData.view'),
    disabled: true,
    itemId: 'reviewBtn',
    disableOn: 'ERROR',
    handler: function(btn){
        var panel = btn.up('ehr-dataentrypanel');

        Ext4.create('NIRC_EHR.window.DeathNecropsySubmitForReviewWindow', {
            dataEntryPanel: panel,
            dataEntryBtn: btn,
            reviewRequiredRecipient: panel.formConfig.defaultReviewRequiredPrincipal
        }).show();

    }
});

Ext4.define('NIRC_EHR.window.DeathNecropsySubmitForReviewWindow', {
    extend: 'Ext.window.Window',

    initComponent: function(){
        Ext4.apply(this, {
            closeAction: 'destroy',
            modal: true,
            title: 'Submit For Review',
            width: 430,
            buttons: [{
                text:'Submit',
                disabled:false,
                itemId: 'submit',
                scope: this,
                handler: function(btn){
                    var win = btn.up('window');
                    var assignedTo = win.down('#assignedTo').getValue();
                    if(!assignedTo){
                        alert('Must assign this task to someone');
                        return;
                    }

                    var taskStore = this.dataEntryPanel.storeCollection.getServerStoreForQuery('ehr', 'tasks');
                    taskStore.getAt(0).set('assignedto', assignedTo);
                    this.dataEntryPanel.storeCollection.transformServerToClient();
                    this.dataEntryPanel.onSubmit(this.dataEntryBtn);
                    win.close();
                }
            },{
                text: 'Cancel',
                scope: this,
                handler: function(btn){
                    btn.up('window').hide();
                }
            }],
            items: [{
                bodyStyle: 'padding:5px;',
                items: [{
                    html: this.reviewRecipientMsg || '',
                    border: false,
                    style: 'padding-bottom: 10px;',
                    hidden: !this.reviewRecipientMsg
                },{
                    xtype: 'labkey-combo',
                    forceSelection: true,
                    fieldLabel: 'Assign To',
                    width: 400,
                    queryMode: 'local',
                    store: {
                        type: 'labkey-store',
                        schemaName: 'ehr_lookups',
                        queryName: 'veterinarians',
                        columns: 'UserId, DisplayName',
                        autoLoad: true
                    },
                    value: this.getDefaultRecipient(),
                    displayField: 'DisplayName',
                    valueField: 'UserId',
                    itemId: 'assignedTo',
                    id: 'assignedTo',
                    anyMatch: true,
                    caseSensitive: false,
                }]
            }]
        });

        this.callParent(arguments);
    },

    getDefaultRecipient: function(){
        return this.reviewRequiredRecipient;
    }
});