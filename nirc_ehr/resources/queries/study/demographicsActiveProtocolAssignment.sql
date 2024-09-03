SELECT
  d.id,
  a.protocol.displayName as protocolDisplayName,
  initcap(a.protocol.investigatorId.firstName) ||' '|| initcap(a.protocol.investigatorId.lastName) as investigator,
  a.protocol.title as protocolTitle,
  a.lsid,
  cast(CASE
           WHEN a.protocol.investigatorId.lastName IS NOT NULL THEN (a.protocol.investigatorId.lastName || ' - ' || a.protocol.displayName)
           ELSE a.protocol.displayName
      END as varchar(500)) as protocolString

FROM study.demographics d
LEFT JOIN study.protocolAssignment a ON (a.id = d.id AND a.enddate IS NULL)