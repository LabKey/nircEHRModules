SELECT anmEvt.ANIMAL_EVENT_ID   AS objectid,
       anm.ANIMAL_ID_NUMBER  AS Id,
       (CASE
            WHEN (anmEvt.STAFF_ID.STAFF_FIRST_NAME IS NULL OR anmEvt.STAFF_ID.STAFF_LAST_NAME IS NULL) THEN 'unknown'
            ELSE (anmEvt.STAFF_ID.STAFF_FIRST_NAME
                || '|' || anmEvt.STAFF_ID.STAFF_LAST_NAME) END)                  AS performedby,
       CAST(COALESCE(adt.modified, anmEvt.CREATED_DATETIME) AS TIMESTAMP) AS modified,
       anm.BIRTH_DATE        AS birth,
       anm.GENDER_ID         AS gender,
       anm.SSB_ID.SPECIES_ID            AS species,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP) AS eventDate,
       NULL                  AS acqDateText,
       'Animal Event'        AS source,
       Evt.NAME              AS sourceFacility,
       CASE WHEN anmEvt.ATTACHMENT_PATH IS NOT NULL THEN
                   ('C:\Program Files\Labkey\labkey\files\NIRC\EHR\@files\attachments'
                       || substring(anmEvt.ATTACHMENT_PATH, LENGTH('N:\'), LENGTH(anmEvt.ATTACHMENT_PATH)))
              ELSE NULL END AS attachmentFile
FROM ANIMAL_EVENT anmEvt
         LEFT JOIN ANIMAL anm ON anmEvt.ANIMAL_ID = anm.ANIMAL_ID
         LEFT JOIN q_modified_event adt ON anmEvt.ANIMAL_EVENT_ID = adt.event_id
         LEFT JOIN EVENT Evt ON anmEvt.EVENT_ID = Evt.EVENT_ID
WHERE anmEvt.EVENT_ID IN (SELECT EVENT_ID FROM EVENT WHERE NAME LIKE 'Lab Transfer Fr%')