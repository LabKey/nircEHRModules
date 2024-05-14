SELECT
    NAME AS category,
    'FALSE' AS enforceUnique,
    'FALSE' AS omitFromOverview,
    'TRUE' AS doHighlight
FROM EVENT_GROUP
WHERE EVENT_GROUP_ID IN (26, 28, 67)
-- 26 Quarantine
-- 28 Surgical Procedures - Major
-- 67 Adverse Reaction to Medication
