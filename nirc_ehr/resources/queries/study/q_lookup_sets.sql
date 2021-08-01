SELECT 'disposition_codes' AS setname,
       'Disposition Codes' AS label,
       'value' AS keyField,
       'title'AS titleColumn,
       CAST('2021-07-09' AS TIMESTAMP) AS updated_at

UNION

SELECT 'termination_reason_codes' AS setname,
       'Termination Reason Codes' AS label,
       'value' AS keyField,
       'title'AS titleColumn,
       CAST('2021-07-09' AS TIMESTAMP) AS updated_at

UNION

SELECT 'department' AS setname,
       'Department' AS label,
       'value' AS keyField,
       'title'AS titleColumn,
       CAST('2021-07-30' AS TIMESTAMP) AS updated_at

UNION

SELECT 'keyword' AS setname,
       'Keywords' AS label,
       'value' AS keyField,
       'title'AS titleColumn,
       CAST('2021-07-30' AS TIMESTAMP) AS updated_at

UNION

SELECT 'protocol_type' AS setname,
       'Protocol Type' AS label,
       'value' AS keyField,
       'title'AS titleColumn,
       CAST('2021-07-30' AS TIMESTAMP) AS updated_at

UNION

SELECT 'protocol_category' AS setname,
       'Protocol Category' AS label,
       'value' AS keyField,
       'title'AS titleColumn,
       CAST('2021-07-30' AS TIMESTAMP) AS updated_at