-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE vegetation_type (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    cover_type_id INTEGER NOT NULL,
    name VARCHAR(250) NOT NULL,
    woody_type BOOLEAN NOT NULL,
    natural_system BOOLEAN NOT NULL,
    version INTEGER NOT NULL);





-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION VegetationTypeVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER VegetationTypeVersionInitializationTrigger
BEFORE INSERT
ON vegetation_type
FOR EACH ROW
EXECUTE PROCEDURE VegetationTypeVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION VegetationTypeVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER VegetationTypeVersionIncrementTrigger
BEFORE UPDATE
ON vegetation_type
FOR EACH ROW
EXECUTE PROCEDURE VegetationTypeVersionIncrementFunction();


-- ------------------------------------------------------------
-- Add Some dummy data
-- ------------------------------------------------------------

INSERT INTO vegetation_type(cover_type_id,name,woody_type,natural_system) VALUES(1,'First Vegetation Type', FALSE,FALSE);
INSERT INTO vegetation_type(cover_type_id,name,woody_type,natural_system) VALUES(2,'Second Vegetation Type', TRUE,FALSE);
INSERT INTO vegetation_type(cover_type_id,name,woody_type,natural_system) VALUES(3,'Third Vegetation Type', TRUE,TRUE);
