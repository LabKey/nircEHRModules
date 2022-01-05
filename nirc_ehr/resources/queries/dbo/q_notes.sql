SELECT anmEvt.ANIMAL_EVENT_ID                                                    AS objectid,
       anmEvt.ANIMAL_ID.ANIMAL_ID_NUMBER                                         AS Id,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP)                                  AS noteDate,
       (CASE
            WHEN (anmEvt.STAFF_ID.STAFF_FIRST_NAME IS NULL OR anmEvt.STAFF_ID.STAFF_LAST_NAME IS NULL) THEN 'unknown'
            ELSE (anmEvt.STAFF_ID.STAFF_FIRST_NAME
                || '|' || anmEvt.STAFF_ID.STAFF_LAST_NAME) END)                  AS performedby,
       anmEvt.EVENT_ID.NAME                                                      AS type,
       anmEvt.ATTACHMENT_PATH                                                    AS attachmentFile,
       anmCmt.TEXT                                                               AS remark,
       CAST(COALESCE(adt.modified, anmEvt.CREATED_DATETIME) AS TIMESTAMP)        AS modified
FROM ANIMAL_EVENT anmEvt
         LEFT JOIN ANIMAL_EVENT_COMMENT anmCmt ON anmEvt.ANIMAL_EVENT_ID = anmCmt.ANIMAL_EVENT_ID
         LEFT JOIN EVENT_EVENT_GROUP evtEvtGrp ON evtEvtGrp.EVENT_ID = anmEvt.EVENT_ID
         LEFT JOIN q_modified_event adt ON anmEvt.ANIMAL_EVENT_ID = adt.event_id

WHERE evtEvtGrp.EVENT_GROUP_ID = 19
  AND anmEvt.EVENT_ID.NAME NOT LIKE 'Lab Transfer Fr%'
  AND anmEvt.EVENT_ID.NAME NOT LIKE 'Lab Transfer To%'
  AND anmEvt.EVENT_ID NOT IN (SELECT EVENT_ID FROM EVENT_EVENT_GROUP WHERE EVENT_GROUP_ID = 14) -- Already in serology
  AND anmEvt.CREATED_DATETIME < now() -- there are rows in ANIMAL_EVENT table with future dates
