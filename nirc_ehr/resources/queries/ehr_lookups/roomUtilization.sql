
SELECT
    r.room,
    count(DISTINCT h.id) as TotalAnimals,
    r.building,
    r.area,
FROM ehr_lookups.rooms r
LEFT JOIN study.housing h
ON r.room = h.room

GROUP BY r.room, r.building, r.area