/*
 * Copyright (c) 2010-2014 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */

SELECT
d.id,
d.calculated_status,
s.*,
s.objectid as treatmentid,
drug.qcstate.label as treatmentStatus
FROM study.demographics d JOIN


(SELECT
  s1.*,
  timestampadd('SQL_TSI_MINUTE', ((s1.hours * 60) + s1.minutes), s1.origDate) as date,
  ((s1.hours * 60) + s1.minutes) as timeOffset

FROM (

SELECT
  t1.lsid,
  t1.objectid,
  t1.dataset,
  t1.id as animalid,

  coalesce(tt.time, ft.hourofday, ((hour(t1.date) * 100) + minute(t1.date))) as time,
  (coalesce(tt.time, ft.hourofday, (hour(t1.date) * 100)) / 100) as hours,
  CASE
    WHEN (tt.time IS NOT NULL OR ft.hourofday IS NOT NULL) THEN (((coalesce(tt.time, ft.hourofday) / 100.0) - floor(coalesce(tt.time, ft.hourofday) / 100)) * 100)
    ELSE minute(t1.date)
  END as minutes,
  dr.date as origDate,
  CASE
    WHEN (tt.time IS NULL) THEN 'Default'
    ELSE 'Custom'
  END as timeType,

  CASE
    WHEN snomed.code IS NOT NULL THEN 'Diet'
    ELSE IFDEFINED(t1.category)
  END as category,
  t1.frequency.meaning as frequency,
  t1.date as startDate,
  timestampdiff('SQL_TSI_DAY', cast(t1.dateOnly as timestamp), dr.dateOnly) + 1 as daysElapsed,
  t1.enddate,
  t1.code,
  t1.treatmentRecord,
  t1.volume,
  t1.vol_units,
  t1.concentration,
  t1.conc_units,
  t1.amount,
  t1.amount_units,
  t1.amountWithUnits,
  t1.amountAndVolume,
  t1.dosage,
  t1.dosage_units,
  t1.route,
  IFDEFINED(t1.reason) AS reason,
  t1.performedby,
  t1.remark,
  t1.caseid,

  t1.qcstate

FROM nirc_ehr.dateRange dr

JOIN study."Treatment Orders" t1
  --NOTE: should the enddate consider date/time?
  ON (dr.dateOnly >= t1.dateOnly and dr.dateOnly <= t1.enddateCoalesced AND
      --technically the first day of the treatment is day 1, not day 0
  ((mod(CAST(timestampdiff('SQL_TSI_DAY', CAST(t1.dateOnly as timestamp), dr.dateOnly) as integer), t1.frequency.intervalindays) = 0 And t1.frequency.intervalindays is not null And t1.frequency.dayofweek is null ))
)

LEFT JOIN ehr.treatment_times tt ON (tt.treatmentid = t1.objectid)
LEFT JOIN ehr_lookups.treatment_frequency_times ft ON (ft.frequency = t1.frequency.meaning AND tt.rowid IS NULL)

LEFT JOIN (
    SELECT
      sc.code
    from ehr_lookups.snomed_subset_codes sc
    WHERE sc.primaryCategory = 'Diet'
    GROUP BY sc.code
) snomed ON snomed.code = t1.code

--NOTE: if we run this report on a future interval, we want to include those treatments
WHERE t1.date is not null
--NOTE: they have decided to include non-public data
--AND t1.qcstate.publicdata = true --and t1.dateOnly <= curdate()

) s1

) s ON (s.animalid = d.id) LEFT JOIN
study.drug drug ON s.objectid = drug.treatmentid AND s.date = IFDEFINED(drug.scheduledDate)

WHERE (d.lastDayatCenter Is Null or d.lastDayAtCenter > s.enddate OR s.enddate IS NULL)


--account for date/time in schedule
and s.date >= s.startDate and (s.enddate IS NULL OR s.date <= s.enddate)