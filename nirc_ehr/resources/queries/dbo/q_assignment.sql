SELECT alt.ALTERNATE_ID AS "objectId",
       anm.ANIMAL_ID,
       anm.ANIMAL_ID_NUMBER AS "Id",
       alt.NAME AS "projectName",
       alt.NAME AS "description",
       ae.EVENT_DATETIME AS assignmentDate,  -- Set to arrival/birth then updated by trigger
       COALESCE(alt.DESCRIPTION, CAST(ae.EVENT_DATETIME AS VARCHAR(100))) AS "changeDateText",
       COALESCE(MAX(CAST(adt.CHANGE_DATETIME AS TIMESTAMP)), ae.CREATED_DATETIME) AS modified
FROM  ALTERNATE alt
LEFT JOIN ANIMAL anm ON alt.ANIMAL_ID = anm.ANIMAL_ID
LEFT JOIN AUDIT_TRAIL adt ON alt.ALTERNATE_ID = substring(PRIMARY_KEY_VALUES, length('Alternate_ID = '))
    AND adt.TABLE_NAME = 'ALTERNATE'
LEFT JOIN ANIMAL_EVENT ae ON ae.ANIMAL_ID = anm.ANIMAL_ID AND EVENT_ID = 1  -- Received
WHERE alt.ALTERNATE_TYPE_ID = 6 -- Project Inventory Number for project transfers
GROUP BY alt.ALTERNATE_ID,
         anm.ANIMAL_ID,
         anm.ANIMAL_ID_NUMBER,
         alt.NAME,
         alt.DESCRIPTION,
         ae.CREATED_DATETIME,
         ae.EVENT_DATETIME
ORDER BY anm.ANIMAL_ID,alt.ALTERNATE_ID ASC