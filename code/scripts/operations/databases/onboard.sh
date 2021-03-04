#!/bin/sh
# See: https://stackoverflow.com/questions/6405127/how-do-i-specify-a-password-to-psql-non-interactively#6405296
# See: https://stackoverflow.com/questions/14549270/check-if-database-exists-in-postgresql-using-shell

echo
echo "---------------------------------------------------------------------------------"
echo "Entering Databases Onboarding Script"
echo "---------------------------------------------------------------------------------"
echo

echo
echo "Setting up Resource Paths"
echo

# root/code/scripts/operations/databases
DATABASES_DIR="$(cd "$(dirname "$0")" && pwd)"

# root/code/scripts/operations
OPERATIONS_DIR="$(dirname "$DATABASES_DIR")"

# root/code/scripts
SCRIPTS_DIR="$(dirname "$OPERATIONS_DIR")"

# root/code
PROJECT_DIR="$(dirname "$SCRIPTS_DIR")"


# -------------------------------------------------------------------------------------
# Get the name of the database that is being onboarded
# -------------------------------------------------------------------------------------

echo
echo "Enter the database name"
echo "---------------------------------------------------------------------------------"
read DATABASE


# -------------------------------------------------------------------------------------
# Check if the database's backup file exists locally; quit if it doesn't
# -------------------------------------------------------------------------------------

if [ ! -f $PROJECT_DIR/data/$DATABASE.backup]; then
    echo
    echo "$DATABASE's backup does not exist"
    echo "Terminating $DATABASE's onboarding"
    exit
fi


# -------------------------------------------------------------------------------------
# Set the connection details of the server to which the database that is being onboarded
# -------------------------------------------------------------------------------------
echo
echo "Setting database server connection details"
echo


LOCAL_SERVER=0
REMOTE_SERVER=1


if [ $LOCAL_SERVER -eq 1 ]; then

  echo
  echo "Setting locally hosted database server details"
  echo

  export PGHOST="localhost"
  export PGPORT="5432"
  export PGUSER="postgres"
  export PGPASSWORD="postgres"

fi

if [ $REMOTE_SERVER -eq 1 ]; then

  echo
  echo "Setting remotely hosted database server details"
  echo

  export PGHOST="reporter.miles.co.ke"
  export PGPORT="31392"
  export PGUSER="postgres"
  export PGPASSWORD="postgres"

fi



# -------------------------------------------------------------------------------------
# Specify the tables that should be onboarded
# -------------------------------------------------------------------------------------

# 1 = on, 0 = off


echo
echo "Specifying the tables that should be onboarded"
echo


COUNTY_INFO_DIMENSION=0
DATE_DIMENSION=1
FLUX_REPORTING_RESULTS_WITH_LOCAL_DOMAIN=1
FLUX_REPORTING_RESULTS=0
FLUX_TYPE_INFO_DIMENSION=0
IPCC_COVER_TYPE_INFO_DIMENSION=0
LOCATION_DIMENSION=1
POOL_INFO_DIMENSION=0
TILE_INFO_DIMENSION=0
VEGETATION_HISTORY_INFO_DIMENSION=1
VEGETATION_HISTORY_VEGETATION_TYPE_INFO_DIMENSION=1
VEGETATION_TYPE_INFO_DIMENSION=1




# -------------------------------------------------------------------------------------
# Create the target database
# -------------------------------------------------------------------------------------

echo
echo "Creating $DATABASE database"
echo "---------------------------------------------------------------------------------"
psql -c "CREATE DATABASE $DATABASE"


# -------------------------------------------------------------------------------------
# Onboard the tables into the database
# -------------------------------------------------------------------------------------


# county info dimension
# -------------------------------------------------------------------------------------

if [ $COUNTY_INFO_DIMENSION -eq 1 ]; then

echo
echo "Loading $DATABASE's countyinfo_dimension table / data"
echo "---------------------------------------------------------------------------------"

pg_restore --verbose --schema-only  -d "$DATABASE" -t "countyinfo_dimension" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE countyinfo_dimension SET (autovacuum_enabled = false)"

pg_restore --verbose --data-only  -d "$DATABASE" -t "countyinfo_dimension" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE countyinfo_dimension SET (autovacuum_enabled = true)"

fi


# date dimension
# -------------------------------------------------------------------------------------

if [ $DATE_DIMENSION -eq 1 ]; then

echo
echo "Loading $DATABASE's date_dimension table / data"
echo "---------------------------------------------------------------------------------"

pg_restore --verbose --schema-only  -d "$DATABASE" -t "date_dimension" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE date_dimension SET (autovacuum_enabled = false)"

pg_restore --verbose --data-only  -d "$DATABASE" -t "date_dimension" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE date_dimension SET (autovacuum_enabled = true)"

fi


# flux type info dimension
# -------------------------------------------------------------------------------------

if [ $FLUX_TYPE_INFO_DIMENSION -eq 1 ]; then

echo
echo "Loading $DATABASE's fluxtypeinfo_dimension table / data"
echo "---------------------------------------------------------------------------------"

pg_restore --verbose --schema-only  -d "$DATABASE" -t "fluxtypeinfo_dimension" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE fluxtypeinfo_dimension SET (autovacuum_enabled = false)"

pg_restore --verbose --data-only  -d "$DATABASE" -t "fluxtypeinfo_dimension" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE fluxtypeinfo_dimension SET (autovacuum_enabled = true)"

fi


# ipcc cover type info dimension
# -------------------------------------------------------------------------------------

if [ $IPCC_COVER_TYPE_INFO_DIMENSION -eq 1 ]; then

echo
echo "Loading $DATABASE's ipcccovertypeinfo_dimension table / data"
echo "---------------------------------------------------------------------------------"

pg_restore --verbose --schema-only  -d "$DATABASE" -t "ipcccovertypeinfo_dimension" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE ipcccovertypeinfo_dimension SET (autovacuum_enabled = false)"

pg_restore --verbose --data-only  -d "$DATABASE" -t "ipcccovertypeinfo_dimension" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE ipcccovertypeinfo_dimension SET (autovacuum_enabled = true)"

fi


# location_dimension
# -------------------------------------------------------------------------------------

if [ $LOCATION_DIMENSION -eq 1 ]; then

echo
echo "Loading $DATABASE's location_dimension table / data"
echo "---------------------------------------------------------------------------------"

pg_restore --verbose --schema-only  -d "$DATABASE" -t "location_dimension" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE location_dimension SET (autovacuum_enabled = false)"

pg_restore --verbose --data-only  -d "$DATABASE" -t "location_dimension" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE location_dimension SET (autovacuum_enabled = true)"

fi


# pool info dimension
# -------------------------------------------------------------------------------------

if [ $POOL_INFO_DIMENSION -eq 1 ]; then

echo
echo "Loading $DATABASE's poolinfo_dimension table / data"
echo "---------------------------------------------------------------------------------"

pg_restore --verbose --schema-only  -d "$DATABASE" -t "poolinfo_dimension" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE poolinfo_dimension SET (autovacuum_enabled = false)"

pg_restore --verbose --data-only  -d "$DATABASE" -t "poolinfo_dimension" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE poolinfo_dimension SET (autovacuum_enabled = true)"

fi


# tile info dimension
# -------------------------------------------------------------------------------------

if [ $TILE_INFO_DIMENSION -eq 1 ]; then

echo
echo "Loading $DATABASE's tileinfo_dimension table / data"
echo "---------------------------------------------------------------------------------"
pg_restore --verbose --schema-only  -d "$DATABASE" -t "tileinfo_dimension" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE tileinfo_dimension SET (autovacuum_enabled = false)"

pg_restore --verbose --data-only  -d "$DATABASE" -t "tileinfo_dimension" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE tileinfo_dimension SET (autovacuum_enabled = true)"

fi


# veg history dimension
# -------------------------------------------------------------------------------------

if [ $VEGETATION_HISTORY_INFO_DIMENSION -eq 1 ]; then

echo
echo "Loading $DATABASE's veghistory_dimension table / data"
echo "---------------------------------------------------------------------------------"

pg_restore --verbose --schema-only  -d "$DATABASE" -t "veghistory_dimension" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE veghistory_dimension SET (autovacuum_enabled = false)"

pg_restore --verbose --data-only  -d "$DATABASE" -t "veghistory_dimension" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE veghistory_dimension SET (autovacuum_enabled = true)"

fi


# veg type info_dimension
# -------------------------------------------------------------------------------------

if [ $VEGETATION_TYPE_INFO_DIMENSION -eq 1 ]; then

echo
echo "Loading $DATABASE's vegtypeinfo_dimension table / data"
echo "---------------------------------------------------------------------------------"

pg_restore --verbose --schema-only  -d "$DATABASE" -t "vegtypeinfo_dimension" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE vegtypeinfo_dimension SET (autovacuum_enabled = false)"

pg_restore --verbose --data-only  -d "$DATABASE" -t "vegtypeinfo_dimension" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE vegtypeinfo_dimension SET (autovacuum_enabled = true)"

fi


# veg history veg type info mapping
# -------------------------------------------------------------------------------------

if [ $VEGETATION_HISTORY_VEGETATION_TYPE_INFO_DIMENSION -eq 1 ]; then

echo
echo "Loading $DATABASE's veghistory_vegtypeinfo_mapping table / data"
echo "---------------------------------------------------------------------------------"

pg_restore --verbose --schema-only  -d "$DATABASE" -t "veghistory_vegtypeinfo_mapping" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE veghistory_vegtypeinfo_mapping SET (autovacuum_enabled = false)"

pg_restore --verbose --data-only  -d "$DATABASE" -t "veghistory_vegtypeinfo_mapping" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE veghistory_vegtypeinfo_mapping SET (autovacuum_enabled = true)"

fi



# flux reporting results with local domain
# -------------------------------------------------------------------------------------

if [ $FLUX_REPORTING_RESULTS_WITH_LOCAL_DOMAIN -eq 1 ]; then

echo
echo "Loading $DATABASE's flux_reporting_results_withlocaldomain table / data"
echo "---------------------------------------------------------------------------------"

pg_restore --verbose --schema-only  -d "$DATABASE" -t "flux_reporting_results_withlocaldomain" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE flux_reporting_results_withlocaldomain SET (autovacuum_enabled = false)"

psql -d "$DATABASE" -c "set statement_timeout = 600000;"
pg_restore -v -x -a -O -1 -d "$DATABASE" -t "flux_reporting_results_withlocaldomain" "$PROJECT_DIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE flux_reporting_results_withlocaldomain SET (autovacuum_enabled = true)"


fi


# flux reporting results
# -------------------------------------------------------------------------------------

if [ $FLUX_REPORTING_RESULTS -eq 1 ]; then

echo
echo "Loading $DATABASE's flux_reporting_results table / data"
echo "---------------------------------------------------------------------------------"

pg_restore --verbose --schema-only  -d "$DATABASE" -t "flux_reporting_results" "$PARENTDIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE flux_reporting_results SET (autovacuum_enabled = false)"

psql -d "$DATABASE" -c "set statement_timeout = 600000;"
pg_restore -v -x -a -O -1 -d "$DATABASE" -t "flux_reporting_results" "$PARENTDIR/data/$DATABASE.backup"
psql -d "$DATABASE" -c "ALTER TABLE flux_reporting_results SET (autovacuum_enabled = true)"

psql -d "$DATABASE" -c "ALTER TABLE flux_reporting_results RENAME TO flux_reporting_results_withlocaldomain"

fi


echo
echo "Creating a materialized view of the flux reporting results"
echo "---------------------------------------------------------------------------------"
psql -d "$DATABASE" -1 -f "$DATABASES_DIR/materialize_flux_reporting_results.sql"



echo
echo "---------------------------------------------------------------------------------"
echo "Leaving Databases Onboarding Script"
echo "---------------------------------------------------------------------------------"
echo
