
SELECT DISTINCT Id, protocol.title as protocol, project FROM
    (
        SELECT DISTINCT d.Id,
                        a.project.name as project, a.enddate AS projectenddate,
                        pa.protocol, pa.enddate AS protocolenddate
        FROM study.demographics d
                 JOIN study.assignment a ON a.Id = d.Id
                 JOIN study.protocolAssignment pa ON pa.Id = d.Id
        WHERE a.enddate IS NULL
    ) sub
WHERE sub.protocolenddate IS NULL