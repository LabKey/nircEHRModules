SELECT
    Id.activeProjectAssignments.project,
    COUNT(*) as Total
FROM housing
GROUP BY Id.activeProjectAssignments.project