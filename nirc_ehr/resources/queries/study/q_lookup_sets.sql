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