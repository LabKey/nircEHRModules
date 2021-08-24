
SELECT anmEvt.ANIMAL_EVENT_ID as objectid,
       anm.ANIMAL_ID_NUMBER AS Id,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP) AS weightDate,
       CAST(COALESCE (adt.CHANGE_DATETIME,anmEvt.CREATED_DATETIME) AS TIMESTAMP) AS modified,
       anmEvt.result AS weight
FROM ANIMAL_EVENT anmEvt
LEFT JOIN ANIMAL anm ON anmEvt.ANIMAL_ID = anm.ANIMAL_ID
LEFT JOIN AUDIT_TRAIL adt ON anmEvt.ANIMAL_EVENT_ID = substring(adt.PRIMARY_KEY_VALUES, length('ANIMAL_EVENT_ID = '))
    AND adt.PRIMARY_KEY_VALUES LIKE '%ANIMAL_EVENT_ID%'
    AND adt.COLUMN_NAME != 'DELETE'
WHERE anmEvt.EVENT_ID = 1349 -- Body weight
  AND anmEvt.CREATED_DATETIME < now() -- there are rows in ANIMAL_EVENT table with future dates