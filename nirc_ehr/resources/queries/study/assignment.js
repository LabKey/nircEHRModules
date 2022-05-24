require("ehr/triggers").initScript(this);
var projectData = {};
var prevAnimalId;
var prevDate;
var count = 0;

function getLastAssignment(id){
    var batchLatestEnddate;

    LABKEY.Query.selectRows({
        schemaName: 'study',
        queryName: 'assignment',
        columns: 'Id,enddate',
        filterArray: [LABKEY.Filter.create('Id', id)],
        sort: '-enddate',
        success: function (results) {
            if (results.rows.length) {
                batchLatestEnddate = results.rows[0].enddate;
            }
        },
        scope: this
    });

    return batchLatestEnddate;
}

function onInit(event, helper){
    if (helper.isETL()) {
        LABKEY.Query.selectRows({
            schemaName: 'ehr',
            queryName: 'project',
            columns: 'project,name',
            success: function (results) {
                if (results.rows.length) {
                    for (var i = 0; i < results.rows.length; i++) {
                        let rec = results.rows[i];
                        projectData[rec.name] = rec.project;
                    }
                }
            },
            scope: this
        });
    }
}

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_INSERT, 'study', 'assignment', function (helper, scriptErrors, row, oldRow) {

    if (helper.isETL()) {
        if (prevAnimalId === row.Id) {
            row.date = prevDate;
        }
        else if (count === 0) {
            // This handles batch boundary row for full truncate ETL, which is the only ETL setup for this currently.
            // Gets previous enddate from db for first row
            var batchLatestEnddate = getLastAssignment(row.Id);
            if (batchLatestEnddate) {
                row.date = batchLatestEnddate;
            }
        }


        var projectName = row.projectName.split(' ')[0];
        var projectId = projectData[projectName];
        if (!projectId) {
            var projectNames = Object.keys(projectData);
            for (var i = 0; i < projectNames.length; i++) {

                var pName = projectNames[i];
                if (pName.toString().indexOf(projectName) === 0) {
                    projectId = projectData[projectNames[i]];
                }
            }
        }
        if (projectId) {
            row.project = projectId;
        }
        else {
            console.log("project can not be found - ", projectName);
        }
        prevAnimalId = row.Id;
        if (!(!row.endDate || row.endDate === 'undefined')) {
            prevDate = row.endDate;
        }

        count++;
    }

});

