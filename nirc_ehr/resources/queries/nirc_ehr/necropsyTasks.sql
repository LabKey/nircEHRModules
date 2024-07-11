SELECT * FROM (SELECT
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
    END AS status,
    CASE WHEN t.qcstate.Label = 'Request: Pending' THEN 1
         WHEN t.qcstate.Label = 'Review Required' THEN 2
         WHEN t.qcstate.Label = 'Completed' THEN 3
         ELSE 4
        END AS statusOrder
FROM ehr.tasks t
LEFT JOIN study.deaths d ON d.taskid = t.taskid
WHERE formType = 'Necropsy') n
ORDER BY n.statusOrder ASC