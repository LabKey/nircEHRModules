
SELECT
    d2.id,
    d2.project,
    h.enddate as enddate

FROM study.assignment d2
         JOIN (SELECT id, max(date) as maxDate, max(enddate) as enddate FROM study.assignment h GROUP BY id) h
              ON (h.id = d2.id and d2.date = h.maxdate)
WHERE d2.qcstate.publicdata = true