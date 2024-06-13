SELECT
    taskid,
    title,
    formType,
    assignedTo,
    created,
    createdBy,
    modified,
    modifiedBy,
    qcstate
FROM ehr.tasks WHERE formType = 'Necropsy'