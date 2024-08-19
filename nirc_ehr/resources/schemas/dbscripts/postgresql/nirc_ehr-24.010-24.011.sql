SELECT core.executeJavaUpgradeCode('reloadFolder');

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;lookup_sets;/data/lookup_sets.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;editable_lookups;/data/editable_lookups.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;procedure_category;/data/procedure_category.tsv');