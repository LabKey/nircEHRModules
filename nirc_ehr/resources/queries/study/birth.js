require("ehr/triggers").initScript(this);

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_INSERT, 'study', 'birth', function (helper, scriptErrors, row, oldRow) {
    let damSire = row.damSire.split(' '); // format - DAM: UNK SIRE: UNK
    if (damSire[1] && damSire[1] !== 'UNK') {
        row.dam = damSire[1];

    }
    if (damSire[3] && damSire[3] !== 'UNK') {
        row.sire = damSire[3];
    }

});