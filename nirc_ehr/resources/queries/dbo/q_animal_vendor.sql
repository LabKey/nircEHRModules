SELECT
    ANIMAL_VENDOR_ID            as AnimalVendorId,
    VENDOR_APPROVAL_CODE_ID     as VendorApprovalCode,
    VENDOR_Name                 as VendorName,
    STREET_ADDRESS1             as StreetAddress1,
    STREET_ADDRESS2             as StreetAddress2,
    CITY                        as City,
    STATE_PROV                  as StateProv,
    COUNTRY                     as Country,
    ZIP                         as Zip,
    ZIP_EXT                     as ZipExt,
    PHONE_NUMBER                as PhoneNumber,
    FAX_NUMBER                  as FaxNumber,
    COMMENTS                    as Comments,
    INTERNAL_VENDOR_YN          as InternalVendor
FROM ANIMAL_VENDOR
