
SELECT
  d.Id AS Id,

  T1.mostRecentArrival,
  T1.earliestArrival,
  d.birth,
  coalesce(T1.EarliestArrival, d.birth) as center_Arrival,
  timestampdiff('SQL_TSI_DAY', coalesce(T1.EarliestArrival, d.birth), now()) as daysSinceCenterArrival,

  CASE
    WHEN T1.EarliestArrival IS NULL AND d.birth IS NOT NULL THEN true
    ELSE false
  END as fromCenter,
  d.source as source,

  CASE
    WHEN T1.EarliestArrival IS NULL AND d.birth IS NOT NULL THEN 'Born at NIRC'
    WHEN T1.EarliestArrival IS NOT NULL AND T2.sourceFacility IS NOT NULL THEN T2.sourceFacility.meaning
    ELSE 'Unknown'
  END as type

FROM study.demographics d

LEFT JOIN
  (select T1.Id, max(T1.date) as MostRecentArrival, min(T1.date) as EarliestArrival FROM study.arrival T1 WHERE T1.qcstate.publicdata = true GROUP BY T1.Id) T1
  ON (T1.Id = d.Id)

LEFT JOIN study.arrival T2 ON (t2.id = d.id AND t2.date = t1.earliestArrival)

