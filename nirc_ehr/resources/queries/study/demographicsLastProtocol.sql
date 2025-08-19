
SELECT
    d2.id,
    d2.protocol,
    h.enddate as enddate

FROM study.protocolAssignment d2
         JOIN (SELECT id, max(date) as maxDate, max(enddate) as enddate FROM study.protocolAssignment h GROUP BY id) h
              ON (h.id = d2.id and d2.date = h.maxdate)
WHERE d2.qcstate.publicdata = true