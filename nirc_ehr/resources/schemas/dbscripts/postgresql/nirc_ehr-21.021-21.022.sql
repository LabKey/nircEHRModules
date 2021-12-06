SELECT core.executeJavaUpgradeCode('importFromTsv;ehr;reports;/data/reports.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;ageclass;/data/ageclass.tsv');

SELECT core.executeJavaUpgradeCode('reloadStudy');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/ehr_lookups;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/locations;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/demographics;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/assignment;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/cases;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/arrival;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/departure;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/housing;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/birth;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/prc;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/blood;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/breeder;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/clinremarks;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/necropsy;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/drug;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/exemptions;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/physicalExam;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/vitals;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/serology;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/pairings;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/obs;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/weight;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocolAssignment;truncate');

