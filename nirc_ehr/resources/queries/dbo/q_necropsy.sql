SELECT anmEvt.ANIMAL_EVENT_ID as objectid,
       anm.ANIMAL_ID_NUMBER AS Id,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP) AS necropsyDate,
       (CAST(COALESCE (adt.modified, anmEvt.CREATED_DATETIME) AS TIMESTAMP)) AS modified,
       (CASE
            WHEN (anmEvt.STAFF_ID.STAFF_FIRST_NAME IS NULL OR anmEvt.STAFF_ID.STAFF_LAST_NAME IS NULL) THEN 'unknown'
            ELSE (trim(anmEvt.STAFF_ID.STAFF_FIRST_NAME)
                || '|' || trim(anmEvt.STAFF_ID.STAFF_LAST_NAME)) END)                  AS performedby,
       anmCmt.TEXT AS remark,
       anmEvt.DIAGNOSIS as diagnosis,
       anmEvt.EVENT_ID.NAME AS category,
       CASE WHEN anmEvt.ATTACHMENT_PATH IS NOT NULL THEN
            ('C:\Program Files\Labkey\labkey\files\NIRC\EHR\@files\attachments'
                || substring(anmEvt.ATTACHMENT_PATH, LENGTH('N:\'), LENGTH(anmEvt.ATTACHMENT_PATH)))
       ELSE NULL END AS attachmentFile
FROM ANIMAL_EVENT anmEvt
         LEFT JOIN ANIMAL anm ON anmEvt.ANIMAL_ID = anm.ANIMAL_ID
         LEFT JOIN ANIMAL_EVENT_COMMENT anmCmt ON anmEvt.ANIMAL_EVENT_ID = anmCmt.ANIMAL_EVENT_ID
         LEFT JOIN q_modified_event adt ON anmEvt.ANIMAL_EVENT_ID = adt.event_id
WHERE anmEvt.EVENT_ID = 1575 -- 1575 Necropsy
  AND anmEvt.CREATED_DATETIME < now() -- there are rows in ANIMAL_EVENT table with future dates
