-- Used for departure ETL and in q_status.sql
SELECT anmEvt.ANIMAL_EVENT_ID AS objectid,
       anm.ANIMAL_ID_NUMBER  AS Id,
       anm.ANIMAL_DISPOSITION_ID AS destination,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP) AS eventDate,
       evt.NAME AS description,
       (CASE
            WHEN anmEvt.STAFF_ID.EMAIL_ADDRESS IS NULL THEN 'unknown'
            ELSE substring(anmEvt.STAFF_ID.EMAIL_ADDRESS, 1,
                           locate('@', anmEvt.STAFF_ID.EMAIL_ADDRESS) - 1) END)     AS performedby,
       CAST(COALESCE(adt.modified, anmEvt.CREATED_DATETIME) AS TIMESTAMP)           AS modified
FROM ANIMAL_EVENT anmEvt
LEFT JOIN ANIMAL anm ON anmEvt.ANIMAL_ID = anm.ANIMAL_ID
LEFT JOIN EVENT evt ON anmEvt.EVENT_ID = evt.EVENT_ID
LEFT JOIN q_modified_event adt ON anmEvt.ANIMAL_EVENT_ID = adt.event_id
WHERE anmEvt.EVENT_ID IN (SELECT EVENT_ID FROM EVENT WHERE NAME LIKE 'Lab Transfer To%')