SELECT anm.ANIMAL_ID_NUMBER AS Id,
       anm.BIRTH_DATE       AS birthDate,
       anm.GENDER_ID        AS gender,
       anm.SSB_ID.SPECIES_ID           AS species,
       alt.NAME AS damSire
FROM Animal anm
LEFT JOIN ALTERNATE alt ON alt.ANIMAL_ID = anm.ANIMAL_ID
WHERE alt.ALTERNATE_TYPE_ID = 7 -- 'Dam/Sire/Acq'
AND anm.ANIMAL_ID_NUMBER LIKE 'A%' -- Animal born in centers are pre-appended with A's (in animal Id)
AND alt.DESCRIPTION IS NOT NULL