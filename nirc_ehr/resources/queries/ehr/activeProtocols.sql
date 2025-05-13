SELECT protocol, title FROM ehr.protocol pr
WHERE pr.inactiveDate IS NULL OR pr.inactiveDate > now()