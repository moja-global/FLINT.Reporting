-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE flux_to_unfccc_variable (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    start_pool_id INTEGER NOT NULL, 
    end_pool_id INTEGER NOT NULL, 
    unfccc_variable_id INTEGER NOT NULL,
    rule VARCHAR(50) NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION FluxToUnfcccVariableVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER FluxToUnfcccVariableVersionInitializationTrigger
BEFORE INSERT
ON flux_to_unfccc_variable
FOR EACH ROW
EXECUTE PROCEDURE FluxToUnfcccVariableVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION FluxToUnfcccVariableVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER FluxToUnfcccVariableVersionIncrementTrigger
BEFORE UPDATE
ON flux_to_unfccc_variable
FOR EACH ROW
EXECUTE PROCEDURE FluxToUnfcccVariableVersionIncrementFunction();