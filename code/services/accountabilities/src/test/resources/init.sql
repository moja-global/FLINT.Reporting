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

CREATE TRIGGER AccountabilityVersionInitializationTrigger
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


-- ------------------------------------------------------------
-- Add Some dummy data
-- ------------------------------------------------------------

INSERT INTO accountability(accountability_type_id,accountability_rule_id,parent_party_id,subsidiary_party_id) VALUES(1,1,1,1);
INSERT INTO accountability(accountability_type_id,accountability_rule_id,parent_party_id,subsidiary_party_id) VALUES(2,2,2,2);
INSERT INTO accountability(accountability_type_id,accountability_rule_id,parent_party_id,subsidiary_party_id) VALUES(3,3,3,3);
INSERT INTO accountability(accountability_type_id,accountability_rule_id,subsidiary_party_id) VALUES(4,4,4);
