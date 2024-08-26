/*
 * Copyright (c) 2024 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */

require("ehr/triggers").initScript(this);

var animalIdCasesMap = {};
var triggerHelper = new org.labkey.nirc_ehr.query.NIRC_EHRTriggerHelper(LABKEY.Security.currentUser.id, LABKEY.Security.currentContainer.id);

function onInit(event, helper){

    LABKEY.Query.selectRows({
        schemaName: 'study',
        queryName: 'cases',
        columns: ['Id', 'caseid', 'enddate'],
        scope: this,
        sort: 'Id',
        success: function (results) {
            if (!results || !results.rows || results.rows.length < 1)
                return;

            for(var i=0; i < results.rows.length; i++) {

                console.log("results.rows[i]: " + results.rows[i]["enddate"]);
                // get all open cases associated with an animal
                if (!results.rows[i]["enddate"]) {
                    var animal = results.rows[i]["Id"]["value"];
                    var caseId = results.rows[i]["caseid"]["value"];
                    if (!animalIdCasesMap[animal]) {
                        animalIdCasesMap[animal] = [];
                    } else {
                        animalIdCasesMap[animal].push(caseId);
                    }
                }
            }
        },
        failure: function (error) {
            console.log("error getting death data in death trigger onInit()\n" + error);
        }
    });
}

function onUpsert(helper, scriptErrors, row, oldRow) {
    if (row.Id) {
        if (animalIdCasesMap[row.Id] && animalIdCasesMap[row.Id].length > 0) {
            triggerHelper.addClinicalObsForCases(row, animalIdCasesMap[row.Id]); //Add clinical obs for all open cases associated with an animal
        }
    }

    if (!row.observation && !row.remark){
        EHR.Server.Utils.addError(scriptErrors, 'observation', 'Must enter an observation or remark', 'WARN');
        EHR.Server.Utils.addError(scriptErrors, 'remark', 'Must enter an observation or remark', 'WARN');
    }
}