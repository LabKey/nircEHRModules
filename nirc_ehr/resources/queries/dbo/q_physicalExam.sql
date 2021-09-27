SELECT anmEvt.ANIMAL_EVENT_ID as objectid,
       anm.ANIMAL_ID_NUMBER AS Id,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP) AS physicalExamDate,
       CAST(COALESCE (adt.CHANGE_DATETIME, anmEvt.CREATED_DATETIME) AS TIMESTAMP) AS modified,
       anmCmt.TEXT AS remark,
       anmEvt.EVENT_ID.NAME AS exam,
       anmEvt.RESULT
FROM ANIMAL_EVENT anmEvt
         LEFT JOIN ANIMAL anm ON anmEvt.ANIMAL_ID = anm.ANIMAL_ID
         LEFT JOIN ANIMAL_EVENT_COMMENT anmCmt ON anmEvt.ANIMAL_EVENT_ID = anmCmt.ANIMAL_EVENT_ID
         LEFT JOIN EVENT_EVENT_GROUP evtEvtGrp ON evtEvtGrp.EVENT_ID = anmEvt.EVENT_ID
         LEFT JOIN AUDIT_TRAIL adt ON anmEvt.ANIMAL_EVENT_ID = substring(adt.PRIMARY_KEY_VALUES, length('ANIMAL_EVENT_ID = '))
    AND adt.PRIMARY_KEY_VALUES LIKE '%ANIMAL_EVENT_ID%'
WHERE evtEvtGrp.EVENT_GROUP_ID = 60 -- Physical Exam
  -- filter out vital signs events
  AND evtEvtGrp.EVENT_ID NOT IN (1349, 2165, 2166, 2167, 2168, 2169, 55, 56, 1643, 1647, 1652, 1653)
  -- 1349 Body Weight
  -- 2165 Blood Pressure-NHP
  -- 2166 Heart Rate-NHP
  -- 2167 Respiration Rate-NHP
  -- 2168 Temperature-NHP
  -- 2169 Pulse Rate-NHP
  -- 55 Blood Pressure
  -- 1652 Heart Rate
  -- 56, 1647 Respiration Rate
  -- 1643 Temperature
  -- 1653 Pulse
  AND anmEvt.CREATED_DATETIME < now() -- there are rows in ANIMAL_EVENT table with future dates
ORDER BY adt.AUDIT_ID ASC