SELECT
    Id,
    date,
    category,
    area,
    observation,
    remark,
    performedBy,
    scheduledDate,
    QCState
FROM clinical_observations
GROUP BY
    Id,
    date,
    category,
    area,
    observation,
    remark,
    performedBy,
    scheduledDate,
    QCState