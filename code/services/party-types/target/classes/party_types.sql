-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE party_type (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    parent_party_type_id INTEGER NOT NULL,
    name VARCHAR(250) NOT NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION PartyTypeVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER PartyTypeVersionInitializationTrigger7
BEFORE INSERT
ON party_type
FOR EACH ROW
EXECUTE PROCEDURE PartyTypeVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION PartyTypeVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER PartyTypeVersionIncrementTrigger
BEFORE UPDATE
ON party_type
FOR EACH ROW
EXECUTE PROCEDURE PartyTypeVersionIncrementFunction();