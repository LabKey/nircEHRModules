ALTER TABLE nirc_ehr.ProtocolUsage DROP COLUMN SegmentId;
ALTER TABLE nirc_ehr.ProtocolUsage ADD COLUMN Species integer;

ALTER TABLE nirc_ehr.AnimalReqOrder DROP COLUMN Segment;
ALTER TABLE nirc_ehr.AnimalReqOrder ADD COLUMN Protocol integer;
ALTER TABLE nirc_ehr.AnimalReqOrder ADD COLUMN Species integer;

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