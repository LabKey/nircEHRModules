CREATE TABLE nirc_ehr.Lot
(
    LotId               INTEGER,
    AnimalShipment      INTEGER,
    Date                TIMESTAMP,
    LotSequence         INTEGER,
    Container           entityId NOT NULL,
    Created             TIMESTAMP,
    CreatedBy           USERID,
    Modified            TIMESTAMP,
    ModifiedBy          USERID,
    CONSTRAINT PK_LOT PRIMARY KEY (LotId),
    CONSTRAINT FK_LOT_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_Lot_Container ON nirc_ehr.Lot (Container);

CREATE TABLE nirc_ehr.AnimalShipment
(
    AnimalShipmentId    INTEGER,
    ReceivedByStaff     USERID,
    Cage                TEXT,
    Room                TEXT,
    Building            TEXT,
    Area                TEXT,
    AnimalDelivery      INTEGER,
    LotNumber           INTEGER,
    ReceivedDate        TIMESTAMP,
    BirthDate           TIMESTAMP,
    MalesReceived       INTEGER,
    FemalesReceived     INTEGER,
    EitherReceived      INTEGER,
    CanUnreceive        BOOLEAN,
    CostCenter          INTEGER,
    Dam                 TEXT,
    Sire                TEXT,
    RecordedDate        TIMESTAMP,
    RecordedBy          TEXT,
    Container           entityId NOT NULL,
    Created             TIMESTAMP,
    CreatedBy           USERID,
    Modified            TIMESTAMP,
    ModifiedBy          USERID,
    CONSTRAINT PK_ANIMALSHIPMENT PRIMARY KEY (AnimalShipmentId),
    CONSTRAINT FK_ANIMALSHIPMENT_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_Animal_Shipment_Container ON nirc_ehr.AnimalShipment (Container);


CREATE TABLE nirc_ehr.AnimalDelivery
(
    AnimalDeliveryId        INTEGER,
    AnimalShipment          INTEGER,
    ShipTo                  INTEGER,
    cage                    TEXT,
    room                    TEXT,
    building                TEXT,
    area                    TEXT,
    AnimalReqOrder          INTEGER,
    DeliveryState           INTEGER,
    DeliveryNumber          INTEGER,
    ExpectedDate            TIMESTAMP,
    Project                 INTEGER,
    BillToAccount           INTEGER,
    BillToStaff             USERID,
    PerDiemAccount          INTEGER,
    PerDiemStaff            USERID,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
    CONSTRAINT PK_ANIMALDELIVERY PRIMARY KEY (AnimalDeliveryId),
    CONSTRAINT FK_ANIMALDELIVER_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_Animal_Delivery_Container ON nirc_ehr.AnimalDelivery (Container);


CREATE TABLE nirc_ehr.AnimalDeliveryEsig
(
    EsigId                  INTEGER,
    AnimalDelivery          INTEGER,
    EsigEvent               INTEGER,
    UserProfile             USERID,
    EsigDateTime            TIMESTAMP,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
    CONSTRAINT PK_ANIMALDELIVERYESIG PRIMARY KEY (EsigId),
    CONSTRAINT FK_ANIMALDELIVERESIG_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_Animal_Delivery_Esig_Container ON nirc_ehr.AnimalDeliveryEsig (Container);


CREATE TABLE nirc_ehr.AnimalReqOrder
(
    AnimalReqOrderId        INTEGER,
    AnimalVendor            INTEGER,
    RequisitionerStaff      USERID,
    RequestNumber           TEXT,
    RequisitionNumber       TEXT,
    Bill                    BOOLEAN,
    ShippingContact         TEXT,
    CreateDate              TIMESTAMP,
    ReqOrderType            INTEGER,
    ReqOrderState           INTEGER,
    BillToAccount           INTEGER,
    BillToStaff             USERID,
    PerDiemAccount          INTEGER,
    PerDiemStaff            USERID,
    SubmittedByStaff        USERID,
    ApprovedByStaff         USERID,
    SubmittedDate           TIMESTAMP,
    ApprovedDate            TIMESTAMP,
    Segment                 INTEGER,
    Project                 INTEGER,
    SiteCage                TEXT,
    SiteRoom                TEXT,
    SiteBuilding            TEXT,
    SiteArea                TEXT,
    CreatedByStaff          USERID,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
    CONSTRAINT PK_ANIMALREQORDER PRIMARY KEY (AnimalReqOrderId),
    CONSTRAINT FK_ANIMALREQORDER_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_Animal_Req_Order_Container ON nirc_ehr.AnimalReqOrder (Container);


CREATE TABLE nirc_ehr.AnimalReqOrderEsig
(
    EsigId                  INTEGER,
    AnimalReqOrder          INTEGER,
    EsigEvent               INTEGER,
    UserProfile             USERID,
    EsigDateTime            TIMESTAMP,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
    CONSTRAINT PK_ANIMALREQORDERESIG PRIMARY KEY (EsigId),
    CONSTRAINT FK_ANIMALREQORDERESIG_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_Animal_Req_Order_Esig_Container ON nirc_ehr.AnimalReqOrderEsig (Container);


CREATE TABLE nirc_ehr.AnimalVendor
(
    AnimalVendorId          INTEGER,
    VendorApprovalCode      INTEGER,
    VendorName              TEXT,
    StreetAddress1          TEXT,
    StreetAddress2          TEXT,
    City                    TEXT,
    StateProv               TEXT,
    Country                 TEXT,
    Zip                     TEXT,
    ZipExt                  TEXT,
    PhoneNumber             TEXT,
    FaxNumber               TEXT,
    Comments                TEXT,
    InternalVendor          TEXT,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
    CONSTRAINT PK_ANIMALVENDOR PRIMARY KEY (AnimalVendorId),
    CONSTRAINT FK_ANIMALVENDOR_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_Animal_Vendor_Container ON nirc_ehr.AnimalVendor (Container);


CREATE TABLE nirc_ehr.ShipTo
(
    ShipToId                INTEGER,
    Name                    TEXT,
    Country                 TEXT,
    City                    TEXT,
    StreetAddress1          TEXT,
    StreetAddress2          TEXT,
    StateProv               TEXT,
    Zip                     TEXT,
    ZipExt                  TEXT,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
    CONSTRAINT PK_SHIPTO PRIMARY KEY (ShipToId),
    CONSTRAINT FK_SHIPTO_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_Ship_To_Container ON nirc_ehr.ShipTo (Container);


SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;lookup_sets;/data/lookup_sets.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;country;/data/country.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;country_category;/data/country_category.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;delivery_state;/data/delivery_state.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;esig_events;/data/esig_events.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;req_order_state;/data/req_order_state.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;req_order_type;/data/req_order_type.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;vendor_approval_code;/data/vendor_approval_code.tsv');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/animalDelivery');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/animalReqOrder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/animalShipment');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/animalVendor');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/lotNumber');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/shipTo');