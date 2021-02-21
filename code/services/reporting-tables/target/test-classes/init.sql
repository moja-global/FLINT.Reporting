-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE reporting_table (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    number VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
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

INSERT INTO reporting_table(number,name,description) VALUES('Table 4','Table 4 Name','Table 4 Description');
INSERT INTO reporting_table(number,name,description) VALUES('Table 4.1','Table 4.1 Name','Table 4.1 Description');
INSERT INTO reporting_table(number,name,description) VALUES('Table 4.A','Table 4.A Name','Table 4.A Description');
