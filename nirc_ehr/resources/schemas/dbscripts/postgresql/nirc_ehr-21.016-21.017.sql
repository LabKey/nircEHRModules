SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/breeder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/ehr_lookups;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/clinremarks');