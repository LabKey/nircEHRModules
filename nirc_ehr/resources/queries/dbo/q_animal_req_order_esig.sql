SELECT
    ID                      as EsigId,
    ANIMAL_REQ_ORDER_ID     as AnimalReqOrder,
    ESIGNATURE_EVENT_ID     as EsigEvent,
    USER_PROFILE_ID.STAFF_ID.STAFF_FIRST_NAME
        || '|' || USER_PROFILE_ID.STAFF_ID.STAFF_LAST_NAME as UserProfile,
    ESIG_DATETIME           as EsigDateTime
FROM ANIMAL_REQ_ORDER_ESIG