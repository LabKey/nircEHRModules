SELECT core.executeJavaUpgradeCode('reloadFolder');

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;lookup_sets;/data/lookup_sets.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;amount_units;/data/amount_units.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;data_category;/data/data_category.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;routes;/data/routes.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;snomed;/data/snomed.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;treatment_frequency;/data/treatment_frequency.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;volume_units;/data/volume_units.tsv');