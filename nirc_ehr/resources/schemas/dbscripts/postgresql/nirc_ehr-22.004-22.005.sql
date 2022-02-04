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
