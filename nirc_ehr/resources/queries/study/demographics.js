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
        let damSire = row.damSire.split('DAM:');
        if (damSire.length === 1) {
            row.damSire.split('DAM');
        }

        if (damSire.length > 1) {
            let damAndSire = damSire[1].split("SIRE:")
            if (damAndSire.length === 1) {
                row.damSire.split('SIRE');
            }
            if (damAndSire[0].trim() !== 'UNK' && damAndSire[0].trim() !== row.Id)
                row.dam = damAndSire[0].trim();

            if (damAndSire.length > 1 && damAndSire[1].trim() !== 'UNK' && damAndSire[1].trim() !== row.Id)
                row.sire = damAndSire[1].trim();

            if (row.sire === row.dam) {
                row.sire = '';
                row.dam = '';
            }

            // Known issue with this Id listed as sire and dam
            if (row.sire === 'C0502009') {
                row.sire = '';
            }
            if (row.dam === 'C0502009') {
                row.dam = '';
            }
        }
    }

}

function onComplete(event, errors, helper){

    if (helper.isETL()) {
        var invalidSiresQuery = "SELECT * FROM Demographics WHERE sire.Demographics.gender.meaning = \'female\'";
        var invalidDamsQuery = "SELECT * FROM Demographics WHERE dam.Demographics.gender.meaning = \'male\'";

        var invalidSires = [];
        var invalidDams = [];

        // MultiRequest not available on server side so have to manually do callback when both done
        var ajaxCount = 0;

        function fixPedigree() {
            var fixedSires = invalidSires.map(function (s) {
                var found = false;

                // If both Ids are off just assume Ids were mixed up and swap them
                invalidDams.forEach(function (d) {
                    if (s.Id === d.Id) {
                        found = true;

                        var sire = s.sire
                        s.sire = s.dam;
                        s.dam = sire;

                    }
                })
                if (!found)
                    s.sire = '';

                return s;
            })

            var fixedDams = [];
            invalidDams.forEach(function (d) {
                var found = false;
                fixedSires.forEach(function (s) {
                    if (s.Id === d.Id) {
                        found = true;
                    }
                })
                if (!found) {
                    d.dam = '';
                    fixedDams.push(d);
                }
            })

            var totalRows = fixedSires.concat(fixedDams);

            console.log("Found " + totalRows.length + " dams/sires of the wrong gender. Attempting to fix or remove the values.")

            LABKEY.Query.updateRows({
                schemaName: "study",
                queryName: "demographics",
                rows: fixedSires.concat(fixedDams),
                scope: this,
                success: function () {
                    console.log("Successfully removed incorrect dam/sire values.")
                },
                failure: function () {
                    console.error("Update incorrect dam/sire values failed.")
                }
            });
        }

        LABKEY.Query.executeSql( {
            schemaName: 'study',
            sql: invalidSiresQuery,
            scope: this,
            success: function (data) {
                if (!data || !data.rows)
                    return;

                invalidSires = data.rows;
                console.log("Invalid Sires: " + data.rows.length)
                ajaxCount += 1;
                if (ajaxCount === 2) {
                    fixPedigree();
                    ajaxCount = 0;
                }
            },
            failure: function (error) {
                console.error("Unable to fix sire data: ", error);
            }
        });

        LABKEY.Query.executeSql({
            schemaName: 'study',
            sql: invalidDamsQuery,
            scope: this,
            success: function (data) {
                if (!data || !data.rows)
                    return;

                invalidDams = data.rows;
                console.log("Invalid Dams: " + data.rows.length)
                ajaxCount += 1;
                if (ajaxCount === 2) {
                    fixPedigree();
                    ajaxCount = 0;
                }
            },
            failure: function (error) {
                console.error("Unable to fix dam data: ", error);
            }
        });
    }
}