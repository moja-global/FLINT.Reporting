#!/bin/sh
# See: https://stackoverflow.com/questions/6405127/how-do-i-specify-a-password-to-psql-non-interactively#6405296
# See: https://stackoverflow.com/questions/14549270/check-if-database-exists-in-postgresql-using-shell

echo
echo "---------------------------------------------------------------------------------"
echo "Entering Databases Initialization Script"
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
EMISSION_TYPES=1
FLUX_TYPES=1
UNFCCC_VARIABLES=1



# Database Collections



echo
echo "Initializing specific databases"
echo "---------------------------------------------------------------------------------"
echo


# -------------------------------------------------------------------------------------
# UTIL
# -------------------------------------------------------------------------------------


# Units
# -------------------------------------------------------------------------------------
if [ $UNITS -eq 1 ]; then

  echo
  echo "Setting up Units Database"
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




# -------------------------------------------------------------------------------------
# FLINT OUTPUT
# -------------------------------------------------------------------------------------


# Flux Types
# -------------------------------------------------------------------------------------
if [ $FLUX_TYPES -eq 1 ]; then

  echo
  echo "Setting up Flux Types Database"
  echo

  # drop the flux types database if it exists
  psql -c "DROP DATABASE IF EXISTS flux_types"

  # create a new flux types database
  psql -c "CREATE DATABASE flux_types"

  # Create the flux type's database objects
  psql -d "flux_types" -1 -f "$PROJECT_DIR/services/flux-types/src/main/resources/flux_types.sql"

  # Load the flux type's database data
  psql -d "flux_types" -1 -c "\copy flux_type(name,description,version) from \
          '$PROJECT_DIR/data/flux_types.csv' DELIMITER ',' CSV HEADER"

fi



# -------------------------------------------------------------------------------------
# REPORTING
# -------------------------------------------------------------------------------------


# Emission Types
# -------------------------------------------------------------------------------------
if [ $EMISSION_TYPES -eq 1 ]; then

  echo
  echo "Setting up Emission Types Database"
  echo

  # drop the emission types database if it exists
  psql -c "DROP DATABASE IF EXISTS emission_types"

  # create a new emission types database
  psql -c "CREATE DATABASE emission_types"

  # Create the emission type's database objects
  psql -d "emission_types" -1 -f "$PROJECT_DIR/services/emission-types/src/main/resources/emission_types.sql"

  # Load the emission type's database data
  psql -d "emission_types" -1 -c "\copy emission_type(name,abbreviation,description,version) from \
          '$PROJECT_DIR/data/emission_types.csv' DELIMITER ',' CSV HEADER"

fi


# UNFCCC Variables
# -------------------------------------------------------------------------------------
if [ $UNFCCC_VARIABLES -eq 1 ]; then

  echo
  echo "Setting up UNFCCC Variables Database"
  echo

  # drop the unfccc variables database if it exists
  psql -c "DROP DATABASE IF EXISTS unfccc_variables"

  # create a new unfccc variables database
  psql -c "CREATE DATABASE unfccc_variables"

  # Create the unfccc variable's database objects
  psql -d "unfccc_variables" -1 -f "$PROJECT_DIR/services/unfccc-variables/src/main/resources/unfccc_variables.sql"

  # Load the unfccc variable's database data
  psql -d "unfccc_variables" -1 -c "\copy unfccc_variable(name, measure, abbreviation, unit_id, version) from \
          '$PROJECT_DIR/data/unfccc_variables.csv' DELIMITER ',' CSV HEADER"

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
echo "Leaving Databases Initialization Script"
echo "---------------------------------------------------------------------------------"
echo
