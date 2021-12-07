SELECT substring(adt.PRIMARY_KEY_VALUES, length('PROTOCOL_ID = ')) AS protocol,
       CAST(adt.CHANGE_DATETIME AS TIMESTAMP)                      AS modified,
       adt.REFERENCE
FROM AUDIT_TRAIL adt
WHERE adt.TABLE_NAME = 'PROTOCOL'
       AND adt.COLUMN_NAME = 'DELETE'