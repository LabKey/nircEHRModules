SELECT
    ad.ANIMAL_DELIVERY_ID          as AnimalDeliveryId,
    ad.ANIMAL_SHIPMENT_ID          as AnimalShipment,
    ad.SHIPTO_ID                   as ShipTo,
    cg.cage                        as cage,
    rm.name                        as room,
    bu.Name                        as building,
    ar.area                        as area,
    ad.ANIMAL_REQ_ORDER_ID         as AnimalReqOrder,
    ad.DELIVERY_STATE_ID           as DeliveryState,
    ad.DELIVERY_NUMBER             as DeliveryNumber,
    ad.EXPECTED_DATE               as ExpectedDate,
    ad.PROJECT_CODE_ID             as Project,
    ad.BILL_TO_ACCOUNT_ID          as BillToAccount,
    ad.BILL_TO_STAFF_ID.STAFF_FIRST_NAME
        || '|' || ad.BILL_TO_STAFF_ID.STAFF_LAST_NAME as BillToStaff,
    ad.PER_DIEM_ACCOUNT_ID         as PerDiemAccount,
    ad.PER_DIEM_STAFF_ID.STAFF_FIRST_NAME
        || '|' || ad.PER_DIEM_STAFF_ID.STAFF_LAST_NAME as PerDiemStaff,
FROM ANIMAL_DELIVERY ad
    LEFT JOIN q_cages cg ON cg.location = ad.LOCATION_ID
    LEFT JOIN q_rooms rm ON rm.room = ad.LOCATION_ID
    LEFT JOIN q_areas ar ON ar.LOCATION_ID = ad.LOCATION_ID
    LEFT JOIN q_buildings bu ON bu.BuildingId = ad.LOCATION_ID