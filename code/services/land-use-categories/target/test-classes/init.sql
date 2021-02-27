-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE land_use_category (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    reporting_framework_id INTEGER NOT NULL,
    parent_land_use_category_id INTEGER NULL,
    cover_type_id INTEGER NOT NULL,
    name VARCHAR(250) NOT NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION LandUseCategoryVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER LandUseCategoryVersionInitializationTrigger
BEFORE INSERT
ON land_use_category
FOR EACH ROW
EXECUTE PROCEDURE LandUseCategoryVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION LandUseCategoryVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER LandUseCategoryVersionIncrementTrigger
BEFORE UPDATE
ON land_use_category
FOR EACH ROW
EXECUTE PROCEDURE LandUseCategoryVersionIncrementFunction();


-- ------------------------------------------------------------
-- Add Some dummy data
-- ------------------------------------------------------------

INSERT INTO land_use_category(reporting_framework_id,cover_type_id,name) VALUES(1,1,'First Land Use Category');
INSERT INTO land_use_category(reporting_framework_id,parent_land_use_category_id,cover_type_id,name) VALUES(1,1,1,'Second Land Use Category');
INSERT INTO land_use_category(reporting_framework_id,parent_land_use_category_id,cover_type_id,name) VALUES(1,1,1,'Third Land Use Category');
