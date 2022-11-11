-- Only used for ETL from staging table to cases table
SELECT objectid,
       Id,
       date,
       modified,
       performedby,
       openRemark,
       closeRemark,
       openDiagnosis,
       closeDiagnosis,
       enddate,
       category,
       attachmentFile
FROM casesTemp
WHERE category = 'Presenting Diagnosis'