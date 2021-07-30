SELECT core.executeJavaUpgradeCode('reloadStudy');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/locations;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/housing');