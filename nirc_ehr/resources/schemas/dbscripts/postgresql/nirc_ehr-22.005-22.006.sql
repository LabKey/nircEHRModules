
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