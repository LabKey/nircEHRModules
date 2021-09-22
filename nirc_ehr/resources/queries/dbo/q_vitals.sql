SELECT anmEvt.ANIMAL_EVENT_ID as objectid,
       anm.ANIMAL_ID_NUMBER AS Id,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP) AS vitalDate,
       CAST(COALESCE (adt.CHANGE_DATETIME, anmEvt.CREATED_DATETIME) AS TIMESTAMP) AS modified,
       CASE WHEN anmEvt.EVENT_ID.NAME = 'Blood Pressure-NHP' THEN anmEvt.TEXT_RESULT END AS bloodPressure,
       CASE WHEN anmEvt.EVENT_ID.NAME = 'Heart Rate-NHP' THEN anmEvt.RESULT END AS heartRate,
       CASE WHEN anmEvt.EVENT_ID.NAME = 'Respiration Rate-NHP' THEN anmEvt.RESULT END AS respRate,
       CASE WHEN anmEvt.EVENT_ID.NAME = 'Temperature-NHP' THEN anmEvt.RESULT END AS temp,
       CASE WHEN anmEvt.EVENT_ID.NAME = 'Pulse Rate-NHP' THEN anmEvt.RESULT END AS pulseRate,
       anmEvt.EVENT_ID.NAME AS type
FROM ANIMAL_EVENT anmEvt
         LEFT JOIN ANIMAL anm ON anmEvt.ANIMAL_ID = anm.ANIMAL_ID
         LEFT JOIN ANIMAL_EVENT_COMMENT anmCmt ON anmEvt.ANIMAL_EVENT_ID = anmCmt.ANIMAL_EVENT_ID
         LEFT JOIN EVENT_EVENT_GROUP evtEvtGrp ON evtEvtGrp.EVENT_ID = anmEvt.EVENT_ID
         LEFT JOIN AUDIT_TRAIL adt ON anmEvt.ANIMAL_EVENT_ID = substring(adt.PRIMARY_KEY_VALUES, length('ANIMAL_EVENT_ID = '))
    AND adt.PRIMARY_KEY_VALUES LIKE '%ANIMAL_EVENT_ID%'
WHERE evtEvtGrp.EVENT_GROUP_ID = 57 -- Vital signs
    OR-- vitals from physical exam event group
   (evtEvtGrp.EVENT_GROUP_ID = 60 AND -- Physical Exam
      evtEvtGrp.EVENT_ID IN (55, 56, 1643, 1647, 1652, 1653))
         -- 55 Blood Pressure
         -- 1652 Heart Rate
         -- 56, 1647 Respiration Rate
         -- 1643 Temperature
         -- 1653 Pulse
  AND anmEvt.CREATED_DATETIME < now() -- there are rows in ANIMAL_EVENT table with future dates
-- GROUP BY anmEvt.ANIMAL_EVENT_ID,
--          anm.ANIMAL_ID_NUMBER,
--          anmEvt.EVENT_DATETIME,
--          anmEvt.EVENT_ID.NAME,
--          anmEvt.RESULT,
--          anmEvt.TEXT_RESULT
ORDER BY adt.AUDIT_ID ASC