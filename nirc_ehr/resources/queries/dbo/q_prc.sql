SELECT anmEvt.ANIMAL_EVENT_ID as objectid,
       anm.ANIMAL_ID_NUMBER AS Id,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP) AS prcDate,
       CAST(COALESCE (adt.CHANGE_DATETIME, anmEvt.CREATED_DATETIME) AS TIMESTAMP) AS modified,
       anmCmt.TEXT AS remark,
       anmEvt.EVENT_ID.NAME AS type
       ,MAX(adt.AUDIT_ID) AS AUDIT_ID
FROM ANIMAL_EVENT anmEvt
         LEFT JOIN ANIMAL anm ON anmEvt.ANIMAL_ID = anm.ANIMAL_ID
         LEFT JOIN ANIMAL_EVENT_COMMENT anmCmt ON anmEvt.ANIMAL_EVENT_ID = anmCmt.ANIMAL_EVENT_ID
         LEFT JOIN EVENT_EVENT_GROUP evtEvtGrp ON evtEvtGrp.EVENT_ID = anmEvt.EVENT_ID
         LEFT JOIN AUDIT_TRAIL adt ON anmEvt.ANIMAL_EVENT_ID = substring(adt.PRIMARY_KEY_VALUES, length('ANIMAL_EVENT_ID = '))
    AND adt.PRIMARY_KEY_VALUES LIKE '%ANIMAL_EVENT_ID%'
WHERE evtEvtGrp.EVENT_GROUP_ID IN (6,10,21,27,28,29,35,50,52)
   -- 6 Diagnostic Imaging
   -- 10 Wound Care
   -- 21 Diagnostic Procedures
   -- 27 Dentistry Procedures
   -- 28 Surgical Procedures Major
   -- 29 Surgical Procedures Minor
   -- 35 Diagnostic Tissue Collection
   -- 50 Medical Treatment
   -- 52  Protocol Associated Events

OR -- Procedures from Diagnostic sample collection event group
(evtEvtGrp.EVENT_GROUP_ID = 36 AND evtEvtGrp.EVENT_ID != 1581)

OR -- Procedures from Timed sample collection event group
    (evtEvtGrp.EVENT_GROUP_ID = 32 AND evtEvtGrp.EVENT_ID != 1581)

OR -- Procedures from Quarantine Period
    (evtEvtGrp.EVENT_GROUP_ID = 26 AND evtEvtGrp.EVENT_ID = 1806)

AND anmEvt.CREATED_DATETIME < now() -- there are rows in ANIMAL_EVENT table with future dates
GROUP BY anmEvt.ANIMAL_EVENT_ID, anm.ANIMAL_ID_NUMBER, anmEvt.EVENT_DATETIME, adt.CHANGE_DATETIME, anmEvt.CREATED_DATETIME,
         anmCmt.TEXT, anmEvt.EVENT_ID.NAME