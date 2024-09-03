SELECT core.executeJavaUpgradeCode('reloadFolder');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/clinical_observations;truncate');

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;lookup_sets;/data/lookup_sets.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;editable_lookups;/data/editable_lookups.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;activity;/data/activity.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;app_score;/data/app_score.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;hyd_score;/data/hyd_score.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;observation_types;/data/observation_types.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;stool_types;/data/stool_types.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;yesno;/data/yesno.tsv');