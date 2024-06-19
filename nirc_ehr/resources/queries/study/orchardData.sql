SELECT
    d.Id,
    d.date,
    d.birth,
    d.Id.activeAssignments.protocols,
    h.cage,
    h.room
FROM demographics d
LEFT JOIN housing h ON d.Id = h.Id