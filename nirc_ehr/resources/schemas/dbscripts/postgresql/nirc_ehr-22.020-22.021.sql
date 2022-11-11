CREATE TABLE nirc_ehr.CasesTemp
(
    Id                  TEXT,
    date                TIMESTAMP,
    enddate             TIMESTAMP,
    objectid            TEXT,
    category            TEXT,
    openDiagnosis       TEXT,
    closeDiagnosis      TEXT,
    openRemark          TEXT,
    closeRemark         TEXT,
    attachmentFile      TEXT,
    performedby         USERID
);

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/cases;truncate');