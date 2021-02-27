-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE reporting_table (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    reporting_framework_id INTEGER NOT NULL,
    number VARCHAR(50) NOT NULL,    
    name VARCHAR(250) NOT NULL,
    description VARCHAR NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION ReportingTableVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER ReportingTableVersionInitializationTrigger
BEFORE INSERT
ON reporting_table
FOR EACH ROW
EXECUTE PROCEDURE ReportingTableVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION ReportingTableVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER ReportingTableVersionIncrementTrigger
BEFORE UPDATE
ON reporting_table
FOR EACH ROW
EXECUTE PROCEDURE ReportingTableVersionIncrementFunction();


-- ------------------------------------------------------------
-- Add Some dummy data
-- ------------------------------------------------------------

INSERT INTO reporting_table(reporting_framework_id,number,name,description) VALUES(1,'First','First ReportingTable', null);
INSERT INTO reporting_table(reporting_framework_id,number,name,description) VALUES(2,'Second','Second ReportingTable',null);
INSERT INTO reporting_table(reporting_framework_id,number,name,description) VALUES(3,'Third','Third ReportingTable', 'Third Description');
