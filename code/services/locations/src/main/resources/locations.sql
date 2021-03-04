-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE location (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    indicator_type_id INTEGER NOT NULL,
    indicator_data_source_id INTEGER NOT NULL,
    unit_id INTEGER NOT NULL,
    name VARCHAR(250) NOT NULL,
    unitAreaSum VARCHAR NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION LocationVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER LocationVersionInitializationTrigger7
BEFORE INSERT
ON location
FOR EACH ROW
EXECUTE PROCEDURE LocationVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION LocationVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER LocationVersionIncrementTrigger
BEFORE UPDATE
ON location
FOR EACH ROW
EXECUTE PROCEDURE LocationVersionIncrementFunction();