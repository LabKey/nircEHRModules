SELECT anmEvt.ANIMAL_EVENT_ID                                                    AS objectid,
       anmEvt.ANIMAL_ID                                                          AS Id,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP)                                  AS administrationDate,
       staff.email_prefix                                                        AS performedby,
       anmEvt.EVENT_ID.NAME                                                      AS type,
       anmEvt.RESULT                                                             AS amount,
       anmEvt.ATTACHMENT_PATH                                                    AS attachmentFile,
       anmCmt.TEXT                                                               AS remark,
       CAST(COALESCE(max(adt.CHANGE_DATETIME), anmEvt.CREATED_DATETIME) AS TIMESTAMP) AS modified
FROM ANIMAL_EVENT anmEvt
         LEFT JOIN staffInfo staff ON staff.staff_id = anmEvt.STAFF_ID
         LEFT JOIN ANIMAL_EVENT_COMMENT anmCmt ON anmEvt.ANIMAL_EVENT_ID = anmCmt.ANIMAL_EVENT_ID
         LEFT JOIN EVENT_EVENT_GROUP evtEvtGrp ON evtEvtGrp.EVENT_ID = anmEvt.EVENT_ID
         LEFT JOIN AUDIT_TRAIL adt
                   ON anmEvt.ANIMAL_EVENT_ID = substring(adt.PRIMARY_KEY_VALUES, length('ANIMAL_EVENT_ID = '))
                       AND adt.PRIMARY_KEY_VALUES LIKE '%ANIMAL_EVENT_ID%'

WHERE (evtEvtGrp.EVENT_GROUP_ID = 31 AND evtEvtGrp.EVENT_ID = 2261) -- Sedation from Timed Procedures
   OR evtEvtGrp.EVENT_GROUP_ID IN (3, 25, 34, 39, 51, 54, 65)
--     3	Vaccinations
--     25	Dose Administration
--     34	Medication Administration
--     39	Behavioral Treatment
--     51	Oral Dose Administration - Alert
--     54	Fluid Administration
--     65	Sedation
AND anmEvt.CREATED_DATETIME < now() -- there are rows in ANIMAL_EVENT table with future dates

-- Joining with event_event_group & audit_trail generates duplicate rows, hence the 'group by'
GROUP BY anmEvt.ANIMAL_EVENT_ID,
         anmEvt.ANIMAL_ID,
         anmEvt.EVENT_DATETIME,
         staff.email_prefix,
         anmEvt.EVENT_ID.NAME,
         anmEvt.RESULT,
         anmEvt.ATTACHMENT_PATH,
         anmCmt.TEXT,
         anmEvt.CREATED_DATETIME