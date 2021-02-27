-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE flux_type (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    name VARCHAR(250) NOT NULL,
    description VARCHAR NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION FluxTypeVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER FluxTypeVersionInitializationTrigger
BEFORE INSERT
ON flux_type
FOR EACH ROW
EXECUTE PROCEDURE FluxTypeVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION FluxTypeVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER FluxTypeVersionIncrementTrigger
BEFORE UPDATE
ON flux_type
FOR EACH ROW
EXECUTE PROCEDURE FluxTypeVersionIncrementFunction();


-- ------------------------------------------------------------
-- Add Some dummy data
-- ------------------------------------------------------------

INSERT INTO flux_type(name,description) VALUES('First FluxType', null);
INSERT INTO flux_type(name,description) VALUES('Second FluxType',null);
INSERT INTO flux_type(name,description) VALUES('Third FluxType', 'Third FluxType Description');
