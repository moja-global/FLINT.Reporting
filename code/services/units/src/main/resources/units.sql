-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE unit (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    unit_category_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    plural VARCHAR(255) NOT NULL,
    symbol VARCHAR(15) NOT NULL,
    scale_factor DOUBLE PRECISION NOT NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION UnitVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER UnitVersionInitializationTrigger
BEFORE INSERT
ON unit
FOR EACH ROW
EXECUTE PROCEDURE UnitVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION UnitVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER UnitVersionIncrementTrigger
BEFORE UPDATE
ON unit
FOR EACH ROW
EXECUTE PROCEDURE UnitVersionIncrementFunction();