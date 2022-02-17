ALTER TABLE nirc_ehr.AnimalVendor ADD COLUMN VendorProductionLocation INTEGER;

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;lookup_sets;/data/lookup_sets.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;qualifier;/data/qualifier.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;numeric_unit;/data/numeric_unit.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;vendor_production_location;/data/vendor_production_location.tsv');

SELECT core.executeJavaUpgradeCode('reloadFolder');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/alopecia;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/blood;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/vitals;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/physicalExam;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/weight;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/animalVendor;truncate');