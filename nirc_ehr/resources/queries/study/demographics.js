/*
 * Copyright (c) 2010-2019 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */

require("ehr/triggers").initScript(this);

function onInit(event, helper){
    helper.setScriptOptions({
        allowAnyId: true,
        requiresStatusRecalc: false,
        allowDatesInDistantPast: true
    });
}

function onUpsert(helper, scriptErrors, row, oldRow){
    //NOTE: this should be getting set by the birth, death, arrival & departure tables
    //ALSO: it should be rare to insert directly into this table.  usually this record will be created by inserting into either birth or arrival
    if (!row.calculated_status && !helper.isETL()){
        row.calculated_status = helper.getJavaHelper().getCalculatedStatusValue(row.Id);
    }

    if (helper.isETL() && row.damSire) {
        let damSire = row.damSire.split(' '); // format - DAM: UNK SIRE: UNK
        if (damSire[1] && damSire[1] !== 'UNK') {
            row.dam = damSire[1];

        }
        if (damSire[3] && damSire[3] !== 'UNK') {
            row.sire = damSire[3];
        }
    }

}