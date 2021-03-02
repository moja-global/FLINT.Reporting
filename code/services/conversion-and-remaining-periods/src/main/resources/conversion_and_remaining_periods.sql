-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE conversion_and_remaining_period (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    previous_land_cover_id INTEGER NOT NULL,
    current_land_cover_id INTEGER NOT NULL,
    conversion_period INTEGER NOT NULL,
    remaining_period INTEGER NOT NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION ConversionAndRemainingPeriodVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER ConversionAndRemainingPeriodVersionInitializationTrigger7
BEFORE INSERT
ON conversion_and_remaining_period
FOR EACH ROW
EXECUTE PROCEDURE ConversionAndRemainingPeriodVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION ConversionAndRemainingPeriodVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER ConversionAndRemainingPeriodVersionIncrementTrigger
BEFORE UPDATE
ON conversion_and_remaining_period
FOR EACH ROW
EXECUTE PROCEDURE ConversionAndRemainingPeriodVersionIncrementFunction();