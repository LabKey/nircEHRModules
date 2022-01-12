SELECT alt.ALTERNATE_ID AS "objectId",
       anm.ANIMAL_ID,
       anm.ANIMAL_ID_NUMBER AS "Id",
       alt.NAME AS "projectName",
       alt.NAME AS "description",
       alt.DESCRIPTION AS "changeDateText",
       COALESCE(MAX(CAST(adt.CHANGE_DATETIME AS TIMESTAMP)), to_date('01/01/1970' ,'MM/DD/YYYY')) AS modified
FROM  ALTERNATE alt
LEFT JOIN ANIMAL anm ON alt.ANIMAL_ID = anm.ANIMAL_ID
LEFT JOIN AUDIT_TRAIL adt ON alt.ALTERNATE_ID = substring(PRIMARY_KEY_VALUES, length('Alternate_ID = '))
    AND adt.TABLE_NAME = 'ALTERNATE'
WHERE alt.ALTERNATE_TYPE_ID = 6 -- Project Inventory Number for project transfers
GROUP BY alt.ALTERNATE_ID,
         anm.ANIMAL_ID,
         anm.ANIMAL_ID_NUMBER,
         alt.NAME,
         alt.DESCRIPTION
ORDER BY anm.ANIMAL_ID,alt.ALTERNATE_ID ASC