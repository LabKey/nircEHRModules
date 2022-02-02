SELECT
    ac.ACCOUNT_ID                  as AccountId,
    ac.ACCOUNT_NUMBER              as AccountNumber,
    ac.DEPARTMENT_ID               as department,
    ac.COST_TYPE_ID                as costType,
    ac.EXPENSE_CLASS_ID            as expenseClass,
    ac.PROJECT_CODE_ID             as projectCode,
    ac.ACCOUNT_DESC                as description,
    ac.ACTIVE_YN                   as active,
    cg.cage                        as cage,
    rm.name                        as room,
    bu.Name                        as building,
    ar.area                        as area
FROM ACCOUNT ac
         LEFT JOIN q_cages cg ON cg.location = ac.LOCATION_ID
         LEFT JOIN q_rooms rm ON rm.room = ac.LOCATION_ID
         LEFT JOIN q_areas ar ON ar.LOCATION_ID = ac.LOCATION_ID
         LEFT JOIN q_buildings bu ON bu.BuildingId = ac.LOCATION_ID