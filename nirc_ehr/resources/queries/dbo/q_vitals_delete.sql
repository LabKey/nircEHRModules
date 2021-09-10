SELECT substring(adt.PRIMARY_KEY_VALUES, length('ANIMAL_EVENT_ID = ')) AS objectid,
       CAST(adt.CHANGE_DATETIME AS TIMESTAMP) AS modified,
       adt.REFERENCE
FROM AUDIT_TRAIL adt
WHERE adt.PRIMARY_KEY_VALUES LIKE '%ANIMAL_EVENT_ID%' AND
    (adt.REFERENCE LIKE '%Blood Pressure-NHP%' OR
     adt.REFERENCE LIKE '%Heart Rate-NHP%' OR
     adt.REFERENCE LIKE '%Respiration Rate-NHP%' OR
     adt.REFERENCE LIKE '%Temperature-NHP%' OR
     adt.REFERENCE LIKE '%Pulse Rate-NHP%' ) AND
    adt.COLUMN_NAME = 'DELETE'