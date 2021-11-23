
        SELECT 'ALT' || alt.ALTERNATE_ID   AS objectid,
               anm.ANIMAL_ID_NUMBER AS Id,
               'unknown'      		AS performedby,
               COALESCE(MAX(CAST(adt.CHANGE_DATETIME AS TIMESTAMP)), to_date('01/01/1970' ,'MM/DD/YYYY')) AS modified,
               anm.BIRTH_DATE       AS birth,
               anm.GENDER_ID        AS gender,
               anm.SSB_ID.SPECIES_ID           AS species,
               NULL                 AS eventDate,
               alt.NAME             AS damSire,
               alt.DESCRIPTION      AS acqDateText,
               'Alternate'          AS source
        FROM  ALTERNATE alt
        LEFT JOIN ANIMAL anm ON alt.ANIMAL_ID = anm.ANIMAL_ID
        LEFT JOIN AUDIT_TRAIL adt ON alt.ALTERNATE_ID = CAST(substring(PRIMARY_KEY_VALUES, length('Alternate_ID = ')) AS INTEGER)
            AND adt.TABLE_NAME = 'ALTERNATE'
        WHERE alt.ALTERNATE_TYPE_ID = 7 -- 'Dam/Sire/Acq'
        AND anm.ANIMAL_ID_NUMBER NOT LIKE 'A%' -- Animal born in centers are pre-appended with A's (in animal Id)
        AND alt.DESCRIPTION IS NOT NULL
GROUP BY alt.ALTERNATE_ID,
         anm.ANIMAL_ID_NUMBER,
         anm.BIRTH_DATE,
         anm.GENDER_ID,
         anm.SSB_ID.SPECIES_ID,
         alt.NAME,
         alt.DESCRIPTION
--     )
-- GROUP BY objectid, Id, performedby, birth, gender, species, eventDate, remark, description, source

UNION

SELECT '' || anmEvt.ANIMAL_EVENT_ID   AS objectid,
       anm.ANIMAL_ID_NUMBER  AS Id,
       (CASE
            WHEN anmEvt.STAFF_ID.EMAIL_ADDRESS IS NULL THEN 'unknown'
            ELSE substring(anmEvt.STAFF_ID.EMAIL_ADDRESS, 1,
                        locate('@', anmEvt.STAFF_ID.EMAIL_ADDRESS) - 1) END)      AS performedby,
       CAST(COALESCE(adt.modified, anmEvt.CREATED_DATETIME) AS TIMESTAMP) AS modified,
       anm.BIRTH_DATE        AS birth,
       anm.GENDER_ID         AS gender,
       anm.SSB_ID.SPECIES_ID            AS species,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP) AS eventDate,
       NULL                  AS damSire,
       NULL                  AS acqDateText,
       'Animal Event'        AS source
FROM ANIMAL_EVENT anmEvt
         LEFT JOIN ANIMAL anm ON anmEvt.ANIMAL_ID = anm.ANIMAL_ID
         LEFT JOIN q_modified_event adt ON anmEvt.ANIMAL_EVENT_ID = adt.event_id
WHERE anmEvt.EVENT_ID IN (SELECT EVENT_ID FROM EVENT WHERE NAME LIKE 'Lab Transfer Fr%')
  AND anm.ANIMAL_ID NOT IN (SELECT ANIMAL_ID FROM ALTERNATE WHERE ALTERNATE_TYPE_ID = 7)