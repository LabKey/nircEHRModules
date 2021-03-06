SELECT an.ANIMAL_ID_NUMBER AS Id,
       CAST(adt.CHANGE_DATETIME AS TIMESTAMP)                    AS modified,
       adt.REFERENCE
FROM AUDIT_TRAIL adt
LEFT JOIN ANIMAL an ON an.ANIMAL_ID = substring(adt.PRIMARY_KEY_VALUES, length('ANIMAL_ID = '))
WHERE adt.TABLE_NAME = 'ANIMAL' AND adt.COLUMN_NAME = 'DELETE'