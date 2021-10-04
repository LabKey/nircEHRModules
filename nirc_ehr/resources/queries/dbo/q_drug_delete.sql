SELECT auditSubQueryOuter.objectid,
       auditSubQueryOuter.modified,
       auditSubQueryOuter.REFERENCE,
       drugEvents.event_id,
       drugEvents.event_group_id,
       auditSubQueryOuter.event_name
FROM
    (SELECT
         auditSubQueryInner.objectid,
         auditSubQueryInner.modified,
         auditSubQueryInner.REFERENCE,
         trim(substr(auditSubQueryInner.event_partial, 1, (length(auditSubQueryInner.event_partial) -
                                                    (length('Event Datetime:') + length(event_datetime))))) as event_name --extract event name from REFERENCE
FROM (
         SELECT substring(adt.PRIMARY_KEY_VALUES, length('ANIMAL_EVENT_ID = '))             AS objectid,
                CAST(adt.CHANGE_DATETIME AS TIMESTAMP)                                      AS modified,
                adt.REFERENCE,
                SUBSTR(adt.reference,
                       instr(adt.reference, 'Event:') + length('Event:'))                   AS event_partial,
                SUBSTR(adt.reference,
                       instr(adt.reference, 'Event Datetime:') + length('Event Datetime:')) AS event_datetime
         FROM AUDIT_TRAIL adt
         WHERE adt.PRIMARY_KEY_VALUES LIKE '%ANIMAL_EVENT_ID%'
           AND adt.COLUMN_NAME = 'DELETE') auditSubQueryInner) auditSubQueryOuter

LEFT JOIN -- on drug events

    (SELECT e.name, e.event_id, eeg.event_group_id
      FROM event_event_group eeg LEFT JOIN event e ON eeg.event_id = e.event_id
      WHERE (eeg.EVENT_GROUP_ID = 31 AND eeg.EVENT_ID = 2261) -- Sedation from Timed Procedures
            OR eeg.EVENT_GROUP_ID IN (3, 25, 34, 39, 51, 54, 65)
      GROUP BY eeg.event_group_id, e.event_id, e.name) drugEvents

ON auditSubQueryOuter.event_name = drugEvents.name WHERE drugEvents.name IS NOT NULL