SELECT PROTOCOL_ID AS "protocolId",
       CASE WHEN PROTOCOL_NUMBER IS NULL THEN 'missing'
            ELSE PROTOCOL_NUMBER END AS "protocol",
       RENEWAL_DATE AS "amendmentDate",
       SUBMISSION_DATE AS "submitted",
       APPROVAL_DATE AS "approved",
       COALESCE(MAX(CAST(adt.CHANGE_DATETIME AS TIMESTAMP)), to_date('01/01/1970' ,'MM/DD/YYYY')) AS modified
FROM PROTOCOL p
LEFT JOIN AUDIT_TRAIL adt ON p.PROTOCOL_ID = substring(PRIMARY_KEY_VALUES, length('PROTOCOL_ID = '))
AND adt.TABLE_NAME = 'PROTOCOL'
WHERE PROTOCOL_TYPE_ID IN (2,3,4)
GROUP BY PROTOCOL_ID,
         PROTOCOL_NUMBER,
         RENEWAL_DATE,
         SUBMISSION_DATE,
         APPROVAL_DATE