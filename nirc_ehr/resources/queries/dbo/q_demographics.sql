
SELECT anm.ANIMAL_ID_NUMBER AS Id,
       anm.BIRTH_DATE,
       anm.GENDER_ID AS gender,
       anm.SSB_ID.SPECIES_ID AS species,
       anm.BIRTH_DATE AS birth,
       anm.DATE_WEANED AS weanedDate,
       anm.DEATH_DATE AS death,
       anm.ANIMAL_DISPOSITION_ID AS disposition,
       anm.DESCRIPTION AS description,
       anm.TERMINATION_REASON_ID AS termination,
       anm.IMPLANT_NUMBER AS implantNumber,
       anm.VENDOR_ANIMAL_NUMBER AS vendorNumber,
       anm.ACTIVE_YN AS activeStatus,
       anm.LOT_NUMBER_ID AS lotId,
       st.status AS calculated_status,
       alt.NAME AS damSire,
       COALESCE(MAX(CAST(adt.CHANGE_DATETIME AS TIMESTAMP)), to_date('01/01/1970' ,'MM/DD/YYYY')) AS modified
FROM Animal anm
LEFT JOIN AUDIT_TRAIL adt ON anm.ANIMAL_ID_NUMBER = substring(PRIMARY_KEY_VALUES, length('ANIMAL_ID = '))
AND adt.TABLE_NAME = 'ANIMAL'
LEFT JOIN q_status st ON st.Id = anm.ANIMAL_ID_NUMBER
LEFT JOIN ALTERNATE alt ON alt.ANIMAL_ID = anm.ANIMAL_ID AND alt.ALTERNATE_TYPE_ID = 7
GROUP BY anm.ANIMAL_ID_NUMBER,
    anm.BIRTH_DATE,
    anm.GENDER_ID,
    anm.SSB_ID.SPECIES_ID,
    anm.BIRTH_DATE,
    anm.DATE_WEANED,
    anm.DEATH_DATE,
    anm.ANIMAL_DISPOSITION_ID,
    anm.DESCRIPTION,
    anm.TERMINATION_REASON_ID,
    anm.IMPLANT_NUMBER,
    anm.VENDOR_ANIMAL_NUMBER,
    anm.ACTIVE_YN,
    anm.LOT_NUMBER_ID,
    st.status,
    alt.NAME
