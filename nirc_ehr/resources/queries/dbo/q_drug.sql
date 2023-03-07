SELECT anmEvt.ANIMAL_EVENT_ID                                                    AS objectid,
       anmEvt.ANIMAL_ID.ANIMAL_ID_NUMBER                                         AS Id,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP)                                  AS administrationDate,
       (CASE
            WHEN (anmEvt.STAFF_ID.STAFF_FIRST_NAME IS NULL OR anmEvt.STAFF_ID.STAFF_LAST_NAME IS NULL) THEN 'unknown'
            ELSE (anmEvt.STAFF_ID.STAFF_FIRST_NAME
                || '|' || anmEvt.STAFF_ID.STAFF_LAST_NAME) END)                  AS performedby,
       anmEvt.EVENT_ID.NAME                                                      AS type,
       anmEvt.RESULT                                                             AS amount,
       CASE WHEN anmEvt.ATTACHMENT_PATH IS NOT NULL THEN
                ('C:\Program Files\Labkey\labkey\files\NIRC\EHR\@files\attachments'
                    || substring(anmEvt.ATTACHMENT_PATH, LENGTH('N:\'), LENGTH(anmEvt.ATTACHMENT_PATH)))
            ELSE NULL END AS attachmentFile,
       (CASE WHEN (anmEvt.DIAGNOSIS IS NOT NULL AND anmCmt.TEXT IS NOT NULL) THEN (anmEvt.DIAGNOSIS || ', ' || anmCmt.TEXT) WHEN (anmEvt.DIAGNOSIS IS NOT NULL AND anmCmt.TEXT IS NULL) THEN (anmEvt.DIAGNOSIS) ELSE (anmCmt.TEXT) END) AS remark,
       CAST(COALESCE(adt.modified, anmEvt.CREATED_DATETIME) AS TIMESTAMP) AS modified
FROM ANIMAL_EVENT anmEvt
         LEFT JOIN ANIMAL_EVENT_COMMENT anmCmt ON anmEvt.ANIMAL_EVENT_ID = anmCmt.ANIMAL_EVENT_ID
         LEFT JOIN EVENT_EVENT_GROUP evtEvtGrp ON evtEvtGrp.EVENT_ID = anmEvt.EVENT_ID
         LEFT JOIN q_modified_event adt ON anmEvt.ANIMAL_EVENT_ID = adt.event_id

WHERE (evtEvtGrp.EVENT_GROUP_ID = 31 AND evtEvtGrp.EVENT_ID = 2261) -- Sedation from Timed Procedures
   OR evtEvtGrp.EVENT_GROUP_ID IN (3, 25, 34, 39, 51, 54, 65)
   AND anmEvt.CREATED_DATETIME < now() -- there are rows in ANIMAL_EVENT table with future dates
--     3	Vaccinations
--     25	Dose Administration
--     34	Medication Administration
--     39	Behavioral Treatment
--     51	Oral Dose Administration - Alert
--     54	Fluid Administration
--     65	Sedation