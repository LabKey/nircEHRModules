SELECT core.executeJavaUpgradeCode('reloadFolder');

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;lookup_sets;/data/lookup_sets.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;app_score;/data/app_score.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;bcs_score;/data/bcs_score.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;blood_draw_reason;/data/blood_draw_reason.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;blood_draw_tube_type;/data/blood_draw_tube_type.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;blood_sample_type;/data/blood_sample_type.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;blood_tube_volumes;/data/blood_tube_volumes.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;hyd_score;/data/hyd_score.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;observation_types;/data/observation_types.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;observation_areas;/data/observation_areas.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;stool_score;/data/stool_score.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;editable_lookups;/data/editable_lookups.tsv');