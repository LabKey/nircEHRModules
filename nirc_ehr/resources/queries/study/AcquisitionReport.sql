SELECT d.Id,
       d.gender,
       d.species,
       d.Birth,
       d.Id.MostRecentArrival.Center_Arrival,
       cpp.project
FROM study.demographics d
         LEFT JOIN CurrentProtocolProjectReport cpp ON cpp.Id = d.Id
ORDER BY d.Id ASC