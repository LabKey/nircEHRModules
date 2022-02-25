SELECT
    ANIMAL_ID.ANIMAL_ID_NUMBER           AS Id,
    ALTERNATE_TYPE_ID                    AS Type,
    NAME
FROM ALTERNATE
WHERE ALTERNATE_TYPE_ID IN (1, 2, 4, 5)
GROUP BY ANIMAL_ID.ANIMAL_ID_NUMBER, NAME, ALTERNATE_TYPE_ID