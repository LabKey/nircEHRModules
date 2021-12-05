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

        if (helper.isETL() && row.damSire) {
            let damSire = row.damSire.split('DAM:');

            if (damSire.length > 1) {
                let damAndSire = damSire[1].split("SIRE:")
                if (damAndSire[0].trim() !== 'UNK' && damAndSire[0].trim() !== row.Id)
                    row.dam = damAndSire[0].trim();

                if (damAndSire.length > 1 && damAndSire[1].trim() !== 'UNK' && damAndSire[1].trim() !== row.Id)
                    row.sire = damAndSire[1].trim();
            }
        }
    }
    else if (row.source === 'Animal Event' && row.eventDate) {
        row.date = row.eventDate;
    }
});