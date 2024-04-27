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
    requiredQC: 'Review Required',
    targetQC: 'Review Required',
    handler: function(submitDeathBtn){
        Ext4.Msg.show({
            title: 'Info',
            msg: 'You are about to submit a death record.  This will send a email notification to the appropriate parties.',
            buttons: Ext4.Msg.OK,
            width: 400,
            fn: function(okBtn, text, config) {
                var panel = submitDeathBtn.up('ehr-dataentrypanel');
                panel.onSubmit(submitDeathBtn);
            }
        }, this);
    },
    disableOn: 'ERROR'
});

// EHR.DataEntryUtils.registerDataEntryFormButton('NECROPSYSUBMIT', {
//     text: 'Submit Necropsy',
//     name: 'submitNecropsy',
//     requiredQC: 'Completed',
//     targetQC: 'Completed',
//     errorThreshold: 'ERROR',
//     successURL: LABKEY.ActionURL.buildURL('ehr', 'enterData.view'),
//     itemId: 'submitNecropsyBtn',
//     handler: function(btn){
//         var panel = btn.up('ehr-dataentrypanel');
//
//         //
//         // Ext4.Msg.confirm('Finalize Form', 'You are about to finalize this form.  Do you want to do this?', function(v){
//         //     if(v === 'yes')
//         //     {
//         //         this.onSubmit(btn);
//         //         panel.disable();
//         //     }
//         //
//         // }, this);
//     },
//     disableOn: 'ERROR'
// });