SELECT
    t.taskid,
    d.Id AS animalId,
    t.updateTitle AS form,
    t.assignedTo,
    t.created,
    t.createdBy,
    t.modified,
    t.modifiedBy,
    CASE WHEN t.qcstate.Label = 'Request: Pending' THEN 'Death Entered: Necropsy Pending'
         WHEN t.qcstate.Label = 'Review Required' THEN 'Necropsy Entered: Review Required'
        ELSE t.qcstate.Label
    END AS status
FROM ehr.tasks t
LEFT JOIN study.deaths d ON d.taskid = t.taskid
WHERE formType = 'Necropsy'