SELECT
    demo.Id,
    CASE WHEN d.QCState.Label = 'Request: Pending' OR d.QCState.Label = 'Review Required' THEN 'Necropsy Pending' ELSE demo.calculated_status END AS calculated_status
    FROM study.demographics demo
LEFT JOIN study.deaths d ON d.Id = demo.Id