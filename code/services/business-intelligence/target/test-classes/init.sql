-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE business_intelligence (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    indicator_type_id INTEGER NOT NULL,
    indicator_data_source_id INTEGER NOT NULL,
    unit_id INTEGER NOT NULL,
    name VARCHAR(250) NOT NULL,
    formulae VARCHAR NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION BusinessIntelligenceVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER BusinessIntelligenceVersionInitializationTrigger
BEFORE INSERT
ON business_intelligence
FOR EACH ROW
EXECUTE PROCEDURE BusinessIntelligenceVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION BusinessIntelligenceVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER BusinessIntelligenceVersionIncrementTrigger
BEFORE UPDATE
ON business_intelligence
FOR EACH ROW
EXECUTE PROCEDURE BusinessIntelligenceVersionIncrementFunction();


-- ------------------------------------------------------------
-- Add Some dummy data
-- ------------------------------------------------------------

INSERT INTO business_intelligence(indicator_type_id,indicator_data_source_id,unit_id,name,formulae) VALUES(1,1,1,'First BusinessIntelligence', null);
INSERT INTO business_intelligence(indicator_type_id,indicator_data_source_id,unit_id,name,formulae) VALUES(2,2,2,'Second BusinessIntelligence',null);
INSERT INTO business_intelligence(indicator_type_id,indicator_data_source_id,unit_id,name,formulae) VALUES(3,3,3,'Third BusinessIntelligence', 'Some Formulae');
