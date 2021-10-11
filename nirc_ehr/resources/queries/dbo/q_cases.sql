SELECT anmEvt.ANIMAL_EVENT_ID                                                    as objectid,
       anm.ANIMAL_ID_NUMBER                                                      AS Id,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP)                                  AS caseDate,
       CAST(COALESCE(max(adt.CHANGE_DATETIME), anmEvt.CREATED_DATETIME) AS TIMESTAMP) AS modified,
       anmCmt.TEXT                                                               AS remark,
       anmEvt.DIAGNOSIS                                                          AS diagnosis,
       anmEvt.EVENT_ID.NAME                                                      AS category,
       staff.email_prefix                                                        AS performedby,
       anmEvt.ATTACHMENT_PATH                                                    AS attachmentFile
FROM ANIMAL_EVENT anmEvt
         LEFT JOIN staffInfo staff ON staff.staff_id = anmEvt.STAFF_ID
         LEFT JOIN ANIMAL anm ON anmEvt.ANIMAL_ID = anm.ANIMAL_ID
         LEFT JOIN ANIMAL_EVENT_COMMENT anmCmt ON anmEvt.ANIMAL_EVENT_ID = anmCmt.ANIMAL_EVENT_ID
         LEFT JOIN EVENT_EVENT_GROUP evtEvtGrp ON evtEvtGrp.EVENT_ID = anmEvt.EVENT_ID
         LEFT JOIN AUDIT_TRAIL adt
                   ON anmEvt.ANIMAL_EVENT_ID = substring(adt.PRIMARY_KEY_VALUES, length('ANIMAL_EVENT_ID = '))
                       AND adt.PRIMARY_KEY_VALUES LIKE '%ANIMAL_EVENT_ID%'
WHERE evtEvtGrp.EVENT_GROUP_ID = 37   -- 37 Clinical Treatment

  AND anmEvt.CREATED_DATETIME < now() -- there are rows in ANIMAL_EVENT table with future dates

-- Joining with audit_trail generates duplicate rows (ex. having multiple audit records for a given ANIMAL_EVENT_ID with the same CHANGE_DATETIME), hence the 'group by'
GROUP BY
     anmEvt.ANIMAL_EVENT_ID,
     anm.ANIMAL_ID_NUMBER,
     anmEvt.EVENT_DATETIME,
     staff.email_prefix,
     anmEvt.CREATED_DATETIME,
     anmCmt.TEXT,
     anmEvt.DIAGNOSIS,
     anmEvt.EVENT_ID.NAME,
     staff.email_prefix,
     anmEvt.ATTACHMENT_PATH