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
    Protocol                INTEGER,
    Species                 VARCHAR,
    Stress                  INTEGER,
    Allowed                 INTEGER,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
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
    Protocol                INTEGER,
    Species                 VARCHAR,
    Procedure               VARCHAR,
    Container               entityId NOT NULL,
    Created                 TIMESTAMP,
    CreatedBy               USERID,
    Modified                TIMESTAMP,
    ModifiedBy              USERID,
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
    CONSTRAINT FK_CAGECARDHISTORY_Container FOREIGN KEY (Container) REFERENCES core.Containers (EntityId)
);
CREATE INDEX IX_Nirc_Ehr_Cage_Card_History_Container ON nirc_ehr.CageCardHistory (Container);