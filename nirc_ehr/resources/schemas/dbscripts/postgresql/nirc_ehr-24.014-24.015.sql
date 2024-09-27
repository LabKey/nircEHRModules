SELECT core.executeJavaUpgradeCode('reloadFolder');

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;lookup_sets;/data/lookup_sets.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;editable_lookups;/data/editable_lookups.tsv');

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;pairing_goal;/data/pairing_goal.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;pairing_reason;/data/pairing_reason.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;pairing_observation;/data/pairing_observation.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;pairing_separation_reason;/data/pairing_separation_reason.tsv');