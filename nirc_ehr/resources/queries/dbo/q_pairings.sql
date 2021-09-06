
SELECT anmEvt.ANIMAL_EVENT_ID as objectid,
       anm.ANIMAL_ID_NUMBER AS Id,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP) AS pairingDate,
       CAST(COALESCE (adt.CHANGE_DATETIME, anmEvt.CREATED_DATETIME) AS TIMESTAMP) AS modified,
       anmCmt.TEXT AS remark,
       anmEvt.EVENT_ID.NAME AS type,
       adt.AUDIT_ID
FROM ANIMAL_EVENT anmEvt
         LEFT JOIN ANIMAL anm ON anmEvt.ANIMAL_ID = anm.ANIMAL_ID
         LEFT JOIN ANIMAL_EVENT_COMMENT anmCmt ON anmEvt.ANIMAL_EVENT_ID = anmCmt.ANIMAL_EVENT_ID
         LEFT JOIN EVENT_EVENT_GROUP evtEvtGrp ON evtEvtGrp.EVENT_ID = anmEvt.EVENT_ID
         LEFT JOIN AUDIT_TRAIL adt ON anmEvt.ANIMAL_EVENT_ID = substring(adt.PRIMARY_KEY_VALUES, length('ANIMAL_EVENT_ID = '))
    AND adt.PRIMARY_KEY_VALUES LIKE '%ANIMAL_EVENT_ID%'
WHERE evtEvtGrp.EVENT_GROUP_ID IN (42, 43, 62) --   Behavioral Assessment, Group/Pair Formation, Pair Housing Type
  AND anmEvt.CREATED_DATETIME < now() -- there are rows in ANIMAL_EVENT table with future dates
ORDER BY adt.AUDIT_ID ASC