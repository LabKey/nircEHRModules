
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