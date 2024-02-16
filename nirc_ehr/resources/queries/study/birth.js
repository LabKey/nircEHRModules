require("ehr/triggers").initScript(this);

function onInit(event, helper){
    helper.setScriptOptions({
        allowAnyId: true,
        requiresStatusRecalc: true,
        allowDeadIds: true,
        skipIdFormatCheck: true,
        skipHousingCheck: true,
        announceAllModifiedParticipants: true,
        allowDatesInDistantPast: true,
        removeTimeFromDate: true
    });

    helper.decodeExtraContextProperty('birthsInTransaction');
}