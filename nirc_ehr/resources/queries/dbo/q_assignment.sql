SELECT alt.ALTERNATE_ID AS "objectId",
       anm.ANIMAL_ID,
       anm.ANIMAL_ID_NUMBER AS "Id",
       alt.NAME AS "projectName",
       alt.NAME AS "description",
       alt.DESCRIPTION AS "changeDateText",
FROM  ALTERNATE alt
LEFT JOIN ANIMAL anm ON alt.ANIMAL_ID = anm.ANIMAL_ID
WHERE alt.ALTERNATE_TYPE_ID = 6 -- Project Inventory Number for project transfers
ORDER BY anm.ANIMAL_ID,alt.ALTERNATE_ID ASC