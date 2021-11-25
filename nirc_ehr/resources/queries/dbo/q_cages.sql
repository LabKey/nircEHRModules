SELECT LOC1.LOCATION_ID      AS location,
       LOC1.LOCATION_ID      AS status, -- using for location as cage is not extensible
       LOC1.LOCATION_NAME    AS cage,
       LOC2.LOCATION_ID      AS room
FROM LOCATION LOC1
JOIN LOCATION LOC2 ON LOC1.PARENT_LOCATION_ID = LOC2.LOCATION_ID
WHERE LOC1.LOCATION_TYPE_ID = 5 -- Sub Room