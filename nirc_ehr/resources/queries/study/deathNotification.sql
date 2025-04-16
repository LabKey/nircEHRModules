
SELECT
    Id,
    date,
    taskid,
    performedBy.DisplayName AS performedBy,
    reason.title AS reason
FROM study.deaths