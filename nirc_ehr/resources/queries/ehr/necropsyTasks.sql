SELECT
    taskid,
    updateTitle AS form,
    assignedTo,
    created,
    createdBy,
    modified,
    modifiedBy,
    qcstate
FROM ehr.tasks WHERE formType = 'Necropsy'