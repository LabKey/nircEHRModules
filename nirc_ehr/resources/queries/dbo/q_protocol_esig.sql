SELECT
    ID                      as EsigId,
    PROTOCOL_ID             as Protocol,
    ESIGNATURE_EVENT_ID     as EsigEvent,
    USER_PROFILE_ID.STAFF_ID.STAFF_FIRST_NAME
        || '|' || USER_PROFILE_ID.STAFF_ID.STAFF_LAST_NAME as UserProfile,
    ESIG_DATETIME           as EsigDateTime,
FROM PROTOCOL_ESIG