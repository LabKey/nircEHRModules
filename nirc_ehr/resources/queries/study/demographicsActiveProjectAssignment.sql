SELECT
  d.id,
  a.project.name as project
FROM study.demographics d
LEFT JOIN study.assignment a ON (a.id = d.id AND a.enddate IS NULL)