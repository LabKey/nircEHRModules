SELECT core.executeJavaUpgradeCode('reloadFolder');

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;lookup_sets;/data/lookup_sets.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;editable_lookups;/data/editable_lookups.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;death_reason;/data/death_reason.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;acquisition_type;/data/acquisition_type.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;housing_reason;/data/housing_reason.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;arrival_type;/data/arrival_type.tsv');