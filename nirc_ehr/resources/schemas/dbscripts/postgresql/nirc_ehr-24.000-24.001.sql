SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/arrival;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/births;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/cageCard;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/housing;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/necropsy;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/ProtocolAndAssignment;truncate');

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;death_reason;/data/death_reason.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;housing_reason;/data/housing_reason.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;necropsy_disposition_codes;/data/necropsy_disposition_codes.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;necropsy_exam_reason;/data/necropsy_exam_reason.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;necropsy_organ_appearance;/data/necropsy_organ_appearance.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;necropsy_organ_systems;/data/necropsy_organ_systems.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;necropsy_physical_condition;/data/necropsy_physical_condition.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;necropsy_specimen_condition;/data/necropsy_specimen_condition.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;necropsy_tissue;/data/necropsy_tissue.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;weight_ranges;/data/weight_ranges.tsv');
