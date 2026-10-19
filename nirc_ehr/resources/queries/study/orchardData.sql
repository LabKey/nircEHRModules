/*
 * Copyright (c) 2024-2026 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
SELECT DISTINCT
    d.Id,
    d.gender,
    d.birth,
    d.species.scientific_name as species,
    dlp.protocol.title as protocol,
	InitCap(dlp.protocol.InvestigatorId.LastName) || ', ' || InitCap(dlp.protocol.InvestigatorId.FirstName) as PI,
	InitCap(dlp.protocol.AuthorId.LastName) || ', ' || InitCap(dlp.protocol.AuthorId.FirstName) as Vet,
	d.Id.lastHousing.cage.cage as cage,
    d.calculated_status as alive
FROM demographics d
LEFT JOIN demographicsLastProtocol dlp ON d.Id = dlp.Id