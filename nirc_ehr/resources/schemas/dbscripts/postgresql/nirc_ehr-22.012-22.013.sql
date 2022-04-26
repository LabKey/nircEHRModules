SELECT core.executeJavaUpgradeCode('reloadFolder');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/necropsy;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/biopsy;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/breeder;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/drug;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/cases;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/pregnancy;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/notes;truncate');