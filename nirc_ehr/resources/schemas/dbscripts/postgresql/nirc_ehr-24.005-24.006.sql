SELECT core.executeJavaUpgradeCode('reloadFolder');

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;lookup_sets;/data/lookup_sets.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;amount_units;/data/amount_units.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;data_category;/data/data_category.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;routes;/data/routes.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;snomed;/data/snomed.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;treatment_frequency;/data/treatment_frequency.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;volume_units;/data/volume_units.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;conc_units;/data/conc_units.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;dosage_units;/data/dosage_units.tsv');

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;alopecia_type;/data/alopecia_type.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;alopecia_score;/data/alopecia_score.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;alopecia_regrowth;/data/alopecia_regrowth.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;att_score;/data/att_score.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;fecal_smear_score;/data/fecal_smear_score.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;incision_score;/data/incision_score.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;mens_score;/data/mens_score.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;sib_score;/data/sib_score.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;stool_score;/data/stool_score.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;turgor_score;/data/turgor_score.tsv');

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;editable_lookups;/data/editable_lookups.tsv');