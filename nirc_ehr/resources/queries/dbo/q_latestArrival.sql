
-- Used in finalDeparture
SELECT Id,
MAX(eventDate) as latestArr,
FROM q_arrival
GROUP BY Id