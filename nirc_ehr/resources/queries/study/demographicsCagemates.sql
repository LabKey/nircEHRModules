/*
 * Copyright (c) 2024-2026 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
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
ON (h.cage = h2.cage
        AND h2.Id.demographics.calculated_status = 'Alive'
        AND h2.enddateTimeCoalesced >= now()
        AND h2.qcstate.publicdata = true)

-- cage holds the ehr_lookups.cage location key, so a null means this row's location never resolved; such a row gets no cagemates rather than sharing one group with every other unresolved row
WHERE h.cage IS NOT NULL
AND h.enddateTimeCoalesced >= now()
AND h.qcstate.publicdata = true
GROUP BY h.id, h.room, h.cage

) t ON (t.id = d.id)

WHERE d.calculated_status = 'Alive'