-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE reporting_framework (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    name VARCHAR(250) NOT NULL,
    description VARCHAR NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION ReportingFrameworkVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER ReportingFrameworkVersionInitializationTrigger
BEFORE INSERT
ON reporting_framework
FOR EACH ROW
EXECUTE PROCEDURE ReportingFrameworkVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION ReportingFrameworkVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER ReportingFrameworkVersionIncrementTrigger
BEFORE UPDATE
ON reporting_framework
FOR EACH ROW
EXECUTE PROCEDURE ReportingFrameworkVersionIncrementFunction();


-- ------------------------------------------------------------
-- Add Some dummy data
-- ------------------------------------------------------------

INSERT INTO reporting_framework(name,description) VALUES('UNFCCC', 'UNFCCC Description');
INSERT INTO reporting_framework(name,description) VALUES('Kyoto Protocol', 'Kyoto Protocol Description');
INSERT INTO reporting_framework(name,description) VALUES('REDD+', 'REDD+ Description');
