SELECT 'DISP_CD_' || ID AS objectid,
'disposition_codes' AS set_name,
ID AS "value",
DESCRIPTION AS description,
NAME AS title,
ID AS sort_order,
CASE ACTIVE_YN
WHEN 'Y' THEN NULL
WHEN 'N' THEN '1/1/1970'
END AS date_disabled,
NULL AS category
FROM ANIMAL_DISPOSITION

UNION

SELECT 'TERMINATION_RSN_CD_' || ID AS objectid,
'termination_reason_codes' AS set_name,
ID AS "value",
DESCRIPTION AS description,
NAME AS title,
ID AS sort_order,
CASE ACTIVE_YN
WHEN 'Y' THEN NULL
WHEN 'N' THEN '1/1/1970'
END AS date_disabled,
NULL AS category
FROM TERMINATION_REASON