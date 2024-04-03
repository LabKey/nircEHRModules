SELECT core.executeJavaUpgradeCode('reloadFolder');

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;alias_category;/data/alias_category.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;pregnancy_result;/data/pregnancy_result.tsv');
