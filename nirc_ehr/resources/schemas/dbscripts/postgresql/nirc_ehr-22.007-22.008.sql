
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