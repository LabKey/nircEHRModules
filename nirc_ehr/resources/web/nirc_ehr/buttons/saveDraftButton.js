EHR.DataEntryUtils.registerDataEntryFormButton('NIRCSAVEDRAFTBUTTON', {
    text: 'Save Draft',
    name: 'nircSaveDraft',
    errorThreshold: 'ERROR',
    successURL: LABKEY.ActionURL.buildURL('ehr', 'enterData.view'),
    itemId: 'nircSaveDraftBtn',
    disabled: false,
    requiredQC: 'In Progress',
    targetQC: 'In Progress',
    handler: function(btn){
        var panel = btn.up('ehr-dataentrypanel');
        panel.onSubmit(btn, false, true);
    },
    disableOn: 'ERROR'
});