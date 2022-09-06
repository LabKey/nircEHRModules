
-- Determines final departure dates including for animals that leave and return
SELECT dep.Id,
       arr.latestArr,
       dep.latestDep,
       -- Animal can leave center and come back multiple times
       CASE WHEN arr.latestArr IS NOT NULL AND dep.latestDep IS NOT NULL AND arr.latestArr > dep.latestDep THEN NULL
       ELSE dep.latestDep END AS eventDate
       FROM q_latestDeparture dep
LEFT JOIN q_latestArrival arr ON arr.Id = dep.Id