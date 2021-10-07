SELECT ae.objectid,
       ae.modified,
       ae.REFERENCE,
       ct.event_id,
       ae.event_name
FROM auditEvent ae

LEFT JOIN -- on clinical treatments

(SELECT e.name, e.event_id, eeg.event_group_id
 FROM event_event_group eeg
          LEFT JOIN event e ON eeg.event_id = e.event_id
 WHERE (eeg.EVENT_GROUP_ID = 37) -- Clinical Treatments
 GROUP BY eeg.event_group_id, e.event_id, e.name) ct

ON ae.event_name = ct.name

WHERE ct.name IS NOT NULL

GROUP BY ae.objectid,
         ae.modified,
         ae.REFERENCE,
         ct.event_id,
         ae.event_name