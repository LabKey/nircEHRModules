SELECT core.executeJavaUpgradeCode('reloadStudy');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/ehr_lookups;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/blood;truncate');