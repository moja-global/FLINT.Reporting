-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE accountability (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    accountability_type_id INTEGER NOT NULL,
    accountability_rule_id INTEGER NOT NULL,
    parent_party_id INTEGER NULL,
    subsidiary_party_id INTEGER NOT NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION AccountabilityVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER AccountabilityVersionInitializationTrigger7
BEFORE INSERT
ON accountability
FOR EACH ROW
EXECUTE PROCEDURE AccountabilityVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION AccountabilityVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER AccountabilityVersionIncrementTrigger
BEFORE UPDATE
ON accountability
FOR EACH ROW
EXECUTE PROCEDURE AccountabilityVersionIncrementFunction();