SELECT
    po.procedureRecord,
    po.Id,
    po.procedure,
    po.windowStart,
    po.windowEnd,
    timestampdiff('SQL_TSI_DAY', po.windowEnd, now()) as daysOverdue,
    po.orderedby,
    po.remark,
    po.objectid,
    CASE WHEN po.qcstate.label = 'Completed' THEN 'Completed' ELSE '' END as status
FROM prc_order po
WHERE now() > windowEnd AND po.qcstate.label != 'Completed'