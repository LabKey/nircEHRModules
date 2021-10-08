SELECT anmEvt.ANIMAL_EVENT_ID as objectid,
       anm.ANIMAL_ID_NUMBER AS Id,
       CAST(anmEvt.EVENT_DATETIME AS TIMESTAMP) AS vitalDate,
       CAST(COALESCE (adt.CHANGE_DATETIME, anmEvt.CREATED_DATETIME) AS TIMESTAMP) AS modified,
       CASE WHEN anmEvt.EVENT_ID.NAME LIKE '%Blood Pressure%' THEN anmEvt.TEXT_RESULT END AS bloodPressure,
       CASE WHEN anmEvt.EVENT_ID.NAME LIKE '%Heart Rate%' THEN anmEvt.RESULT END AS heartRate,
       CASE WHEN anmEvt.EVENT_ID.NAME LIKE '%Respiration Rate%' THEN anmEvt.RESULT END AS respRate,
       CASE WHEN anmEvt.EVENT_ID.NAME LIKE '%Temperature%' THEN anmEvt.RESULT END AS temp,
       CASE WHEN anmEvt.EVENT_ID.NAME LIKE '%Pulse Rate%' THEN anmEvt.RESULT END AS pulseRate,
       CASE WHEN anmEvt.EVENT_ID.NAME LIKE '%Pulse Oximetry%' THEN anmEvt.RESULT END AS pulseOximetry,
       CASE WHEN anmEvt.EVENT_ID.NAME LIKE '%EKG%' THEN anmEvt.RESULT END AS ekg,
       anmEvt.EVENT_ID.NAME AS type
FROM ANIMAL_EVENT anmEvt
         LEFT JOIN ANIMAL anm ON anmEvt.ANIMAL_ID = anm.ANIMAL_ID
         LEFT JOIN ANIMAL_EVENT_COMMENT anmCmt ON anmEvt.ANIMAL_EVENT_ID = anmCmt.ANIMAL_EVENT_ID
         LEFT JOIN EVENT_EVENT_GROUP evtEvtGrp ON evtEvtGrp.EVENT_ID = anmEvt.EVENT_ID
         LEFT JOIN AUDIT_TRAIL adt ON anmEvt.ANIMAL_EVENT_ID = substring(adt.PRIMARY_KEY_VALUES, length('ANIMAL_EVENT_ID = '))
                                   AND adt.PRIMARY_KEY_VALUES LIKE '%ANIMAL_EVENT_ID%'
WHERE evtEvtGrp.EVENT_ID IN (55, 56, 57, 1643, 1647, 1652, 1653, 1654, 1655, 2165, 2166, 2167, 2168, 2169)
        -- 55 Blood Pressure
        -- 57 EKG
        -- 1643 Temperature
        -- 56, 1647 Respiration Rate
        -- 1652 Heart Rate
        -- 1653 Pulse
        -- 1654 Pulse Rate
        -- 1655 Pulse Oximetry
        -- 2165 Blood Pressure-NHP
        -- 2166 Heart Rate-NHP
        -- 2167 Respiration Rate-NHP
        -- 2168 Temperature-NHP
        -- 2169 Pulse Rate-NHP
  AND anmEvt.CREATED_DATETIME < now() -- there are rows in ANIMAL_EVENT table with future dates

GROUP BY anmEvt.ANIMAL_EVENT_ID,
         anm.ANIMAL_ID_NUMBER,
         anmEvt.EVENT_DATETIME,
         adt.CHANGE_DATETIME,
         anmEvt.CREATED_DATETIME,
         anmEvt.EVENT_ID.NAME,
         anmEvt.RESULT,
         anmEvt.TEXT_RESULT
