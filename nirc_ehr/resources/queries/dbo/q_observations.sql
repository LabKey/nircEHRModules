
SELECT anmEvt.ANIMAL_EVENT_ID as objectid,
       anm.ANIMAL_ID_NUMBER AS Id,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP) AS obsDate,
       CAST(COALESCE (adt.CHANGE_DATETIME, anmEvt.CREATED_DATETIME) AS TIMESTAMP) AS modified,
       anmEvt.RESULT AS quantity,
       anmCmt.TEXT AS remark
FROM ANIMAL_EVENT anmEvt
         LEFT JOIN ANIMAL anm ON anmEvt.ANIMAL_ID = anm.ANIMAL_ID
         LEFT JOIN ANIMAL_EVENT_COMMENT anmCmt ON anmEvt.ANIMAL_EVENT_ID = anmCmt.ANIMAL_EVENT_ID
         LEFT JOIN AUDIT_TRAIL adt ON anmEvt.ANIMAL_EVENT_ID = substring(adt.PRIMARY_KEY_VALUES, 18) -- ANIMAL_EVENT_ID =xxxxxx
    AND adt.PRIMARY_KEY_VALUES LIKE '%ANIMAL_EVENT_ID%'
WHERE anmEvt.EVENT_ID = 1656 --Normal observations
  AND anmEvt.CREATED_DATETIME < now() -- there are rows in ANIMAL_EVENT table with future dates