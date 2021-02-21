-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE unfccc_variable (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    measure VARCHAR(255) NOT NULL,
    abbreviation VARCHAR(15) NOT NULL,
    unit_id INTEGER NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION UnfcccVariableVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER UnfcccVariableVersionInitializationTrigger
BEFORE INSERT
ON unfccc_variable
FOR EACH ROW
EXECUTE PROCEDURE UnfcccVariableVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION UnfcccVariableVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER UnfcccVariableVersionIncrementTrigger
BEFORE UPDATE
ON unfccc_variable
FOR EACH ROW
EXECUTE PROCEDURE UnfcccVariableVersionIncrementFunction();