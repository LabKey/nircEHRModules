SELECT substring(adt.PRIMARY_KEY_VALUES, length('ANIMAL_ID = ')) AS Id,
       CAST(adt.CHANGE_DATETIME AS TIMESTAMP)                    AS modified,
       adt.REFERENCE
FROM AUDIT_TRAIL adt
WHERE adt.TABLE_NAME = 'ANIMAL' AND adt.COLUMN_NAME = 'DELETE'