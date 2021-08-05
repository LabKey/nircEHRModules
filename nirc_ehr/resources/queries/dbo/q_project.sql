
SELECT  project AS "name",
        project AS "title",
        MAX(ALTERNATE_ID) AS "objectId",
        MAX(ALTERNATE_ID) AS "project"
FROM
(
    SELECT CASE
               WHEN substring(alt.NAME, 3, 1) = '-' THEN substring(alt.NAME, 1, 11)
               WHEN substring(alt.NAME, 5, 1) != '-' THEN substring(alt.NAME, 1, 8)
               WHEN substring(alt.NAME, 5, 1) = '-' THEN substring(alt.NAME, 1, 11)
               END AS project,
           alt.ALTERNATE_ID
    FROM ALTERNATE alt
    WHERE alt.ALTERNATE_TYPE_ID = 6 -- Project Inventory Number
) x
GROUP BY project