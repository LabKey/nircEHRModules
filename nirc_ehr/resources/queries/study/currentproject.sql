-- This query is used to find the current project

SELECT DISTINCT assignment.Id,
assignment.project
FROM assignment
WHERE assignment.enddate is null