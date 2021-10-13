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

UNION

SELECT 'DEPARTMENT_' || DEPARTMENT_ID AS objectid,
'department' AS set_name,
DEPARTMENT_ID AS "value",
DEPARTMENT_DESC AS description,
DEPARTMENT_NAME AS title,
DEPARTMENT_ID AS sort_order,
CASE ACTIVE_YN
WHEN 'Y' THEN NULL
WHEN 'N' THEN '1/1/1970'
END AS date_disabled,
NULL AS category
FROM DEPARTMENT

UNION

SELECT 'KEYWORD_' || KEY_WORD_ID AS objectid,
'keyword' AS set_name,
KEY_WORD_ID AS "value",
NULL AS description,
KEY_WORD_NAME AS title,
KEY_WORD_ID AS sort_order,
NULL AS date_disabled,
NULL AS category
FROM KEY_WORD

UNION

SELECT 'PROTOCOL_TYPE_' || PROTOCOL_TYPE_ID AS objectid,
'protocol_type' AS set_name,
PROTOCOL_TYPE_ID AS "value",
NAME AS description,
NAME AS title,
PROTOCOL_TYPE_ID AS sort_order,
NULL AS date_disabled,
NULL AS category
FROM PROTOCOL_TYPE

UNION

SELECT 'PROTOCOL_CATEGORY_' || ID AS objectid,
'protocol_category' AS set_name,
ID AS "value",
DESCRIPTION AS description,
NAME AS title,
ID AS sort_order,
CASE ACTIVE_YN
WHEN 'Y' THEN NULL
WHEN 'N' THEN '1/1/1970'
END AS date_disabled,
NULL AS category,
FROM PROTOCOL_CATEGORY

UNION

SELECT 'PROTOCOL_STATE_' || ID AS objectid,
'protocol_state' AS set_name,
ID AS "value",
DESCRIPTION AS description,
NAME AS title,
ID AS sort_order,
NULL AS date_disabled,
NULL AS category,
FROM PROTOCOL_CATEGORY

UNION

SELECT 'QUESTIONNAIRE' || QUESTIONNAIRE_ID AS objectid,
'questionnaire' AS set_name,
QUESTIONNAIRE_ID AS "value",
QUESTIONNAIRE_NAME AS description,
QUESTIONNAIRE_NAME AS title,
QUESTIONNAIRE_ID AS sort_order,
CASE QUESTIONNAIRE_ACTIVE_YN
WHEN 'Y' THEN NULL
WHEN 'N' THEN '1/1/1970'
END AS date_disabled,
NULL AS category,
FROM QUESTIONNAIRE

UNION

SELECT
'CLINREMARKS_CATEGORY' || EVENT_ID.EVENT_ID as objectid,
'clinremarks_category' AS set_name,
EVENT_ID.EVENT_ID AS "value",
EVENT_ID.DESCRIPTION AS description,
EVENT_ID.NAME AS title,
EVENT_ID.EVENT_ID AS sort_order,
CASE
    WHEN EVENT_ID.ACTIVE_YN = 'Y' AND EVENT_GROUP_ID.ACTIVE_YN = 'Y' THEN NULL
    ELSE '1/1/1970'
    END AS date_disabled,
NULL AS category,
FROM EVENT_EVENT_GROUP
WHERE EVENT_GROUP_ID.EVENT_GROUP_ID = 53
