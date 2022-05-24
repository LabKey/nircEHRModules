-- Used for departure ETL. There are some duplicate departures in ANIMAL_EVENTS thus the GROUP BY and MAX values
SELECT MAX(anmEvt.ANIMAL_EVENT_ID) AS objectid,
       anm.ANIMAL_ID_NUMBER  AS Id,
       anm.ANIMAL_DISPOSITION_ID AS destination,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP) AS eventDate,
       evt.NAME AS description,
       (CASE
            WHEN (MAX(anmEvt.STAFF_ID.STAFF_FIRST_NAME) IS NULL OR MAX(anmEvt.STAFF_ID.STAFF_LAST_NAME) IS NULL) THEN 'unknown'
            ELSE (MAX(anmEvt.STAFF_ID.STAFF_FIRST_NAME)
                || '|' || MAX(anmEvt.STAFF_ID.STAFF_LAST_NAME)) END)                  AS performedby,
       CAST(COALESCE(MAX(adt.modified), MAX(anmEvt.CREATED_DATETIME)) AS TIMESTAMP)           AS modified
FROM ANIMAL_EVENT anmEvt
LEFT JOIN ANIMAL anm ON anmEvt.ANIMAL_ID = anm.ANIMAL_ID
LEFT JOIN EVENT evt ON anmEvt.EVENT_ID = evt.EVENT_ID
LEFT JOIN q_modified_event adt ON anmEvt.ANIMAL_EVENT_ID = adt.event_id
WHERE anmEvt.EVENT_ID IN (SELECT EVENT_ID FROM EVENT WHERE NAME LIKE 'Lab Transfer To%')
GROUP BY
    anm.ANIMAL_ID_NUMBER,
    anm.ANIMAL_DISPOSITION_ID,
    anmEvt.EVENT_DATETIME,
    evt.NAME
