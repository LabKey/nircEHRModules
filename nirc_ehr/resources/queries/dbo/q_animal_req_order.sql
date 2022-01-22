SELECT
    ar.ANIMAL_REQ_ORDER_ID             as AnimalReqOrderId,
    ar.ANIMAL_VENDOR_ID                as AnimalVendor,
    ar.REQUISITIONER_STAFF_ID.STAFF_FIRST_NAME
        || '|' || ar.REQUISITIONER_STAFF_ID.STAFF_LAST_NAME as RequisitionerStaff,
    ar.REQUEST_NUMBER                  as RequestNumber,
    ar.REQUISITION_NUMBER              as RequisitionNumber,
    ar.BILL_YN                         as Bill,
    ar.SHIPPING_CONTACT                as ShippingContact,
    ar.CREATE_DATE                     as CreateDate,
    ar.REQ_ORDER_TYPE_ID               as ReqOrderType,
    ar.REQ_ORDER_STATE_ID              as ReqOrderState,
    ar.BILL_TO_ACCOUNT_ID              as BillToAccount,
    ar.BILL_TO_STAFF_ID.STAFF_FIRST_NAME
        || '|' || ar.BILL_TO_STAFF_ID.STAFF_LAST_NAME as BillToStaff,
    ar.PER_DIEM_ACCOUNT_ID             as PerDiemAccount,
    ar.PER_DIEM_STAFF_ID.STAFF_FIRST_NAME
        || '|' || ar.PER_DIEM_STAFF_ID.STAFF_LAST_NAME as PerDiemStaff,
    ar.SUBMITTED_BY_STAFF_ID.STAFF_FIRST_NAME
        || '|' || ar.SUBMITTED_BY_STAFF_ID.STAFF_LAST_NAME as SubmittedByStaff,
    ar.APPROVED_BY_STAFF_ID.STAFF_FIRST_NAME
        || '|' || ar.APPROVED_BY_STAFF_ID.STAFF_LAST_NAME as ApprovedByStaff,
    ar.SUBMITTED_DATE                  as SubmittedDate,
    ar.APPROVED_DATE                   as ApprovedDate,
    ar.SEGMENT_ID                      as Segment,
    ar.PROJECT_CODE_ID                 as Project,
    cg.cage                            as SiteCage,
    rm.name                            as SiteRoom,
    bu.Name                            as SiteBuilding,
    area.area                          as SiteArea,
    ar.CREATED_BY_STAFF_ID.STAFF_FIRST_NAME
        || '|' || ar.CREATED_BY_STAFF_ID.STAFF_LAST_NAME as CreatedByStaff
FROM ANIMAL_REQ_ORDER ar
    LEFT JOIN q_cages cg ON cg.location = ar.SITE_LOCATION_ID
    LEFT JOIN q_rooms rm ON rm.room = ar.SITE_LOCATION_ID
    LEFT JOIN q_areas area ON area.LOCATION_ID = ar.SITE_LOCATION_ID
    LEFT JOIN q_buildings bu ON bu.BuildingId = ar.SITE_LOCATION_ID
