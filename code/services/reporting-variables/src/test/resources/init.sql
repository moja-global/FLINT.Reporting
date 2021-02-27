-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE reporting_variable (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    reporting_framework_id INTEGER NOT NULL,
    name VARCHAR(250) NOT NULL,
    description VARCHAR NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION ReportingVariableVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER ReportingVariableVersionInitializationTrigger
BEFORE INSERT
ON reporting_variable
FOR EACH ROW
EXECUTE PROCEDURE ReportingVariableVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION ReportingVariableVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER ReportingVariableVersionIncrementTrigger
BEFORE UPDATE
ON reporting_variable
FOR EACH ROW
EXECUTE PROCEDURE ReportingVariableVersionIncrementFunction();


-- ------------------------------------------------------------
-- Add Some dummy data
-- ------------------------------------------------------------

INSERT INTO reporting_variable(reporting_framework_id,name,description) VALUES(2,'First Variable', 'First Variable Description');
INSERT INTO reporting_variable(reporting_framework_id,name,description) VALUES(1,'Second Variable', 'Second Variable Description');
INSERT INTO reporting_variable(reporting_framework_id,name,description) VALUES(1,'Third Variable', 'Third Variable Description');
