/*
 * Copyright (c) 2025-2026 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */

SELECT
    d2.id,
    d2.project,
    d2.enddate

FROM study.assignment d2
WHERE d2.qcstate.publicdata = true
-- Don't self-join on max(date): Postgres underestimates that join by ~1000x and picks an unindexed nested loop.
AND NOT EXISTS (SELECT 1 FROM study.assignment h WHERE h.id = d2.id AND h.date > d2.date AND h.qcstate.publicdata = true)