SELECT substring(adt.PRIMARY_KEY_VALUES, length('ANIMAL_EVENT_ID = ')) AS objectid,
       CAST(adt.CHANGE_DATETIME AS TIMESTAMP) AS modified,
       adt.REFERENCE
FROM AUDIT_TRAIL adt
WHERE adt.PRIMARY_KEY_VALUES LIKE '%ANIMAL_EVENT_ID%' AND
    (adt.REFERENCE LIKE '%General Condition%' OR
     adt.REFERENCE LIKE '%Assessment%' OR
     adt.REFERENCE LIKE '%Specific System Exam%' OR
     adt.REFERENCE LIKE '%Blood Pressure%' OR
     adt.REFERENCE LIKE '%Heart%' OR
     adt.REFERENCE LIKE '%Heart%' OR
     adt.REFERENCE LIKE '%Heart%' OR
     adt.REFERENCE LIKE '%Heart%' OR
     adt.REFERENCE LIKE '%Pulse Rate-NHP%' ) AND
    adt.COLUMN_NAME = 'DELETE'