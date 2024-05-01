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

EHR.DataEntryUtils.registerDataEntryFormButton('SUBMITNECROPSYFORREVIEW', {
    text: 'Submit for Review',
    name: 'submitNecropsyForReview',
    requiredQC: 'Review Required',
    targetQC: 'Review Required',
    errorThreshold: 'ERROR',
    disabled: true,
    successURL: LABKEY.ActionURL.buildURL('ehr', 'enterData.view'),
    itemId: 'submitNecropsyForReviewBtn',
    handler: function(btn){
        var panel = btn.up('ehr-dataentrypanel');
        panel.onSubmit(btn, false, true);
    },
    disableOn: 'ERROR'
});