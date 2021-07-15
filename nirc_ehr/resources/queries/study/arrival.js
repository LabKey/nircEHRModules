require("ehr/triggers").initScript(this);

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_INSERT, 'study', 'arrival', function (helper, scriptErrors, row, oldRow) {
    // if born at center acq date and birth date is same (row.description contains ACQ date)
    // if brought from outside aqc date and birth date may differ

    if (row.source === 'Alternate' ) {
        if (row.acqDateText) {
            let acqDateText = row.acqDateText.split(':')[1]; // format - ACQ: 24 Sep 2008
            if (!acqDateText) {
                acqDateText = row.acqDateText.split(';')[1]; // at least one value is there with ';'
            }
            if (acqDateText) {
                let acqDate = new Date(acqDateText.trim());
                if (acqDate !== row.birth) {
                    row.date = acqDate;
                }
                else {
                    // investigate to see if any row exists
                    console.log("born at center - ", row.Id);
                    row.date = row.birth;
                }
            }
        }

        let damSire = row.damSire.split(' '); // format - DAM: UNK SIRE: UNK
        if (damSire[1] && damSire[1] !== 'UNK') {
            row.dam = damSire[1];

        }
        if (damSire[3] && damSire[3] !== 'UNK') {
            row.sire = damSire[3];
        }
    }
    else if (row.source === 'Animal Event' && row.eventDate) {
        row.date = row.eventDate;
    }
});