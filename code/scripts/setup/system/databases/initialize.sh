#!/bin/sh
# See: https://stackoverflow.com/questions/6405127/how-do-i-specify-a-password-to-psql-non-interactively#6405296
# See: https://stackoverflow.com/questions/14549270/check-if-database-exists-in-postgresql-using-shell

echo
echo "---------------------------------------------------------------------------------"
echo "Entering databases Initialization Script"
echo "---------------------------------------------------------------------------------"
echo

echo
echo "Setting up Resource Paths"
echo

# root/project/scripts/setup/system/databases
DATABASES_DIR="$(cd "$(dirname "$0")" && pwd)"

# root/project/scripts/setup/system
SYSTEM_DIR="$(dirname "$DATABASES_DIR")"

# root/project/scripts/setup
SETUP_DIR="$(dirname "$SYSTEM_DIR")"

# root/project/scripts
SCRIPTS_DIR="$(dirname "$SETUP_DIR")"

# root/project/
PROJECT_DIR="$(dirname "$SCRIPTS_DIR")"


# Connection Details
# ----------------------------------------------------------------------------------

DEVELOPMENT_SERVER=0
CLOUD_SERVER=1

if [ $DEVELOPMENT_SERVER -eq 1 ]; then

  echo
  echo "Setting up local host connection details"
  echo

  export PGHOST="localhost"
  export PGPORT="5432"
  export PGUSER="postgres"
  export PGPASSWORD="postgres"

fi

if [ $CLOUD_SERVER -eq 1 ]; then

  echo
  echo "Setting up cloud host connection details"
  echo

  export PGHOST="localhost"
  export PGPORT="31392"
  export PGUSER="postgres"
  export PGPASSWORD="postgres"

fi

# Use the flags 1 and 0 below to configure the databases that you want to initialize
# 1 = on, 0 = off
# ----------------------------------------------------------------------------------

# Single databases
ACCOUNTABILITIES=1
ACCOUNTABILITY_RULES=1
ACCOUNTABILITY_TYPES=1
CONVERSION_AND_REMAINING_PERIODS=1
COVER_TYPES=1
DATABASES=1
EMISSION_TYPES=1
FLUXES_TO_REPORTING_VARIABLES=1
FLUX_TYPES=1
LAND_USE_CATEGORIES=1
LAND_USES_FLUX_TYPES=1
LAND_USES_FLUX_TYPES_TO_REPORTING_TABLES=1
PARTIES=1
PARTY_TYPES=1
POOLS=1
QUANTITY_OBSERVATIONS=1
REPORTING_FRAMEWORKS=1
REPORTING_TABLES=1
REPORTING_VARIABLES=1
TASKS=1
UNIT_CATEGORIES=1
UNITS=1



# database Collections



echo
echo "Initializing specific databases"
echo "---------------------------------------------------------------------------------"
echo


# accountabilities
# -------------------------------------------------------------------------------------
if [ $ACCOUNTABILITIES -eq 1 ]; then

  echo
  echo "Setting up accountabilities database"
  echo

  # drop the accountabilities database if it exists
  psql -c "DROP DATABASE IF EXISTS accountabilities"

  # create a new accountabilities database
  psql -c "CREATE DATABASE accountabilities"

  # Create the accountability's database objects
  psql -d "accountabilities" -1 -f "$PROJECT_DIR/services/accountabilities/src/main/resources/accountabilities.sql"

  # Load the accountability's database data
  psql -d "accountabilities" -1 -c "\copy accountability(accountability_type_id,accountability_rule_id,parent_party_id,subsidiary_party_id,version) from \
          '$PROJECT_DIR/data/accountabilities.csv' DELIMITER ',' CSV HEADER"

fi


# accountability rules
# -------------------------------------------------------------------------------------
if [ $ACCOUNTABILITY_RULES -eq 1 ]; then

  echo
  echo "Setting up accountability rules database"
  echo

  # drop the accountability rules database if it exists
  psql -c "DROP DATABASE IF EXISTS accountability_rules"

  # create a new accountability rules database
  psql -c "CREATE DATABASE accountability_rules"

  # Create the accountability rules database objects
  psql -d "accountability_rules" -1 -f "$PROJECT_DIR/services/accountability-rules/src/main/resources/accountability_rules.sql"

  # Load the accountability rules database data
  psql -d "accountability_rules" -1 -c "\copy accountability_rule(accountability_type_id,parent_party_type_id,subsidiary_party_type_id, version) from \
          '$PROJECT_DIR/data/accountability_rules.csv' DELIMITER ',' CSV HEADER"

fi


# accountability types
# -------------------------------------------------------------------------------------
if [ $ACCOUNTABILITY_TYPES -eq 1 ]; then

  echo
  echo "Setting up accountability types database"
  echo

  # drop the accountability types database if it exists
  psql -c "DROP DATABASE IF EXISTS accountability_types"

  # create a new accountability types database
  psql -c "CREATE DATABASE accountability_types"

  # Create the accountability types database objects
  psql -d "accountability_types" -1 -f "$PROJECT_DIR/services/accountability-types/src/main/resources/accountability_types.sql"

  # Load the accountability types database data
  psql -d "accountability_types" -1 -c "\copy accountability_type(name, version) from \
          '$PROJECT_DIR/data/accountability_types.csv' DELIMITER ',' CSV HEADER"

fi


# conversion and remaining periods
# -------------------------------------------------------------------------------------
if [ $CONVERSION_AND_REMAINING_PERIODS -eq 1 ]; then

  echo
  echo "Setting up conversion and remaining periods database"
  echo

  # drop the conversion and remaining periods database if it exists
  psql -c "DROP DATABASE IF EXISTS conversion_and_remaining_periods"

  # create a new conversion and remaining periods database
  psql -c "CREATE DATABASE conversion_and_remaining_periods"

  # Create the conversion and remaining periods database objects
  psql -d "conversion_and_remaining_periods" -1 -f "$PROJECT_DIR/services/conversion-and-remaining-periods/src/main/resources/conversion_and_remaining_periods.sql"

  # Load the conversion and remaining periods database data
  psql -d "conversion_and_remaining_periods" -1 -c "\copy conversion_and_remaining_period(previous_land_cover_id,current_land_cover_id,conversion_period,remaining_period, version) from \
          '$PROJECT_DIR/data/conversion_and_remaining_periods.csv' DELIMITER ',' CSV HEADER"

fi


# cover types
# -------------------------------------------------------------------------------------
if [ $COVER_TYPES -eq 1 ]; then

  echo
  echo "Setting up cover types database"
  echo

  # drop the cover types database if it exists
  psql -c "DROP DATABASE IF EXISTS cover_types"

  # create a new cover types database
  psql -c "CREATE DATABASE cover_types"

  # Create the cover types database objects
  psql -d "cover_types" -1 -f "$PROJECT_DIR/services/cover-types/src/main/resources/cover_types.sql"

  # Load the cover types database data
  psql -d "cover_types" -1 -c "\copy cover_type(code, description, version) from \
          '$PROJECT_DIR/data/cover_types.csv' DELIMITER ',' CSV HEADER"

fi


# databases
# -------------------------------------------------------------------------------------
if [ $DATABASES -eq 1 ]; then

  echo
  echo "Setting up databases database"
  echo

  # drop the databases database if it exists
  psql -c "DROP DATABASE IF EXISTS databases"

  # create a new databases database
  psql -c "CREATE DATABASE databases"

  # Create the database's database objects
  psql -d "databases" -1 -f "$PROJECT_DIR/services/databases/src/main/resources/databases.sql"

  # Load the database's database data
  psql -d "databases" -1 -c "\copy database(label,description,url,start_year,end_year,processed,published,archived,version) from \
          '$PROJECT_DIR/data/databases.csv' DELIMITER ',' CSV HEADER"

fi


# emission types
# -------------------------------------------------------------------------------------
if [ $EMISSION_TYPES -eq 1 ]; then

  echo
  echo "Setting up emission types database"
  echo

  # drop the emission types database if it exists
  psql -c "DROP DATABASE IF EXISTS emission_types"

  # create a new emission types database
  psql -c "CREATE DATABASE emission_types"

  # Create the emission types database objects
  psql -d "emission_types" -1 -f "$PROJECT_DIR/services/emission-types/src/main/resources/emission_types.sql"

  # Load the emission types database data
  psql -d "emission_types" -1 -c "\copy emission_type(name, abbreviation, description, version) from \
          '$PROJECT_DIR/data/emission_types.csv' DELIMITER ',' CSV HEADER"

fi


# fluxes to reporting variables
# ----------------------------------------------------------------------------------
if [ $FLUXES_TO_REPORTING_VARIABLES -eq 1 ]; then

  echo
  echo "Setting up fluxes to reporting variables database"
  echo

  # drop the fluxes to reporting variables database if it exists
  psql -c "DROP DATABASE IF EXISTS fluxes_to_reporting_variables"

  # create a new fluxes to reporting variables database
  psql -c "CREATE DATABASE fluxes_to_reporting_variables"

  # Create the flux to reporting variables database objects
  psql -d "fluxes_to_reporting_variables" -1 -f "$PROJECT_DIR/services/fluxes-to-reporting-variables/src/main/resources/fluxes_to_reporting_variables.sql"

  # Load the fluxes to reporting variables database data

  netCarbonStockChangeInLivingBiomasId=2
  netCarbonStockChangeInDOMId=3
  netCarbonStockChangeInMineralSoilsId=4
  netCarbonStockChangeInOrganicSoilsId=5
  ch4Id=7
  n2oId=8

  IFS=","
  while read f1 f2 f3 f4 f5 f6 f7 f8 f9 f10 f11; do
    

    startPoolId=${f1:=null}
    startPoolName=${f2:=null}
    endPoolId=${f3:=null}
    endPoolName=${f4:=null}
    netCarbonStockChangeInLivingBiomasTreatment=${f5:=null}
    netCarbonStockChangeInDOMTreatment=${f6:=null}
    netCarbonStockChangeInMineralSoilsTreatment=${f7:=null}
    netCarbonStockChangeInOrganicSoilsTreatment=${f8:=null}
    ch4Treatment=${f9:=null}
    n2oTreatment=${f10:=null}
    version=${f11:=null}

    # Create fluxes to reporting variables records
  
    printf '\n%s to %s:\n' "$startPoolName" "$endPoolName"
    psql -d "fluxes_to_reporting_variables" -c "INSERT INTO flux_to_reporting_variable(start_pool_id, end_pool_id, reporting_variable_id, rule, version) VALUES ($startPoolId, $endPoolId, $netCarbonStockChangeInLivingBiomasId, $netCarbonStockChangeInLivingBiomasTreatment, $version),($startPoolId, $endPoolId, $netCarbonStockChangeInDOMId, $netCarbonStockChangeInDOMTreatment, $version),($startPoolId, $endPoolId, $netCarbonStockChangeInMineralSoilsId, $netCarbonStockChangeInMineralSoilsTreatment, $version),($startPoolId, $endPoolId, $netCarbonStockChangeInOrganicSoilsId, $netCarbonStockChangeInOrganicSoilsTreatment, $version),($startPoolId, $endPoolId, $ch4Id, $ch4Treatment, $version),($startPoolId, $endPoolId, $n2oId, $n2oTreatment, $version)"

  done <$PROJECT_DIR/data/fluxes_to_reporting_variables.csv

fi



# flux types
# -------------------------------------------------------------------------------------
if [ $FLUX_TYPES -eq 1 ]; then

  echo
  echo "Setting up flux types database"
  echo

  # drop the flux types database if it exists
  psql -c "DROP DATABASE IF EXISTS flux_types"

  # create a new flux types database
  psql -c "CREATE DATABASE flux_types"

  # Create the flux types database objects
  psql -d "flux_types" -1 -f "$PROJECT_DIR/services/flux-types/src/main/resources/flux_types.sql"

  # Load the flux types database data
  psql -d "flux_types" -1 -c "\copy flux_type(name,description,version) from \
          '$PROJECT_DIR/data/flux_types.csv' DELIMITER ',' CSV HEADER"

fi




# land use categories
# -------------------------------------------------------------------------------------
if [ $LAND_USE_CATEGORIES -eq 1 ]; then

  echo
  echo "Setting up land use categories database"
  echo

  # drop the land use categories database if it exists
  psql -c "DROP DATABASE IF EXISTS land_use_categories"

  # create a new land use categories database
  psql -c "CREATE DATABASE land_use_categories"

  # Create the land use categories database objects
  psql -d "land_use_categories" -1 -f "$PROJECT_DIR/services/land-use-categories/src/main/resources/land_use_categories.sql"

  # Load the land use categories database data
  psql -d "land_use_categories" -1 -c "\copy land_use_category(reporting_framework_id, parent_land_use_category_id, cover_type_id, name, version) from \
          '$PROJECT_DIR/data/land_use_categories.csv' DELIMITER ',' CSV HEADER"

fi


# land uses flux types
# -------------------------------------------------------------------------------------
if [ $LAND_USES_FLUX_TYPES -eq 1 ]; then

  echo
  echo "Setting up land uses flux types database"
  echo

  # drop the land uses flux types database if it exists
  psql -c "DROP DATABASE IF EXISTS land_uses_flux_types"

  # create a new land uses flux types database
  psql -c "CREATE DATABASE land_uses_flux_types"

  # Create the land uses flux types database objects
  psql -d "land_uses_flux_types" -1 -f "$PROJECT_DIR/services/land-uses-flux-types/src/main/resources/land_uses_flux_types.sql"

  # Load the land uses flux types database data
  psql -d "land_uses_flux_types" -1 -c "\copy land_use_flux_type(land_use_category_id, flux_type_id, version) from \
          '$PROJECT_DIR/data/land_uses_flux_types.csv' DELIMITER ',' CSV HEADER"

fi

# land uses flux types to reporting tables
# -------------------------------------------------------------------------------------
if [ $LAND_USES_FLUX_TYPES_TO_REPORTING_TABLES -eq 1 ]; then

  echo
  echo "Setting up land uses flux types to reporting tables database"
  echo

  # drop the land uses flux types to reporting tables database if it exists
  psql -c "DROP DATABASE IF EXISTS land_uses_flux_types_to_reporting_tables"

  # create a new land uses flux types to reporting tables database
  psql -c "CREATE DATABASE land_uses_flux_types_to_reporting_tables"

  # Create the land uses flux types to reporting tables database objects
  psql -d "land_uses_flux_types_to_reporting_tables" -1 -f "$PROJECT_DIR/services/land-uses-flux-types-to-reporting-tables/src/main/resources/land_uses_flux_types_to_reporting_tables.sql"

  # Load the land uses flux types to reporting tables database data
  psql -d "land_uses_flux_types_to_reporting_tables" -1 -c "\copy land_use_flux_type_to_reporting_table(land_use_flux_type_id, emission_type_id, reporting_table_id, version) from \
          '$PROJECT_DIR/data/land_uses_flux_types_to_reporting_tables.csv' DELIMITER ',' CSV HEADER"

fi


# parties
# -------------------------------------------------------------------------------------
if [ $PARTIES -eq 1 ]; then

  echo
  echo "Setting up parties party"
  echo

  # drop the parties party if it exists
  psql -c "DROP DATABASE IF EXISTS parties"

  # create a new parties party
  psql -c "CREATE DATABASE parties"

  # Create the party's party objects
  psql -d "parties" -1 -f "$PROJECT_DIR/services/parties/src/main/resources/parties.sql"

  # Load the party's party data
  psql -d "parties" -1 -c "\copy party(party_type_id,name,version) from \
          '$PROJECT_DIR/data/parties.csv' DELIMITER ',' CSV HEADER"

fi


# party types
# -------------------------------------------------------------------------------------
if [ $PARTY_TYPES -eq 1 ]; then

  echo
  echo "Setting up party types database"
  echo

  # drop the party types database if it exists
  psql -c "DROP DATABASE IF EXISTS party_types"

  # create a new party types database
  psql -c "CREATE DATABASE party_types"

  # Create the party types database objects
  psql -d "party_types" -1 -f "$PROJECT_DIR/services/party-types/src/main/resources/party_types.sql"

  # Load the party types database data
  psql -d "party_types" -1 -c "\copy party_type(parent_party_type_id, name, version) from \
          '$PROJECT_DIR/data/party_types.csv' DELIMITER ',' CSV HEADER"

fi


# pools
# -------------------------------------------------------------------------------------
if [ $POOLS -eq 1 ]; then

  echo
  echo "Setting up pools database"
  echo

  # drop the pools database if it exists
  psql -c "DROP DATABASE IF EXISTS pools"

  # create a new pools database
  psql -c "CREATE DATABASE pools"

  # Create the pool's database objects
  psql -d "pools" -1 -f "$PROJECT_DIR/services/pools/src/main/resources/pools.sql"

  # Load the pool's database data
  psql -d "pools" -1 -c "\copy pool(name,description,version) from \
          '$PROJECT_DIR/data/pools.csv' DELIMITER ',' CSV HEADER"

fi


# quantity observations
# -------------------------------------------------------------------------------------
if [ $QUANTITY_OBSERVATIONS -eq 1 ]; then

  echo
  echo "Setting up quantity observations database"
  echo

  # drop the quantity observations database if it exists
  psql -c "DROP DATABASE IF EXISTS quantity_observations"

  # create a new quantity observations database
  psql -c "CREATE DATABASE quantity_observations"

  # Create the quantity observations database objects
  psql -d "quantity_observations" -1 -f "$PROJECT_DIR/services/quantity-observations/src/main/resources/quantity_observations.sql"


fi


# reporting frameworks
# -------------------------------------------------------------------------------------
if [ $REPORTING_FRAMEWORKS -eq 1 ]; then

  echo
  echo "Setting up reporting frameworks database"
  echo

  # drop the reporting frameworks database if it exists
  psql -c "DROP DATABASE IF EXISTS reporting_frameworks"

  # create a new reporting frameworks database
  psql -c "CREATE DATABASE reporting_frameworks"

  # Create the reporting frameworks database objects
  psql -d "reporting_frameworks" -1 -f "$PROJECT_DIR/services/reporting-frameworks/src/main/resources/reporting_frameworks.sql"

  # Load the reporting frameworks database data
  psql -d "reporting_frameworks" -1 -c "\copy reporting_framework(name,description,version) from \
          '$PROJECT_DIR/data/reporting_frameworks.csv' DELIMITER ',' CSV HEADER"

fi


# reporting tables
# -------------------------------------------------------------------------------------
if [ $REPORTING_TABLES -eq 1 ]; then

  echo
  echo "Setting up reporting tables database"
  echo

  # drop the reporting tables database if it exists
  psql -c "DROP DATABASE IF EXISTS reporting_tables"

  # create a new reporting tables database
  psql -c "CREATE DATABASE reporting_tables"

  # Create the reporting tables database objects
  psql -d "reporting_tables" -1 -f "$PROJECT_DIR/services/reporting-tables/src/main/resources/reporting_tables.sql"

  # Load the reporting tables database data
  psql -d "reporting_tables" -1 -c "\copy reporting_table(reporting_framework_id, number, name, description, version) from \
          '$PROJECT_DIR/data/reporting_tables.csv' DELIMITER ',' CSV HEADER"

fi


# reporting variables
# -------------------------------------------------------------------------------------
if [ $REPORTING_VARIABLES -eq 1 ]; then

  echo
  echo "Setting up reporting variables database"
  echo

  # drop the reporting variables database if it exists
  psql -c "DROP DATABASE IF EXISTS reporting_variables"

  # create a new reporting variables database
  psql -c "CREATE DATABASE reporting_variables"

  # Create the reporting variables database objects
  psql -d "reporting_variables" -1 -f "$PROJECT_DIR/services/reporting-variables/src/main/resources/reporting_variables.sql"

  # Load the reporting variables database data
  psql -d "reporting_variables" -1 -c "\copy reporting_variable(reporting_framework_id, name, description, version) from \
          '$PROJECT_DIR/data/reporting_variables.csv' DELIMITER ',' CSV HEADER"

fi


# tasks
# -------------------------------------------------------------------------------------
if [ $TASKS -eq 1 ]; then

  echo
  echo "Setting up tasks database"
  echo

  # drop the tasks database if it exists
  psql -c "DROP DATABASE IF EXISTS tasks"

  # create a new tasks database
  psql -c "CREATE DATABASE tasks"

  # Create the task's database objects
  psql -d "tasks" -1 -f "$PROJECT_DIR/services/tasks/src/main/resources/tasks.sql"


fi


# unit categories
# -------------------------------------------------------------------------------------
if [ $UNIT_CATEGORIES -eq 1 ]; then

  echo
  echo "Setting up unit categories database"
  echo

  # drop the unit categories database if it exists
  psql -c "DROP DATABASE IF EXISTS unit_categories"

  # create a new unit categories database
  psql -c "CREATE DATABASE unit_categories"

  # Create the unit categories database objects
  psql -d "unit_categories" -1 -f "$PROJECT_DIR/services/unit-categories/src/main/resources/unit_categories.sql"

  # Load the unit categories' database data
  psql -d "unit_categories" -1 -c "\copy unit_category(name,version) from \
          '$PROJECT_DIR/data/unit_categories.csv' DELIMITER ',' CSV HEADER"

fi


# units
# -------------------------------------------------------------------------------------
if [ $UNITS -eq 1 ]; then

  echo
  echo "Setting up units database"
  echo

  # drop the units database if it exists
  psql -c "DROP DATABASE IF EXISTS units"

  # create a new units database
  psql -c "CREATE DATABASE units"

  # Create the unit's database objects
  psql -d "units" -1 -f "$PROJECT_DIR/services/units/src/main/resources/units.sql"

  # Load the unit's database data
  psql -d "units" -1 -c "\copy unit(unit_category_id, name, plural, symbol, scale_factor,version) from \
          '$PROJECT_DIR/data/units.csv' DELIMITER ',' CSV HEADER"

fi



echo
echo "Initializing database collections"
echo "---------------------------------------------------------------------------------"
echo


# ----------------------------------------------------------------------------------
# 
# ----------------------------------------------------------------------------------



echo
echo "---------------------------------------------------------------------------------"
echo "Leaving databases Initialization Script"
echo "---------------------------------------------------------------------------------"
echo
