SELECT anmEvt.ANIMAL_EVENT_ID                                                    AS objectid,
       anmEvt.ANIMAL_ID.ANIMAL_ID_NUMBER                                         AS Id,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP)                                  AS administrationDate,
       (CASE
            WHEN anmEvt.STAFF_ID.EMAIL_ADDRESS IS NULL THEN 'unknown'
            ELSE substr(anmEvt.STAFF_ID.EMAIL_ADDRESS, 1,
                        instr(anmEvt.STAFF_ID.EMAIL_ADDRESS, '@') - 1) END)      AS performedby,
       anmEvt.EVENT_ID.EVENT_ID                                                  AS category,
       anmEvt.DIAGNOSIS                                                          AS hx,
       anmCmt.TEXT                                                               AS remark,
       CAST(COALESCE(adt.modified, anmEvt.CREATED_DATETIME) AS TIMESTAMP) AS modified
FROM ANIMAL_EVENT anmEvt
         LEFT JOIN ANIMAL_EVENT_COMMENT anmCmt ON anmEvt.ANIMAL_EVENT_ID = anmCmt.ANIMAL_EVENT_ID
         LEFT JOIN EVENT_EVENT_GROUP evtEvtGrp ON evtEvtGrp.EVENT_ID = anmEvt.EVENT_ID
         LEFT JOIN q_modified_event adt ON anmEvt.ANIMAL_EVENT_ID = adt.event_id

WHERE evtEvtGrp.EVENT_GROUP_ID = 53
  AND anmEvt.CREATED_DATETIME < now() -- there are rows in ANIMAL_EVENT table with future dates
--     53 Comments