-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE vegetation_history_vegetation_type (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    indicator_type_id INTEGER NOT NULL,
    indicator_data_source_id INTEGER NOT NULL,
    unit_id INTEGER NOT NULL,
    name VARCHAR(250) NOT NULL,
    year VARCHAR NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION VegetationHistoryVegetationTypeVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER VegetationHistoryVegetationTypeVersionInitializationTrigger7
BEFORE INSERT
ON vegetation_history_vegetation_type
FOR EACH ROW
EXECUTE PROCEDURE VegetationHistoryVegetationTypeVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION VegetationHistoryVegetationTypeVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER VegetationHistoryVegetationTypeVersionIncrementTrigger
BEFORE UPDATE
ON vegetation_history_vegetation_type
FOR EACH ROW
EXECUTE PROCEDURE VegetationHistoryVegetationTypeVersionIncrementFunction();