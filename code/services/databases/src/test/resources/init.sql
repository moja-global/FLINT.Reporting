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
    default_accountability_type_id INTEGER NULL,
    default_party_type_id INTEGER NULL,
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

CREATE TRIGGER DatabaseVersionInitializationTrigger
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


-- ------------------------------------------------------------
-- Add Some dummy data
-- ------------------------------------------------------------

INSERT INTO database(label,description,url,start_year,end_year,processed,published,archived)
       VALUES(
                'First Database',
                'First Database Description',
                'jdbc:postgresql://localhost:5432/database1',
                1981,
                2001,
                FALSE,
                FALSE,
                FALSE);

INSERT INTO database(label,description,url,start_year,end_year,processed,published,archived)
       VALUES(
                'Second Database',
                'Second Database Description',
                'jdbc:postgresql://localhost:5432/database2',
                1982,
                2002,
                TRUE,
                FALSE,
                FALSE);

INSERT INTO database(label,description,url,start_year,end_year,processed,published,archived)
       VALUES(
                'Third Database',
                'Third Database Description',
                'jdbc:postgresql://localhost:5432/database3',
                1983,
                2003,
                TRUE,
                TRUE,
                FALSE);
