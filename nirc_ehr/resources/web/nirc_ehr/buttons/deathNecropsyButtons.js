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
        Ext4.Msg.confirm('Confirm', 'You are about to submit a death record. This will send an email notification to the appropriate parties. Submit death?', function(val){
            if (val == 'yes') {
                var panel = submitDeathBtn.up('ehr-dataentrypanel');
                panel.onSubmit(submitDeathBtn);
            }
        }, this);
    },
    disableOn: 'ERROR'
});

//TODO: figure out the workflow from client
// EHR.DataEntryUtils.registerDataEntryFormButton('SUBMITNECROPSYFORREVIEW', {
//     text: 'Submit for Review',
//     name: 'submitNecropsyForReview',
//     requiredQC: 'Review Required',
//     targetQC: 'Review Required',
//     errorThreshold: 'ERROR',
//     disabled: true,
//     successURL: LABKEY.ActionURL.buildURL('ehr', 'enterData.view'),
//     itemId: 'submitNecropsyForReviewBtn',
//     handler: function(btn){
//         var panel = btn.up('ehr-dataentrypanel');
//         panel.onSubmit(btn, false, true);
//     },
//     disableOn: 'ERROR'
// });