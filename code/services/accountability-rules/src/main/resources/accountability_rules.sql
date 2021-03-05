-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE accountability_rule (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    accountability_type_id INTEGER NOT NULL,
    parent_party_type_id INTEGER NOT NULL,
    subsidiary_party_type_id INTEGER NOT NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION AccountabilityRuleVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER AccountabilityRuleVersionInitializationTrigger7
BEFORE INSERT
ON accountability_rule
FOR EACH ROW
EXECUTE PROCEDURE AccountabilityRuleVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION AccountabilityRuleVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER AccountabilityRuleVersionIncrementTrigger
BEFORE UPDATE
ON accountability_rule
FOR EACH ROW
EXECUTE PROCEDURE AccountabilityRuleVersionIncrementFunction();