SELECT
    ans.ANIMAL_SHIPMENT_ID          as AnimalShipmentId,
    ans.RECEIVED_BY_STAFF_ID.STAFF_FIRST_NAME
        || '|' || ans.RECEIVED_BY_STAFF_ID.STAFF_LAST_NAME as ReceivedByStaff,
    cg.cage                         as cage,
    rm.name                         as room,
    bu.Name                         as building,
    ar.area                         as area,
    ans.ANIMAL_DELIVERY_ID          as AnimalDelivery,
    ans.LOT_NUMBER_ID               as LotNumber,
    ans.RECEIVED_DATE               as ReceivedDate,
    ans.ANIMAL_BIRTH_DATE           as BirthDate,
    ans.NUMBER_MALES_RECEIVED       as MalesReceived,
    ans.NUMBER_FEMALES_RECEIVED     as FemalesReceived,
    ans.NUMBER_EITHER_RECEIVED      as EitherReceived,
    ans.CAN_UNRECEIVE_YN            as CanUnreceive,
    ans.COST_CENTER_ID              as CostCenter,
    ans.MATERNAL_ID                 as Dam,
    ans.PATERNAL_ID                 as Sire,
    ans.RECORDED_DATETIME           as RecordedDate,
    ans.RECORDED_BY_NAME            as RecordedBy
FROM ANIMAL_SHIPMENT ans
    LEFT JOIN q_cages cg ON cg.location = ans.LOCATION_ID
    LEFT JOIN q_rooms rm ON rm.room = ans.LOCATION_ID
    LEFT JOIN q_areas ar ON ar.LOCATION_ID = ans.LOCATION_ID
    LEFT JOIN q_buildings bu ON bu.BuildingId = ans.LOCATION_ID