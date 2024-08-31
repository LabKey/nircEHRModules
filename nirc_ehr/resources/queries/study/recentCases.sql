
PARAMETERS(SubjectId VARCHAR, CaseCategory VARCHAR)

SELECT * FROM study.cases
WHERE Id = SubjectId AND category = CaseCategory
ORDER BY date DESC LIMIT 20