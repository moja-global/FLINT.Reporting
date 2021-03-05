-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE party (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    party_type_id INTEGER NOT NULL,
    name VARCHAR(250) NOT NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION PartyVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER PartyVersionInitializationTrigger7
BEFORE INSERT
ON party
FOR EACH ROW
EXECUTE PROCEDURE PartyVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION PartyVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER PartyVersionIncrementTrigger
BEFORE UPDATE
ON party
FOR EACH ROW
EXECUTE PROCEDURE PartyVersionIncrementFunction();


-- ------------------------------------------------------------
-- Set the id to start at 0
-- ------------------------------------------------------------

ALTER SEQUENCE party_id_seq
MINVALUE 0
START 0
RESTART 0;