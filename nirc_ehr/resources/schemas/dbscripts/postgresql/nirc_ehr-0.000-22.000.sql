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

/* 21.xxx SQL scripts */

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/locations;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/housing');

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/project');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/assignment');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocolAssignment');

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/housing;truncate');

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/blood;truncate');

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/weight;truncate');

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/obs');

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/pairings');

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/serology');

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/vitals');

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/physicalExam');

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/prc');

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/exemptions');

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/drug');

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/necropsy');

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/breeder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/ehr_lookups;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/clinremarks');

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/cases');

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/ehr_lookups;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/blood;truncate');

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr;reports;/data/reports.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;ageclass;/data/ageclass.tsv');

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/ehr_lookups;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/locations;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/demographics;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/arrival;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/departure;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/housing;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/birth;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/prc;truncate');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/project;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocol;truncate');

SELECT core.executeJavaUpgradeCode('importFromTsv;ehr;reports;/data/reports.tsv');
SELECT core.executeJavaUpgradeCode('importFromTsv;ehr_lookups;ageclass;/data/ageclass.tsv');

SELECT core.executeJavaUpgradeCode('reloadFolder');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/ehr_lookups;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/locations;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/demographics;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/assignment;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/cases;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/arrival;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/departure;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/housing;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/birth;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/prc;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/blood;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/breeder;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/clinremarks;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/necropsy;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/drug;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/exemptions;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/physicalExam;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/vitals;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/serology;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/pairings;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/obs;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/weight;truncate');
SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocolAssignment;truncate');

SELECT core.executeJavaUpgradeCode('etl;{NIRC_EHR}/protocol;truncate');