
SELECT substring(adt.PRIMARY_KEY_VALUES, 18) AS objectid,
       CAST(adt.CHANGE_DATETIME AS TIMESTAMP) AS modified
FROM AUDIT_TRAIL adt
WHERE adt.PRIMARY_KEY_VALUES LIKE '%ANIMAL_EVENT_ID%' AND
      adt.REFERENCE LIKE '%Blood Sample Collection (with Volume)%' AND
      adt.COLUMN_NAME = 'DELETE'

