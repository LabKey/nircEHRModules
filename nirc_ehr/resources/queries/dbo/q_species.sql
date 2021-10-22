SELECT
    SPEC_NAME AS common,
    SPEC_DESC AS scientific_name,
    CASE WHEN SPEC_NAME LIKE '%Capuchin%' OR SPEC_NAME LIKE '%Marmoset%' OR SPEC_NAME LIKE '%Tamarin%' THEN 3
         WHEN SPECIES_GROUP_ID = 3 OR SPECIES_GROUP_ID = 2 THEN 10  -- monkey group
         ELSE 0
    END AS blood_per_kg,
    CASE WHEN SPEC_NAME LIKE '%Capuchin%' OR SPEC_NAME LIKE '%Marmoset%' OR SPEC_NAME LIKE '%Tamarin%'
        THEN TO_CHAR(0.1, 'FM9990.00')  -- Insane Oracle formatting for decimal
         WHEN SPECIES_GROUP_ID = 3 OR SPECIES_GROUP_ID = 2 THEN TO_CHAR(1, 'FM9990.00') -- monkey group
         ELSE TO_CHAR(0, 'FM9990.00')
        END AS max_draw_pct,
    CASE WHEN SPEC_NAME LIKE '%Capuchin%' OR SPEC_NAME LIKE '%Marmoset%' OR SPEC_NAME LIKE '%Tamarin%' THEN 14
         WHEN SPECIES_GROUP_ID = 3 OR SPECIES_GROUP_ID = 2 THEN 28 -- monkey group
         ELSE 0
        END AS blood_draw_interval
FROM SPECIES
