SELECT
    po.procedureRecord,
    po.Id,
    po.procedure,
    po.windowStart,
    po.windowEnd,
    po.orderedby,
    po.remark,
    po.objectid
FROM prc_order po
WHERE now() > windowEnd AND po.qcstate.label != 'Completed'