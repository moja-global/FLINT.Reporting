-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE database (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    label VARCHAR(250) NOT NULL,
    description VARCHAR NULL,
    url VARCHAR NOT NULL,
    start_year INTEGER NOT NULL,
    end_year INTEGER NOT NULL,
    processed BOOLEAN NOT NULL,
    published BOOLEAN NOT NULL,
    archived BOOLEAN NOT NULL,
    default_accountability_type_id INTEGER NOT NULL,
    default_party_type_id INTEGER NOT NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION DatabaseVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER DatabaseVersionInitializationTrigger7
BEFORE INSERT
ON database
FOR EACH ROW
EXECUTE PROCEDURE DatabaseVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION DatabaseVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER DatabaseVersionIncrementTrigger
BEFORE UPDATE
ON database
FOR EACH ROW
EXECUTE PROCEDURE DatabaseVersionIncrementFunction();