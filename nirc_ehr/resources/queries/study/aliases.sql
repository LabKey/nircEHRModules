-- This query is used to match upper and lowercase names in animal history

SELECT Id,
       Id as alias
FROM study.Animal where Dataset.Demographics.calculated_status != 'Alive - In Progress'
UNION
SELECT Id,
    Name as alias
FROM nirc_ehr.IdHistory
UNION
SELECT Id,
    Alias as alias
FROM study.alias where Id.demographics.calculated_status != 'Alive - In Progress'
'