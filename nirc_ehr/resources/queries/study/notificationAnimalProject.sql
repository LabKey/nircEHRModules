-- Add more here to make a single query for animal details in notifications
SELECT
    Id,
    date,
    project.name AS project,
    enddate
FROM study.assignment
WHERE enddate IS NULL