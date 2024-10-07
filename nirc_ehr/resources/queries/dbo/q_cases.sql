SELECT * FROM (
    SELECT anmEvt.ANIMAL_EVENT_ID                                                 as objectid,
          anm.ANIMAL_ID_NUMBER                                                   AS Id,
          CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP)                               AS caseDate,
          CAST(COALESCE(adt.modified, anmEvt.CREATED_DATETIME) AS TIMESTAMP)     AS modified,
          (CASE
               WHEN (trim(anmEvt.STAFF_ID.STAFF_FIRST_NAME) IS NULL OR trim(anmEvt.STAFF_ID.STAFF_LAST_NAME) IS NULL)
                   THEN 'unknown'
               ELSE (trim(anmEvt.STAFF_ID.STAFF_FIRST_NAME)
                   || '|' || trim(anmEvt.STAFF_ID.STAFF_LAST_NAME)) END)               AS performedby,
          (CASE WHEN anmEvt.EVENT_ID = 1580 THEN anmCmt.TEXT ELSE NULL END)      AS openRemark,
          (CASE WHEN anmEvt.EVENT_ID = 1677 THEN anmCmt.TEXT ELSE NULL END)      AS closeRemark,
          (CASE WHEN anmEvt.EVENT_ID = 1580 THEN anmEvt.DIAGNOSIS ELSE NULL END) AS openDiagnosis,
          (CASE WHEN anmEvt.EVENT_ID = 1677 THEN anmEvt.DIAGNOSIS ELSE NULL END) AS closeDiagnosis,
          COALESCE(dea.deathDate, dep.eventDate)                                 AS enddate,
          anmEvt.EVENT_ID.NAME                                                   AS category,
          CASE
              WHEN anmEvt.ATTACHMENT_PATH IS NOT NULL THEN
                  ('C:\Program Files\Labkey\labkey\files\NIRC\EHR\@files\attachments'
                      || substring(anmEvt.ATTACHMENT_PATH, LENGTH('N:\'), LENGTH(anmEvt.ATTACHMENT_PATH)))
              ELSE NULL END                                                      AS attachmentFile
       FROM ANIMAL_EVENT anmEvt
            LEFT JOIN staffInfo staff ON staff.staff_id = anmEvt.STAFF_ID
            LEFT JOIN ANIMAL anm ON anmEvt.ANIMAL_ID = anm.ANIMAL_ID
            LEFT JOIN ANIMAL_EVENT_COMMENT anmCmt ON anmEvt.ANIMAL_EVENT_ID = anmCmt.ANIMAL_EVENT_ID
            LEFT JOIN q_modified_event adt ON anmEvt.ANIMAL_EVENT_ID = adt.event_id
            LEFT JOIN q_deaths dea ON anm.ANIMAL_ID_NUMBER = dea.participantId
            LEFT JOIN q_finalDeparture dep ON anm.ANIMAL_ID_NUMBER = dep.Id
       WHERE anmEvt.EVENT_ID = 1580
          OR anmEvt.EVENT_ID = 1677 -- 1580 Presenting Diagnosis, 1677 Clinical Resolution
           AND anmEvt.CREATED_DATETIME < now() -- there are rows in ANIMAL_EVENT table with future dates
      ) cas
ORDER BY cas.Id DESC, cas.caseDate DESC, cas.category ASC