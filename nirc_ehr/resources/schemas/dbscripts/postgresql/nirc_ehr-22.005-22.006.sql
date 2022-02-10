
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