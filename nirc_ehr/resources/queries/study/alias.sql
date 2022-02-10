-- This query is used to match upper and lowercase names in animal history

SELECT Id,
       lower(Id) as alias
FROM study.Animal
UNION
SELECT Id,
       Id as alias
FROM study.Animal
UNION
SELECT Id,
    Name as alias
FROM nirc_ehr.IdHistory