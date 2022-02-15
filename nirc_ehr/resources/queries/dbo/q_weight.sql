
SELECT anmEvt.ANIMAL_EVENT_ID as objectid,
       anm.ANIMAL_ID_NUMBER AS Id,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP) AS weightDate,
       CAST(COALESCE (adt.modified,anmEvt.CREATED_DATETIME) AS TIMESTAMP) AS modified,
       (CASE
            WHEN (anmEvt.STAFF_ID.STAFF_FIRST_NAME IS NULL OR anmEvt.STAFF_ID.STAFF_LAST_NAME IS NULL) THEN 'unknown'
            ELSE (anmEvt.STAFF_ID.STAFF_FIRST_NAME
                || '|' || anmEvt.STAFF_ID.STAFF_LAST_NAME) END)                  AS performedby,
       anmEvt.result AS weight,
       ev.NUMERIC_UNIT_ID   AS Units
FROM ANIMAL_EVENT anmEvt
LEFT JOIN ANIMAL anm ON anmEvt.ANIMAL_ID = anm.ANIMAL_ID
LEFT JOIN EVENT ev ON anmEvt.EVENT_ID = ev.EVENT_ID
LEFT JOIN q_modified_event adt ON anmEvt.ANIMAL_EVENT_ID = adt.event_id
WHERE anmEvt.EVENT_ID = 1349 -- Body weight
  AND anmEvt.CREATED_DATETIME < now() -- there are rows in ANIMAL_EVENT table with future dates