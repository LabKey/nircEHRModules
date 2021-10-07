SELECT ae.objectid,
       ae.modified,
       ae.REFERENCE,
       drugEvents.event_id,
       ae.event_name
FROM auditEvent ae

LEFT JOIN -- on drug events

    (SELECT e.name, e.event_id, eeg.event_group_id
      FROM event_event_group eeg LEFT JOIN event e ON eeg.event_id = e.event_id
      WHERE (eeg.EVENT_GROUP_ID = 31 AND eeg.EVENT_ID = 2261) -- Sedation from Timed Procedures
            OR eeg.EVENT_GROUP_ID IN (3, 25, 34, 39, 51, 54, 65)
      GROUP BY eeg.event_group_id, e.event_id, e.name) drugEvents

ON ae.event_name = drugEvents.name WHERE drugEvents.name IS NOT NULL

GROUP BY
    ae.objectid,
    ae.modified,
    ae.REFERENCE,
    drugEvents.event_id,
    ae.event_name