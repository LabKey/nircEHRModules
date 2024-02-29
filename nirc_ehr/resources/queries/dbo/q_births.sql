SELECT anm.ANIMAL_ID_NUMBER AS participantId,
       anm.BIRTH_DATE       AS birthDate,
       -- audit timestamp for modifications or animal event received for created
       COALESCE(MAX(CAST(adt.CHANGE_DATETIME AS TIMESTAMP)), ae.CREATED_DATETIME) AS modified
FROM Animal anm
LEFT JOIN ALTERNATE alt ON alt.ANIMAL_ID = anm.ANIMAL_ID
LEFT JOIN AUDIT_TRAIL adt ON anm.ANIMAL_ID = substring(PRIMARY_KEY_VALUES, length('ANIMAL_ID = '))
    AND adt.TABLE_NAME = 'ANIMAL'
LEFT JOIN ANIMAL_EVENT ae ON ae.ANIMAL_ID = anm.ANIMAL_ID AND EVENT_ID = 1  -- Received
WHERE alt.ALTERNATE_TYPE_ID = 7 -- 'Dam/Sire/Acq'
AND anm.ANIMAL_ID_NUMBER LIKE 'A%' -- Animal born in centers are pre-appended with A's (in animal Id)
AND alt.DESCRIPTION IS NOT NULL
GROUP BY anm.ANIMAL_ID_NUMBER,
         anm.BIRTH_DATE,
         ae.CREATED_DATETIME