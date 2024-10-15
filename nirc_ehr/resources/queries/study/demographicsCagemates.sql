SELECT
  d.id,
--   t.room,
  t.cage,
  t.total,
  cast(t.animals as varchar(4000)) as animals

FROM study.demographics d
LEFT JOIN (
SELECT
  h.id,
--   h.room,
  h.cage,
  count(distinct h2.id) as total,
  group_concat(distinct h2.id, ', ') as animals

FROM study.housing h

JOIN study.housing h2
ON (h2.Id.demographics.calculated_status = 'Alive'
--         AND h.room = h2.room
        AND (h.cage = h2.cage OR (h.cage IS NULL and h2.cage IS NULL)))

WHERE h.enddateTimeCoalesced >= now()
GROUP BY h.id, h.room, h.cage

) t ON (t.id = d.id)

WHERE d.calculated_status = 'Alive'