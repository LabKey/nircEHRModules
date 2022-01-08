SELECT substring(adt.PRIMARY_KEY_VALUES, length('Alternate_ID = ')) AS objectid,
       CAST(adt.CHANGE_DATETIME AS TIMESTAMP)                            AS modified,
       adt.REFERENCE
FROM AUDIT_TRAIL adt
WHERE adt.TABLE_NAME = 'ALTERNATE' AND
        adt.COLUMN_NAME = 'DELETE'