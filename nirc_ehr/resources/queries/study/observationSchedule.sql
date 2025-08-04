SELECT
    g.id,
    g.scheduledDate,
    COUNT(g.caseid) cases,
    GROUP_CONCAT(g.observations, ';') AS observations,
    SUM(obsCount) AS obsCount,
    GROUP_CONCAT(g.obsOrderIds, ';') AS orderIds,
    GROUP_CONCAT(g.status, ';') AS status,
    GROUP_CONCAT(g.taskids, ';') AS taskids,
    MAX(g.type) AS type,
    MAX(g.caseid) AS caseid
FROM
(
    SELECT
        sch.animalId as id,
        sch.date AS scheduledDate,
        sch.caseid,
        sch.type,
        GROUP_CONCAT(sch.objectid, ';') AS obsOrderIds,
        GROUP_CONCAT(sch.category, ';') AS observations,
        GROUP_CONCAT(obsStatus, ';') AS status,
        GROUP_CONCAT(DISTINCT(sch.taskid), ';') AS taskids,
        COUNT(sch.category) AS obsCount,
        COUNT(sch.obsStatus) AS statusCount
    FROM (
        SELECT * FROM observationOrdersByDate
    ) sch
    GROUP BY
        sch.animalId,
        sch.date,
        sch.caseid,
        sch.type
) g
GROUP BY
    g.id,
    g.scheduledDate
