SELECT core.executeJavaUpgradeCode('importFromTsv;ehr;reports;/data/reports.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;ageclass;/data/ageclass.tsv');

SELECT core.executeJavaUpgradeCode('reloadStudy');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/ehr_lookups;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/locations;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/demographics;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/arrival;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/departure;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/housing;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/birth;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/prc;truncate');