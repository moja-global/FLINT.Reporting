-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE quantity_observation (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    observation_type_id INTEGER NOT NULL,
    task_id INTEGER NOT NULL,
    party_id INTEGER NOT NULL,
    database_id INTEGER NOT NULL,
    land_use_category_id INTEGER NOT NULL,
    reporting_table_id INTEGER NOT NULL,
    reporting_variable_id INTEGER NOT NULL,
    year  INTEGER NOT NULL,
    amount  DOUBLE PRECISION NOT NULL,
    unit_id  INTEGER NOT NULL,
    version INTEGER NOT NULL);

-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION QuantityObservationVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER QuantityObservationVersionInitializationTrigger7
BEFORE INSERT
ON quantity_observation
FOR EACH ROW
EXECUTE PROCEDURE QuantityObservationVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION QuantityObservationVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER QuantityObservationVersionIncrementTrigger
BEFORE UPDATE
ON quantity_observation
FOR EACH ROW
EXECUTE PROCEDURE QuantityObservationVersionIncrementFunction();