SELECT
    Id,
    date,
    enddate,
    code,
    frequency,
    route,
    volume,
    vol_units,
    amount,
    amount_units,
    concentration,
    conc_units,
    dosage,
    dosage_units,
    performedBy,
    description,
    remark
FROM treatment_order
WHERE enddate IS NULL OR enddate >= NOW() -- calculated col. isActive is false when the enddate is in the future, so using this condition instead