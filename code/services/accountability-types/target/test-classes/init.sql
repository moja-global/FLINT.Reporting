-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE accountability_type (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    name VARCHAR(250) NOT NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION AccountabilityTypeVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER AccountabilityTypeVersionInitializationTrigger
BEFORE INSERT
ON accountability_type
FOR EACH ROW
EXECUTE PROCEDURE AccountabilityTypeVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION AccountabilityTypeVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER AccountabilityTypeVersionIncrementTrigger
BEFORE UPDATE
ON accountability_type
FOR EACH ROW
EXECUTE PROCEDURE AccountabilityTypeVersionIncrementFunction();


-- ------------------------------------------------------------
-- Add Some dummy data
-- ------------------------------------------------------------

INSERT INTO accountability_type(name) VALUES('First AccountabilityType');
INSERT INTO accountability_type(name) VALUES('Second AccountabilityType');
INSERT INTO accountability_type(name) VALUES('Third AccountabilityType');
