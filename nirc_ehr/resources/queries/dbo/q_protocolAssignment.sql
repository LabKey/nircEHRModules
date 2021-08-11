
SELECT anm.ANIMAL_ID AS "objectId",
       anmEvt.ANIMAL_EVENT_ID AS "animalEventId",
       anm.ANIMAL_ID_NUMBER AS Id,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP) AS transferDate,
       REPLACE(anmCmt.TEXT, ';', ':') AS remark,
       anmEvt.LOCATION_ID AS location
FROM ANIMAL_EVENT anmEvt
         LEFT JOIN ANIMAL anm ON anmEvt.ANIMAL_ID = anm.ANIMAL_ID
         LEFT JOIN ANIMAL_EVENT_COMMENT anmCmt ON anmEvt.ANIMAL_EVENT_ID = anmCmt.ANIMAL_EVENT_ID
WHERE anmEvt.EVENT_ID = 2 --Animal Transfer
  AND anmCmt.TEXT LIKE '%Pro%'
  ORDER BY anm.ANIMAL_ID_NUMBER,anmEvt.ANIMAL_EVENT_ID DESC