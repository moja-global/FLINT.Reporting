-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE cover_type (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    code VARCHAR(15) NOT NULL,
    description VARCHAR(250) NOT NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION CoverTypeVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER CoverTypeVersionInitializationTrigger7
BEFORE INSERT
ON cover_type
FOR EACH ROW
EXECUTE PROCEDURE CoverTypeVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION CoverTypeVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER CoverTypeVersionIncrementTrigger
BEFORE UPDATE
ON cover_type
FOR EACH ROW
EXECUTE PROCEDURE CoverTypeVersionIncrementFunction();