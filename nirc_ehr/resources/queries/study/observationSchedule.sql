SELECT
    g.id,
    g.scheduledDate,
    COUNT(g.caseid) cases,
    COUNT(g.taskid) orders,
    g.observations,
    GROUP_CONCAT(g.status, ';') as status,
    MAX(g.taskid) as taskid,
    MAX(g.type) as type,
    MAX(g.caseid) as caseid
FROM
(
    SELECT
        sr.id,
        sr.taskid,
        sr.scheduledDate,
        sr.caseid,
        sr.type,
        CASE WHEN sr.observations = 'Activity; Appetite; BCS; Hydration; Stool; Verified Id?'
            THEN javaConstant('org.labkey.nirc_ehr.NIRC_EHRManager.DAILY_CLINICAL_OBS_TITLE')
            ELSE sr.observations
            END as observations,
        sr.obsCount,
        sr.statusCount,
        sr.status
    FROM
    (
    SELECT
        sch.id,
        sch.taskid,
        sch.date as scheduledDate,
        sch.caseid,
        sch.type,
        GROUP_CONCAT(sch.category, '; ') as observations,
        GROUP_CONCAT(obsStatus, ';') as status,
        COUNT(sch.category) as obsCount,
        COUNT(sch.obsStatus) as statusCount
    FROM
        (
        SELECT
            d.id,
            s.*,
            s.category,
            co.qcstate.label as obsStatus
        FROM study.demographics d JOIN
        (SELECT
            s1.*,
            timestampadd('SQL_TSI_MINUTE', ((s1.hours * 60) + s1.minutes), s1.origDate) as date,
            ((s1.hours * 60) + s1.minutes) as timeOffset

        FROM (

        SELECT
            t1.lsid,
            t1.dataset,
            t1.id as animalid,

            coalesce(ft.hourofday, ((hour(t1.date) * 100) + minute(t1.date))) as time,
            (coalesce(ft.hourofday, (hour(t1.date) * 100)) / 100) as hours,

            CASE WHEN ft.hourofday IS NOT NULL
            THEN (((ft.hourofday / 100.0) - floor(ft.hourofday / 100)) * 100)
            ELSE minute(t1.date)
            END as minutes,

            dr.date as origDate,

            t1.frequency.meaning as frequency,
            t1.date as startDate,
            timestampdiff('SQL_TSI_DAY', cast(t1.dateOnly as timestamp), dr.dateOnly) + 1 as daysElapsed,
            t1.enddate,
            t1.category,
            t1.area,
            t1.performedby,
            t1.remark,
            t1.caseid,
            t1.taskid,
            t1.type,

            t1.qcstate

        FROM nirc_ehr.dateRange dr

        JOIN

        -- order by category to replace string with Daily Obs
        (SELECT * FROM
            study.observation_order
        ORDER BY category) t1
            ON (dr.dateOnly >= t1.dateOnly and dr.dateOnly <= t1.enddateCoalesced AND
              --technically the first day of the treatment is day 1, not day 0
            ((mod(CAST(timestampdiff('SQL_TSI_DAY', CAST(t1.dateOnly as timestamp), dr.dateOnly) as integer), t1.frequency.intervalindays) = 0 And t1.frequency.intervalindays is not null And t1.frequency.dayofweek is null)))

        LEFT JOIN ehr_lookups.treatment_frequency_times ft ON ft.frequency = t1.frequency.meaning

        WHERE t1.date is not null AND t1.qcstate.publicdata = true

        ) s1

        ) s ON (s.animalid = d.id)
        LEFT JOIN study.clinical_observations co ON s.category = co.category AND co.scheduledDate IS NOT NULL AND s.date = co.scheduledDate AND co.id = s.animalid
    ) sch
    GROUP BY
        sch.id,
        sch.taskid,
        sch.date,
        sch.caseid,
        sch.type
    ) sr
) g
GROUP BY
    g.id,
    g.scheduledDate,
    g.observations
