SELECT anmEvt.ANIMAL_EVENT_ID                                                    AS objectid,
       anmEvt.ANIMAL_ID                                                          AS Id,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP)                                  AS administrationDate,
       (CASE
            WHEN anmEvt.STAFF_ID.EMAIL_ADDRESS IS NULL THEN 'unknown'
            ELSE substr(anmEvt.STAFF_ID.EMAIL_ADDRESS, 1,
                        instr(anmEvt.STAFF_ID.EMAIL_ADDRESS, '@') - 1) END)      AS performedby,
       anmEvt.EVENT_ID.NAME                                                      AS type,
       anmEvt.TEXT_RESULT                                                        AS result,
       anmEvt.ATTACHMENT_PATH                                                    AS attachmentFile,
       anmCmt.TEXT                                                               AS remark,
       CAST(COALESCE(adt.CHANGE_DATETIME, anmEvt.CREATED_DATETIME) AS TIMESTAMP) AS modified
FROM ANIMAL_EVENT anmEvt
         LEFT JOIN ANIMAL_EVENT_COMMENT anmCmt ON anmEvt.ANIMAL_EVENT_ID = anmCmt.ANIMAL_EVENT_ID
         LEFT JOIN EVENT_EVENT_GROUP evtEvtGrp ON evtEvtGrp.EVENT_ID = anmEvt.EVENT_ID
         LEFT JOIN AUDIT_TRAIL adt
                   ON anmEvt.ANIMAL_EVENT_ID = substring(adt.PRIMARY_KEY_VALUES, length('ANIMAL_EVENT_ID = '))
                       AND adt.PRIMARY_KEY_VALUES LIKE '%ANIMAL_EVENT_ID%'

WHERE evtEvtGrp.EVENT_GROUP_ID = 56
--     56 Breeder