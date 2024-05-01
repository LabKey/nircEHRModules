SELECT DISTINCT
anmEvt.EVENT_ID.NAME AS category
FROM ANIMAL_EVENT anmEvt
    LEFT JOIN EVENT_EVENT_GROUP evtEvtGrp ON evtEvtGrp.EVENT_ID = anmEvt.EVENT_ID
WHERE evtEvtGrp.EVENT_GROUP_ID IN (45,46)
  -- 45 Food Enrichment Exemption
  -- 46 Environmental Enrichment Exemption