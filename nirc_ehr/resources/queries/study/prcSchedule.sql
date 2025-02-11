
SELECT
    po.procedureRecord,
    po.Id,
    po.procedure,
    po.windowStart,
    po.windowEnd,
    po.orderedby,
    po.remark,
    po.objectid,
    CASE WHEN po.qcstate.label = 'Completed' THEN 'Completed' ELSE '' END as status
FROM prc_order po
WHERE now() >= windowStart AND now() <= windowEnd