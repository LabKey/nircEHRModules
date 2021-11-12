
SELECT anm.ANIMAL_ID_NUMBER AS Id,
       anm.BIRTH_DATE       AS birth,
       anm.GENDER_ID        AS gender,
       anm.SSB_ID           AS species,
       NULL                 AS eventDate,
       alt.NAME AS damSire,
       alt.DESCRIPTION AS acqDateText,
       'Alternate'          AS source
FROM  ALTERNATE alt
LEFT JOIN ANIMAL anm ON alt.ANIMAL_ID = anm.ANIMAL_ID
WHERE alt.ALTERNATE_TYPE_ID = 7 -- 'Dam/Sire/Acq'
  AND anm.ANIMAL_ID_NUMBER NOT LIKE 'A%' -- Animal born in centers are pre-appended with A's (in animal Id)
  AND alt.DESCRIPTION IS NOT NULL

UNION

SELECT anm.ANIMAL_ID_NUMBER  AS Id,
       anm.BIRTH_DATE        AS birth,
       anm.GENDER_ID         AS gender,
       anm.SSB_ID            AS species,
       anmEvt.EVENT_DATETIME AS eventDate,
       NULL                  AS damSire,
       NULL                  AS acqDateText,
       'Animal Event'        AS source
FROM ANIMAL_EVENT anmEvt
LEFT JOIN ANIMAL anm ON anmEvt.ANIMAL_ID = anm.ANIMAL_ID
WHERE EVENT_ID IN (SELECT EVENT_ID FROM EVENT WHERE NAME LIKE 'Lab Transfer Fr%')
  AND anm.ANIMAL_ID NOT IN (SELECT ANIMAL_ID FROM ALTERNATE WHERE ALTERNATE_TYPE_ID = 7)
