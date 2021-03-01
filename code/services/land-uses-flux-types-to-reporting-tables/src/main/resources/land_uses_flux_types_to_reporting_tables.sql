-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------

CREATE TABLE land_use_flux_type_to_reporting_table (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    land_use_flux_type_id INTEGER NOT NULL,
    emission_type_id INTEGER NOT NULL,
    reporting_table_id INTEGER NULL,
    version INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Version Initialization Trigger for all insert operations
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION LandUseFluxTypeToReportingTableVersionInitializationFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER LandUseFluxTypeToReportingTableVersionInitializationTrigger7
BEFORE INSERT
ON land_use_flux_type_to_reporting_table
FOR EACH ROW
EXECUTE PROCEDURE LandUseFluxTypeToReportingTableVersionInitializationFunction();


-- ------------------------------------------------------------
-- Add Version Update Trigger for all update operations
-- ------------------------------------------------------------

CREATE OR REPLACE FUNCTION LandUseFluxTypeToReportingTableVersionIncrementFunction()
RETURNS "trigger" AS
$$
BEGIN
  New.version:=Old.version + 1;
  Return NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER LandUseFluxTypeToReportingTableVersionIncrementTrigger
BEFORE UPDATE
ON land_use_flux_type_to_reporting_table
FOR EACH ROW
EXECUTE PROCEDURE LandUseFluxTypeToReportingTableVersionIncrementFunction();