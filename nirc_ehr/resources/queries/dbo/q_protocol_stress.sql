SELECT
    SEGMENT_ID.PROTOCOL_ID.PROTOCOL_ID      AS Protocol,
    SEGMENT_ID.SPECIES_ID                   AS Species,
    STRESS_ID.STRESS_ID                     AS Stress,
    AUTHORIZED_AMT                          AS Allowed
FROM SEGMENT_STRESS
WHERE AUTHORIZED_AMT > 0 AND SEGMENT_ID.SPECIES_ID IS NOT NULL