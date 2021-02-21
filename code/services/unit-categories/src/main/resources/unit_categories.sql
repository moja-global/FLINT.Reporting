-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE unit_category (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION UnitCategoryVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER UnitCategoryVersionInitializationTrigger
BEFORE INSERT
ON unit_category
FOR EACH ROW
EXECUTE PROCEDURE UnitCategoryVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION UnitCategoryVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER UnitCategoryVersionIncrementTrigger
BEFORE UPDATE
ON unit_category
FOR EACH ROW
EXECUTE PROCEDURE UnitCategoryVersionIncrementFunction();