SELECT 'PROTOCOL-' || PROTOCOL_ID AS "objectid",
       PROTOCOL_NUMBER AS "title",
       PROTOCOL_NUMBER AS "external_id",
       PROTOCOL_ID AS "protocol",
       DEPARTMENT_ID AS "departmentId",
       PROTOCOL_ID_PREFIX AS "prefix",
       PROTOCOL_ID_SUFFIX AS "suffix",
       PROTOCOL_SHORT_TITLE AS "description",
       APPROVAL_DATE AS "approve",
       EXPIRATION_DATE AS "enddate",
       RENEWAL_DATE AS "renewalDate",
       EMERGENCY_PHONE_NUM AS "emergencyPhoneNumber",
       INACTIVE_DATE AS "inactiveDate",
       NEXT_REVIEW_DATE AS "nextReviewDate",
       LOCATION_ID AS "locationId",
       EFFECTIVE_DATE AS "effectiveDate",
       CURRENT_STATE AS "currentState",
       USER_REFERENCE_NUMBER AS "userReferenceNumber",
       UNRESTRICTED_VIEW_YN AS "isUnrestrictedView",
       AUDIT_YN AS "isAudit",
       AUDIT_YN_MODIFIED_YN AS "isAuditModified",
       PROTOCOL_TYPE_ID AS "protocolTypeId",
       PARENT_PROTOCOL_ID AS "parentProtocolId",
       QUESTIONNAIRE_ID AS "questionnaireId",
       AMENDMENT_REASON AS "amendmentReason",
       REQUESTED_EFFECTIVE_DATE AS "requestedEffectiveDate",
       PROTOCOL_CATEGORY_ID AS "protocolCategoryId",
       OBJECTIVE AS "objective",
       RATIONALE AS "rationale",
       TISSUE_HARVEST_YN AS "isTissueHarvest",
       ORDER_TRACKING_YN AS "isOrderTracking",
       LONG_TERM_YN AS "isLongTerm",
       IN_VIVO_YN AS "isInVivo",
       STOCK_YN AS "isStock",
       SUBMISSION_DATE AS "submissionDate",
       REVIEW_COMPLETION_DATE AS "reviewCompletionDate",
       HONOR_RESET_YN AS "isHonorReset",
       X.modified
FROM PROTOCOL p
LEFT JOIN (
    SELECT substring(PRIMARY_KEY_VALUES, length('PROTOCOL_ID = ')) AS protocolId,
           COALESCE(MAX(CAST(CHANGE_DATETIME AS TIMESTAMP)), to_date('01/01/1970' ,'MM/DD/YYYY')) AS modified
    FROM AUDIT_TRAIL
    WHERE TABLE_NAME = 'PROTOCOL'
    GROUP BY substring(PRIMARY_KEY_VALUES, length('PROTOCOL_ID = '))
    ) X ON X.protocolId = p.PROTOCOL_ID
WHERE PROTOCOL_NUMBER IS NOT NULL
