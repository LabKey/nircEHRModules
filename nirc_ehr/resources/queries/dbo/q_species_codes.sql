
SELECT
    SPECIES_ID AS code,
    SPEC_NAME AS common_name,
    SPEC_DESC AS scientific_name,
    SPECIES_GROUP_ID as species,
    CASE ACTIVE_YN
        WHEN 'Y' THEN NULL
        WHEN 'N' THEN '1/1/1970'
        END AS dateDisabled
FROM SPECIES