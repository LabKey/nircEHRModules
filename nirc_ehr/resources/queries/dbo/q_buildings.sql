SELECT
    building.LOCATION_ID        AS name,
    building.LOCATION_NAME      AS description,
    area.LOCATION_ID            AS area
FROM LOCATION building
JOIN LOCATION area ON building.PARENT_LOCATION_ID = area.LOCATION_ID
WHERE building.LOCATION_TYPE_ID = 2 -- building