-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE emission_type (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    abbreviation VARCHAR(15) NOT NULL,
    description VARCHAR(255) NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION EmissionTypeVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER EmissionTypeVersionInitializationTrigger
BEFORE INSERT
ON emission_type
FOR EACH ROW
EXECUTE PROCEDURE EmissionTypeVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION EmissionTypeVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER EmissionTypeVersionIncrementTrigger
BEFORE UPDATE
ON emission_type
FOR EACH ROW
EXECUTE PROCEDURE EmissionTypeVersionIncrementFunction();