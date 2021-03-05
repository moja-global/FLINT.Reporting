-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE quantity_observation (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    task_id INTEGER NOT NULL,
    party_id INTEGER NOT NULL,
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

CREATE TRIGGER QuantityObservationVersionInitializationTrigger
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


-- ------------------------------------------------------------
-- Add Some dummy data
-- ------------------------------------------------------------

INSERT INTO quantity_observation(task_id,party_id,reporting_variable_id,year,amount,unit_id)
VALUES(1,1,1,1991,1.0,1);

INSERT INTO quantity_observation(task_id,party_id,reporting_variable_id,year,amount,unit_id)
VALUES(2,2,2,1992,2.0,2);

INSERT INTO quantity_observation(task_id,party_id,reporting_variable_id,year,amount,unit_id)
VALUES(3,3,3,1993,3.0,3);
