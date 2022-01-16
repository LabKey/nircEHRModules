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