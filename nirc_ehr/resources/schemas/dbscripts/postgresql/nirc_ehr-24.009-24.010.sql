SELECT core.executeJavaUpgradeCode('reloadFolder');

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;death_reason;/data/death_reason.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;acquisition_type;/data/acquisition_type.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;housing_reason;/data/housing_reason.tsv');