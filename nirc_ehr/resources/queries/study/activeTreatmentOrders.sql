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
WHERE isActive = true