-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE pool (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    name VARCHAR(250) NOT NULL,
    description VARCHAR NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION PoolVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER PoolVersionInitializationTrigger7
BEFORE INSERT
ON pool
FOR EACH ROW
EXECUTE PROCEDURE PoolVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION PoolVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER PoolVersionIncrementTrigger
BEFORE UPDATE
ON pool
FOR EACH ROW
EXECUTE PROCEDURE PoolVersionIncrementFunction();


-- ------------------------------------------------------------
-- Set the id to start at 0
-- ------------------------------------------------------------

ALTER SEQUENCE pool_id_seq
MINVALUE 0
START 0
RESTART 0;