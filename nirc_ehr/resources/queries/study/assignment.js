require("ehr/triggers").initScript(this);
var projectData = {};
var prevAnimalId;
var prevDate;

function onInit(event, helper){
    LABKEY.Query.selectRows({
        schemaName: 'ehr',
        queryName: 'project',
        columns: 'project,name',
        success: function(results){
            if (results.rows.length){
                for (var i = 0; i < results.rows.length; i++) {
                    let rec = results.rows[i];
                    projectData[rec.name] = rec.project;
                }
            }
        },
        scope: this
    });
}

EHR.Server.TriggerManager.registerHandlerForQuery(EHR.Server.TriggerManager.Events.BEFORE_INSERT, 'study', 'assignment', function (helper, scriptErrors, row, oldRow) {

    if (prevAnimalId === row.Id) {
        row.date = prevDate;
    }
    else {
        row.date = new Date("1970/01/01");
    }

    if (row.changeDateText) {
        if (row.changeDateText.indexOf("ACQ:") === 0) {
            row.endDate = new Date(Date.parse(row.changeDateText.split(":")[1].trim()));
        }
        else {
            row.endDate = new Date(Date.parse(row.changeDateText));
        }
    }

    var projectName = row.projectName.split(' ')[0];
    var projectId = projectData[projectName];
    if (!projectId) {
        var projectNames = Object.keys(projectData);
        for (var i = 0; i< projectNames.length; i++) {

            var pName = projectNames[i] ;
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

});

