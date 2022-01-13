SELECT substring(adt.PRIMARY_KEY_VALUES, length('ANIMAL_EVENT_ID = ')) AS objectid,
       CAST(adt.CHANGE_DATETIME AS TIMESTAMP)                            AS modified,
       adt.REFERENCE
FROM AUDIT_TRAIL adt
WHERE adt.PRIMARY_KEY_VALUES LIKE '%ANIMAL_EVENT_ID%' AND
    (adt.REFERENCE LIKE '%Foster%') AND
        adt.COLUMN_NAME = 'DELETE'