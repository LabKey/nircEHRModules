SELECT anmEvt.ANIMAL_EVENT_ID                                                    AS objectid,
       anmEvt.ANIMAL_ID                                                          AS Id,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP)                                  AS administrationDate,
       (CASE
            WHEN anmEvt.STAFF_ID.EMAIL_ADDRESS IS NULL THEN 'unknown'
            ELSE substr(anmEvt.STAFF_ID.EMAIL_ADDRESS, 1,
                        instr(anmEvt.STAFF_ID.EMAIL_ADDRESS, '@') - 1) END)      AS performedby,
       anmEvt.EVENT_ID.NAME                                                      AS type,
       anmEvt.TEXT_RESULT                                                        AS result,
       anmEvt.DIAGNOSIS                                                          AS diagnosis,
       anmEvt.ATTACHMENT_PATH                                                    AS attachmentFile,
       anmCmt.TEXT                                                               AS remark,
       CAST(COALESCE(max(adt.CHANGE_DATETIME), anmEvt.CREATED_DATETIME) AS TIMESTAMP) AS modified
FROM ANIMAL_EVENT anmEvt
         LEFT JOIN ANIMAL_EVENT_COMMENT anmCmt ON anmEvt.ANIMAL_EVENT_ID = anmCmt.ANIMAL_EVENT_ID
         LEFT JOIN EVENT_EVENT_GROUP evtEvtGrp ON evtEvtGrp.EVENT_ID = anmEvt.EVENT_ID
         LEFT JOIN AUDIT_TRAIL adt
                   ON anmEvt.ANIMAL_EVENT_ID = substring(adt.PRIMARY_KEY_VALUES, length('ANIMAL_EVENT_ID = '))
                       AND adt.PRIMARY_KEY_VALUES LIKE '%ANIMAL_EVENT_ID%'

WHERE evtEvtGrp.EVENT_ID IN (2170, 2171)
--     2170	Abort
--     2171	Stillborn
    AND anmEvt.CREATED_DATETIME < now() -- there are rows in ANIMAL_EVENT table with future dates

-- Joining with event_event_group & audit_trail generates duplicate rows, hence the 'group by'
GROUP BY anmEvt.ANIMAL_EVENT_ID,
         anmEvt.ANIMAL_ID,
         anmEvt.EVENT_DATETIME,
         anmEvt.STAFF_ID.EMAIL_ADDRESS,
         anmEvt.EVENT_ID.NAME,
         anmEvt.TEXT_RESULT,
         anmEvt.DIAGNOSIS,
         anmEvt.ATTACHMENT_PATH,
         anmCmt.TEXT,
         anmEvt.CREATED_DATETIME,
         adt.CHANGE_DATETIME