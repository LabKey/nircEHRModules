SELECT core.executeJavaUpgradeCode('reloadStudy');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/locations');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/housing;truncate');