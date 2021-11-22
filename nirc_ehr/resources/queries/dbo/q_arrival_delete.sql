SELECT 'ALT' || substring(adt.PRIMARY_KEY_VALUES, length('Alternate_ID = ')) AS objectid,
       CAST(adt.CHANGE_DATETIME AS TIMESTAMP)                            AS modified,
       adt.REFERENCE
FROM AUDIT_TRAIL adt
WHERE adt.TABLE_NAME = 'ALTERNATE' AND
        adt.COLUMN_NAME = 'DELETE'

UNION

SELECT substring(adt.PRIMARY_KEY_VALUES, length('ANIMAL_EVENT_ID = ')) AS objectid,
       CAST(adt.CHANGE_DATETIME AS TIMESTAMP)                            AS modified,
       adt.REFERENCE
FROM AUDIT_TRAIL adt
WHERE adt.PRIMARY_KEY_VALUES LIKE '%ANIMAL_EVENT_ID%' AND
    (adt.REFERENCE LIKE '%Lab Transfer Fr%') AND
        adt.COLUMN_NAME = 'DELETE'