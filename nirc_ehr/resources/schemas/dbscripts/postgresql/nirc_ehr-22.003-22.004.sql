
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
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;cost_center;/data/cost_center.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;cost_type;/data/cost_type.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;expense_class;/data/expense_class.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;protocol_category;/data/protocol_category.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;protocol_state;/data/protocol_state.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;protocol_type;/data/protocol_type.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;questionnaire;/data/questionnaire.tsv');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/staff;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocol;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocolEsig');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocolUsage');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/procedure');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/account');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/department');
