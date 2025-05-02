SELECT
    Id,
    date,
    category,
    area,
    observation,
    remark,
    performedBy,
    scheduledDate,
    QCState,
    type
FROM clinical_observations
WHERE type = 'Clinical'
GROUP BY
    Id,
    date,
    category,
    area,
    observation,
    remark,
    performedBy,
    scheduledDate,
    QCState,
    type