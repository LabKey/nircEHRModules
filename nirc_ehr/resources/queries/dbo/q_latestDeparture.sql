
-- Used in finalDeparture
SELECT Id,
MAX(eventDate) as latestDep,
FROM q_departure
GROUP BY Id