SELECT sd.Id,
       sd.Date,
       sd.DataSet.Label AS datasetLabel,
       sd.taskid,
       sd.lsid

FROM study.StudyData sd
INNER JOIN core.QCState qc ON sd.QCState = qc.RowId
WHERE qc.Label = 'Review Required'
