CREATE SCHEMA nirc_ehr;

CREATE TABLE nirc_ehr.LocationTypes
(
    RowId           SERIAL NOT NULL,
    Created         TIMESTAMP,
    CreatedBy       USERID,
    Modified        TIMESTAMP,
    ModifiedBy      USERID,
    Container       entityId NOT NULL,

    NAME            TEXT,
    LocationTypeId      INTEGER,

    CONSTRAINT PK_LocationTypes PRIMARY KEY (RowId),
    CONSTRAINT FK_LocationTypes_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_nirc_ehr_LocationTypes_Container ON nirc_ehr.LocationTypes (Container);

CREATE TABLE nirc_ehr.Locations
(
    RowId           SERIAL NOT NULL,
    Created         TIMESTAMP,
    CreatedBy       USERID,
    Modified        TIMESTAMP,
    ModifiedBy      USERID,
    Container       entityId NOT NULL,

    NAME            TEXT,
    LocationId      INTEGER,
    LocationType    INTEGER,

    CONSTRAINT PK_Locations PRIMARY KEY (RowId),
    CONSTRAINT FK_Locations_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_nirc_ehr_Locations_Container ON nirc_ehr.Locations (Container);

CREATE TABLE nirc_ehr.LocationsMapping
(
    RowId           SERIAL NOT NULL,
    Created         TIMESTAMP,
    CreatedBy       USERID,
    Modified        TIMESTAMP,
    ModifiedBy      USERID,
    Container       entityId NOT NULL,

    ParentLocation  INTEGER,
    ChildLocation   INTEGER,
    NAME            TEXT,

    CONSTRAINT PK_LocationsMapping PRIMARY KEY (RowId),
    CONSTRAINT FK_LocationsMapping_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_nirc_ehr_LocationsMapping_Container ON nirc_ehr.LocationsMapping (Container);

/* 22.xxx SQL scripts */

CREATE TABLE nirc_ehr.Staff
(
    staffId             INTEGER NOT NULL,
    email               TEXT,
    firstName           TEXT,
    lastName            TEXT,
    middleName          TEXT,
    displayName         TEXT,
    hireDate            TIMESTAMP,
    lastEmployDate      TIMESTAMP,
    officePhone         TEXT,
    officePhoneExt      TEXT,
    officeFax           TEXT,
    homePhone           TEXT,
    beeperPhone         TEXT,
    cellPhone           TEXT,
    emergencyContact    TEXT,
    emergencyPhone      TEXT,
    homeAddress1        TEXT,
    homeAddress2        TEXT,
    homeCity            TEXT,
    homeState           TEXT,
    homeZip             TEXT,
    homeZipExt          TEXT,
    homeCountry         TEXT,
    officeAddress1      TEXT,
    officeAddress2      TEXT,
    officeCity          TEXT,
    officeState         TEXT,
    officeZip           TEXT,
    officeZipExt        TEXT,
    officeCountry       TEXT,
    birthDate           TIMESTAMP,
    hsName              TEXT,
    collegeGradDate     TEXT,
    collegeDegree       TEXT,
    collegeMajor        TEXT,
    collegeName         TEXT,
    supervisor          INTEGER,
    positionName        TEXT,
    positionDesc        TEXT,
    active              BOOLEAN,
    Container           entityId NOT NULL,
    CONSTRAINT PK_STAFF PRIMARY KEY (staffId),
    CONSTRAINT FK_STAFF_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_nirc_ehr_Staff_Container ON nirc_ehr.Staff (Container);

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/staff;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/alopecia;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/arrival;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/biopsy;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/blood;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/breeder;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/cases;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/chemistryResults;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/clinremarks;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/departure;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/drug;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/exemptions;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/flags;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/foster;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/housing;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/necropsy;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/notes;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/obs;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/pairings;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/physicalExam;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/prc;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/pregnancy;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/project;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/assignment;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocol;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocolAssignment;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/serology;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/vitals;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/weight;truncate');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/arrival;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/birth;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/deaths;truncate');

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

ALTER TABLE nirc_ehr.Staff
ADD COLUMN Created TIMESTAMP,
ADD COLUMN CreatedBy USERID,
ADD COLUMN Modified TIMESTAMP,
ADD COLUMN ModifiedBy USERID;


CREATE TABLE nirc_ehr.ProtocolEsig
(
    EsigId                  INTEGER,
    Protocol                INTEGER,
    EsigEvent               INTEGER,
    UserProfile             USERID,
    EsigDateTime            TIMESTAMP,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
    CONSTRAINT PK_PROTOCOLESIG PRIMARY KEY (EsigId),
    CONSTRAINT FK_PROTOCOLESIG_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_Protocol_Esig_Container ON nirc_ehr.ProtocolEsig (Container);

CREATE TABLE nirc_ehr.ProtocolUsage
(
    Protocol                TEXT,
    UserReferenceNumber     TEXT,
    ShortTitle              TEXT,
    Investigator            TEXT,
    ProtocolType            TEXT,
    CurrentState            TEXT,
    SubmitDate              TIMESTAMP,
    ApprovalDate            TIMESTAMP,
    ExpirationDate          TIMESTAMP,
    EffectiveDate           TIMESTAMP,
    RenewalDate             TIMESTAMP,
    AuthorizedAmt           FLOAT,
    OnOrderAmt              FLOAT,
    UsedAmt                 FLOAT,
    AvailableAmt            FLOAT,
    PercentLeft             FLOAT,
    SegmentId               INTEGER,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
    CONSTRAINT FK_PROTOCOLUSAGE_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_Protocol_Usage_Container ON nirc_ehr.ProtocolUsage (Container);

CREATE TABLE nirc_ehr.Account
(
    AccountId               INTEGER,
    AccountNumber           TEXT,
    Department              INTEGER,
    CostType                INTEGER,
    ExpenseClass            INTEGER,
    ProjectCode             INTEGER,
    Description             TEXT,
    Active                  BOOLEAN,
    Cage                    TEXT,
    Room                    TEXT,
    Building                TEXT,
    Area                    TEXT,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
    CONSTRAINT PK_ACCOUNT PRIMARY KEY (AccountId),
    CONSTRAINT FK_ACCOUNT_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_Account_Container ON nirc_ehr.Account (Container);


CREATE TABLE nirc_ehr.Department
(
    DepartmentId            INTEGER,
    Name                    TEXT,
    Description             TEXT,
    ParentDepartment        INTEGER,
    Staff                   USERID,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
    CONSTRAINT PK_DEPARTMENT PRIMARY KEY (DepartmentId),
    CONSTRAINT FK_DEPARTMENT_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_Department_Container ON nirc_ehr.Department (Container);

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;lookup_sets;/data/lookup_sets.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;cost_type;/data/cost_type.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;expense_class;/data/expense_class.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;protocol_category;/data/protocol_category.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;protocol_state;/data/protocol_state.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;protocol_type;/data/protocol_type.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;questionnaire;/data/questionnaire.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;gender_codes;/data/gender_codes.tsv');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/ehr_lookups');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/staff;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocol;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocolEsig');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocolUsage');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/procedure');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/account');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/department');

ALTER TABLE nirc_ehr.AnimalShipment ADD COLUMN Floor VARCHAR;
ALTER TABLE nirc_ehr.AnimalDelivery ADD COLUMN Floor VARCHAR;


CREATE TABLE nirc_ehr.IdHistory
(
    Id                      VARCHAR,
    Name                    VARCHAR,
    Type                    INTEGER,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
    CONSTRAINT PK_IDHISTORY PRIMARY KEY (Id, Name),
    CONSTRAINT FK_IDHISTORY_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_ID_History_Container ON nirc_ehr.IdHistory (Container);


SELECT core.executeJavaUpgradeCode('importTemplate;nirc_ehr;ehr_lookups');
SELECT core.executeJavaUpgradeCode('importTemplate;nirc_ehr;ehr');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;lookup_sets;/data/lookup_sets.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;id_history_type;/data/id_history_type.tsv');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/locations;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/demographics;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/account;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/animalDelivery;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/animalReqOrder;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/animalShipment;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/ProtocolAndAssignment;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/idHistory');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/housing;truncate');

CREATE TABLE nirc_ehr.DeletedRecord
(
    Id                      INTEGER,
    Protocol                VARCHAR,
    UserReferenceNumber     VARCHAR,
    Investigator            VARCHAR,
    Title                   VARCHAR,
    EsigEvent               INTEGER,
    EsigUser                USERID,
    EsigDate                TIMESTAMP,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
    CONSTRAINT PK_DELETEDRECORD PRIMARY KEY (Id),
    CONSTRAINT FK_DELETEDRECORD_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_Deleted_Record_Container ON nirc_ehr.DeletedRecord (Container);

CREATE TABLE nirc_ehr.Question
(
    QuestionId              INTEGER,
    Type                    VARCHAR,
    Text                    VARCHAR,
    CreationDate            TIMESTAMP,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
    CONSTRAINT PK_QUESTION PRIMARY KEY (QuestionId),
    CONSTRAINT FK_QUESTION_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_Question_Container ON nirc_ehr.Question (Container);

CREATE TABLE nirc_ehr.QuestionResponse
(
    QuestionResponseId      INTEGER,
    Protocol                INTEGER,
    Response                VARCHAR,
    Question                INTEGER,
    Questionnaire           INTEGER,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
    CONSTRAINT PK_QUESTIONRESPONSE PRIMARY KEY (QuestionResponseId),
    CONSTRAINT FK_QUESTIONRESPONSE_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_Question_Response_Container ON nirc_ehr.QuestionResponse (Container);

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/deletedRecord');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/questionResponse');

ALTER TABLE nirc_ehr.AnimalVendor ADD COLUMN VendorProductionLocation INTEGER;

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;lookup_sets;/data/lookup_sets.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;qualifier;/data/qualifier.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;numeric_unit;/data/numeric_unit.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;vendor_production_location;/data/vendor_production_location.tsv');

SELECT core.executeJavaUpgradeCode('reloadFolder');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/alopecia;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/blood;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/vitals;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/physicalExam;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/weight;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/animalVendor;truncate');

ALTER TABLE nirc_ehr.ProtocolUsage DROP COLUMN SegmentId;
ALTER TABLE nirc_ehr.ProtocolUsage ADD COLUMN Species integer;

ALTER TABLE nirc_ehr.AnimalReqOrder DROP COLUMN Segment;
ALTER TABLE nirc_ehr.AnimalReqOrder ADD COLUMN Protocol integer;
ALTER TABLE nirc_ehr.AnimalReqOrder ADD COLUMN Species integer;

ALTER TABLE nirc_ehr.Account DROP COLUMN cage;
ALTER TABLE nirc_ehr.Account DROP COLUMN room;
ALTER TABLE nirc_ehr.Account DROP COLUMN building;

CREATE TABLE nirc_ehr.ProtocolStress
(
    RowId                   SERIAL NOT NULL,
    Protocol                INTEGER,
    Species                 VARCHAR,
    Stress                  INTEGER,
    Allowed                 INTEGER,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
    CONSTRAINT PK_PROTOCOLSTRESS PRIMARY KEY (RowId),
    CONSTRAINT FK_PROTOCOLSTRESS_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_ProtocolStress_Container ON nirc_ehr.ProtocolStress (Container);

CREATE TABLE nirc_ehr.Stress
(
    StressId                INTEGER,
    Name                    VARCHAR,
    Description             VARCHAR,
    Ranking                 INTEGER,
    RegulatoryStressLevel   INTEGER,
    Active                  BOOLEAN,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
    CONSTRAINT PK_STRESS PRIMARY KEY (StressId),
    CONSTRAINT FK_STRESS_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_Stress_Container ON nirc_ehr.Stress (Container);

CREATE TABLE nirc_ehr.ProtocolProcedures
(
    RowId                   SERIAL NOT NULL,
    Protocol                INTEGER,
    Species                 VARCHAR,
    Procedure               VARCHAR,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
    CONSTRAINT PK_PROTOCOLPROCEDURES PRIMARY KEY (RowId),
    CONSTRAINT FK_PROTOCOLPROCEDURES_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_ProtocolProcedures_Container ON nirc_ehr.ProtocolProcedures (Container);

CREATE TABLE nirc_ehr.CageCard
(
    CageCardId              INTEGER,
    Protocol                INTEGER,
    Species                 INTEGER,
    CardFormat              INTEGER,
    CostCenter              INTEGER,
    GenerationDate          TIMESTAMP,
    UpdateDate              TIMESTAMP,
    NumberOfAnimals         INTEGER,
    Cage                    INTEGER,
    Room                    INTEGER,
    Floor                   INTEGER,
    Building                INTEGER,
    Area                    INTEGER,
    Account                 INTEGER,
    AccountStaff            USERID,
    CensusActivityStatus    VARCHAR,
    CensusActivityDate      TIMESTAMP,
    AnimalDelivery          INTEGER,
    AnimalRequestedByStaff  USERID,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
    CONSTRAINT PK_CAGECARD PRIMARY KEY (CageCardId),
    CONSTRAINT FK_CAGECARD_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_Cage_Card_Container ON nirc_ehr.CageCard (Container);

CREATE TABLE nirc_ehr.CageCardHistory
(
    RowId                   SERIAL NOT NULL,
    CageCardId              INTEGER,
    AnimalDelivery          INTEGER,
    Protocol                INTEGER,
    Species                 INTEGER,
    CardFormat              INTEGER,
    CostCenter              INTEGER,
    GenerationDate          TIMESTAMP,
    UpdateDate              TIMESTAMP,
    NumberOfAnimals         INTEGER,
    Cage                    INTEGER,
    Room                    INTEGER,
    Floor                   INTEGER,
    Building                INTEGER,
    Area                    INTEGER,
    Account                 INTEGER,
    AccountStaff            USERID,
    CensusActivityStatus    VARCHAR,
    CensusActivityDate      TIMESTAMP,
    CensusStartDate         TIMESTAMP,
    CensusStopDate          TIMESTAMP,
    TransferDate            TIMESTAMP,
    ActionDate              TIMESTAMP,
    RetrospectiveStress     INTEGER,
    AnimalRequestedByStaff  USERID,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
    CONSTRAINT PK_CAGECARDHISTORY PRIMARY KEY (RowId),
    CONSTRAINT FK_CAGECARDHISTORY_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_Cage_Card_History_Container ON nirc_ehr.CageCardHistory (Container);

SELECT core.executeJavaUpgradeCode('importTemplate;nirc_ehr;ehr');

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;lookup_sets;/data/lookup_sets.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;card_format;/data/card_format.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;census_activity_status;/data/census_activity_status.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;euthanasia_type;/data/euthanasia_type.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;regulatory_stress_levels;/data/regulatory_stress_levels.tsv');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocol;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocolAssignment;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocolCounts');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocolSupplement');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocolUsage;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/cageCard');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/stress');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/animalReqOrder;truncate');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/housing;truncate');

SELECT core.executeJavaUpgradeCode('reloadFolder');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/demographics;truncate');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/demographics;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/deaths;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/birth;truncate');

SELECT core.executeJavaUpgradeCode('reloadFolder');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/necropsy;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/biopsy;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/breeder;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/drug;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/cases;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/pregnancy;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/notes;truncate');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/necropsy;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/biopsy;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/breeder;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/drug;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/cases;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/pregnancy;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/notes;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/housing;truncate');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/demographics;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/birth;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/deaths;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/alopecia;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/arrival;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/assignment;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/biopsy;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/birth;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/blood;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/breeder;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/cases;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/chemistryResults;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/clinremarks;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/departure;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/drug;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/exemptions;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/flags;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/foster;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/housing;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/necropsy;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/notes;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/obs;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/pairings;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/physicalExam;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/prc;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/pregnancy;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocolAssignment;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/serology;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/vitals;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/weight;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/ProtocolAndAssignment;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/ProjectAndAssignment;truncate');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/demographics;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/housing;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/necropsy;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/notes;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/obs;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/pairings;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/physicalExam;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/prc;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/pregnancy;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/serology;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/vitals;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/weight;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/project;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/assignment;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocol;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocolAssignment;truncate');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/project;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/assignment;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocol;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocolAssignment;truncate');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/departure;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/assignment;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocolAssignment;truncate');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/ProjectAndAssignment');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/ProtocolAndAssignment');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/ProjectAndAssignment');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/ProtocolAndAssignment');

CREATE TABLE nirc_ehr.CasesTemp
(
    Id                  TEXT,
    date                TIMESTAMP,
    enddate             TIMESTAMP,
    objectid            TEXT,
    category            TEXT,
    openDiagnosis       TEXT,
    closeDiagnosis      TEXT,
    openRemark          TEXT,
    closeRemark         TEXT,
    attachmentFile      TEXT,
    performedby         USERID
);

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/cases;truncate');