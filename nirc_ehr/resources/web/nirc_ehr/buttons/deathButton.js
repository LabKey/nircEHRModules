/**
 * A button specific to the Death-Necropsy form
 */

EHR.DataEntryUtils.registerDataEntryFormButton('DEATHSUBMIT', {
    text: 'Submit Death',
    name: 'submitDeath',
    errorThreshold: 'ERROR',
    successURL: LABKEY.ActionURL.buildURL('ehr', 'enterData.view'),
    itemId: 'submitDeathBtn',
    handler: function(btn){
        var panel = btn.up('ehr-dataentrypanel');

        //TODO:
        // 1. Info dialog about sending Death Notification.
        // 2. Send Death notification to the configured email addresses.
        // Ext4.Msg.confirm('Finalize Form', 'You are about to finalize this form.  Do you want to do this?', function(v){
        //     if(v === 'yes')
        //     {
        //         this.onSubmit(btn);
        //         panel.disable();
        //     }
        //
        // }, this);
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
//         //TODO: Death notification message
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