
-- Used in assignment queries. Handles duplicates and multiple departures.
SELECT d.* FROM
q_departure d
INNER JOIN
(
    SELECT
    q_departure.Id,
    MAX(q_departure.eventDate) as latest,
    FROM q_departure
    GROUP BY Id
) l ON d.Id = l.Id AND d.eventDate = l.latest