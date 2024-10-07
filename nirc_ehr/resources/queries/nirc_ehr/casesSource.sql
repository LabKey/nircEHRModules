-- Only used for ETL from staging table to cases table
SELECT objectid,
       Id,
       date,
       performedby,
       openRemark,
       closeRemark,
       openDiagnosis,
       closeDiagnosis,
       enddate,
       'Clinical' as category,
       attachmentFile
FROM CasesTemp
WHERE category = 'Presenting Diagnosis' AND (openRemark IS NULL OR openRemark NOT LIKE 'Error:%')