SELECT substring(adt.PRIMARY_KEY_VALUES, length('ANIMAL_EVENT_ID = ')) AS objectid,
       CAST(adt.CHANGE_DATETIME AS TIMESTAMP)                          AS modified,
       adt.REFERENCE
FROM AUDIT_TRAIL adt
WHERE adt.PRIMARY_KEY_VALUES LIKE '%ANIMAL_EVENT_ID%'
  AND (adt.REFERENCE LIKE '%Vaccination%' OR
       adt.REFERENCE LIKE '%Dose Administration%' OR
       adt.REFERENCE LIKE '%Medication Administration%' OR
       adt.REFERENCE LIKE '%Behavioral Treatment%' OR
       adt.REFERENCE LIKE '%Oral Dose Administration - Alert%' OR
       adt.REFERENCE LIKE '%Fluid Administration%' OR
       adt.REFERENCE LIKE '%Sedation%')
  AND adt.COLUMN_NAME = 'DELETE'