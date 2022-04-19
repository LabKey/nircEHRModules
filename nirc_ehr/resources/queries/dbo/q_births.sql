SELECT anm.ANIMAL_ID_NUMBER AS Id,
       anm.BIRTH_DATE       AS birthDate,
       anm.GENDER_ID        AS gender,
       anm.SSB_ID.SPECIES_ID           AS species,
       COALESCE(MAX(CAST(adt.CHANGE_DATETIME AS TIMESTAMP)), to_date('01/01/1970' ,'MM/DD/YYYY')) AS modified
FROM Animal anm
LEFT JOIN ALTERNATE alt ON alt.ANIMAL_ID = anm.ANIMAL_ID
LEFT JOIN AUDIT_TRAIL adt ON anm.ANIMAL_ID = substring(PRIMARY_KEY_VALUES, length('ANIMAL_ID = '))
    AND adt.TABLE_NAME = 'ANIMAL'
WHERE alt.ALTERNATE_TYPE_ID = 7 -- 'Dam/Sire/Acq'
AND anm.ANIMAL_ID_NUMBER LIKE 'A%' -- Animal born in centers are pre-appended with A's (in animal Id)
AND alt.DESCRIPTION IS NOT NULL
GROUP BY anm.ANIMAL_ID_NUMBER,
         anm.BIRTH_DATE,
         anm.GENDER_ID,
         anm.SSB_ID.SPECIES_ID