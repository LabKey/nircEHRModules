SELECT pa.Id,
       pa.protocol.title AS protocolTitle,
       pa.protocol.InvestigatorId AS investigatorId,
       pa.protocol.InvestigatorId.DisplayName AS investigatorName,
       pa.protocol.InvestigatorId.LastName AS investigatorLastName,
       a.project,
       a.isActive AS isActiveAssignment,
       pa.isActive AS isActiveProtocolAssignment,
       a.qcstate.publicData AS isPublicAssignment,
       pa.qcstate.publicData AS isPublicProtocolAssignment

    FROM protocolAssignment pa
FULL OUTER JOIN assignment a ON pa.Id = a.Id
WHERE pa.isActive = true AND
      a.isActive = true AND
      a.qcstate.publicData = true AND
      pa.qcstate.publicData = true
