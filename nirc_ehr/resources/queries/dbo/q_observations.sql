
SELECT anmEvt.ANIMAL_EVENT_ID as objectid,
       anm.ANIMAL_ID_NUMBER AS Id,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP) AS obsDate,
       CAST(COALESCE (adt.modified, anmEvt.CREATED_DATETIME) AS TIMESTAMP) AS modified,
       anmCmt.TEXT AS remark,
       anmEvt.EVENT_ID.NAME AS category
FROM ANIMAL_EVENT anmEvt
LEFT JOIN ANIMAL anm ON anmEvt.ANIMAL_ID = anm.ANIMAL_ID
LEFT JOIN ANIMAL_EVENT_COMMENT anmCmt ON anmEvt.ANIMAL_EVENT_ID = anmCmt.ANIMAL_EVENT_ID
LEFT JOIN EVENT_EVENT_GROUP evtEvtGrp ON evtEvtGrp.EVENT_ID = anmEvt.EVENT_ID
LEFT JOIN q_modified_event adt ON anmEvt.ANIMAL_EVENT_ID = adt.event_id
WHERE evtEvtGrp.EVENT_GROUP_ID = 30 -- Observations
AND anmEvt.CREATED_DATETIME < now() -- there are rows in ANIMAL_EVENT table with future dates