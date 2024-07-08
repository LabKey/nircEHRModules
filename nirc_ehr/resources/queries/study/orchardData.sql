SELECT
    d.Id,
    d.date,
    d.birth,
    d.Id.activeAssignments.protocols,
    d.Id.curLocation.cage,
    d.Id.curLocation.room,
    d.Id.curLocation.date as housingDate,
FROM demographics d