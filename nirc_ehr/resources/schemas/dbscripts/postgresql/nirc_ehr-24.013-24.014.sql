SELECT core.executeJavaUpgradeCode('reloadFolder');

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;lookup_sets;/data/lookup_sets.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;editable_lookups;/data/editable_lookups.tsv');

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;nhp_training_reason;/data/nhp_training_reason.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;nhp_training_result;/data/nhp_training_result.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;nhp_training_type;/data/nhp_training_type.tsv');