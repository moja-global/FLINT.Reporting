#!/bin/sh
# See: https://stackoverflow.com/questions/6405127/how-do-i-specify-a-password-to-psql-non-interactively#6405296
# See: https://stackoverflow.com/questions/14549270/check-if-database-exists-in-postgresql-using-shell

echo
echo "---------------------------------------------------------------------------------"
echo "Entering Operations Databases Restoration Script"
echo "---------------------------------------------------------------------------------"
echo

echo
echo "Setting up Resource Paths"
echo

# root/project/scripts/operations/databases
DATABASES_DIR="$(cd "$(dirname "$0")" && pwd)"

# root/project/scripts/operations
OPERATIONS_DIR="$(dirname "$DATABASES_DIR")"

# root/project/scripts
SCRIPTS_DIR="$(dirname "$OPERATIONS_DIR")"

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


SYSTEM_ADMINISTRATOR=0

echo
echo "Restoring databases"
echo "---------------------------------------------------------------------------------"
echo


  psql -f backup.out postgres

  # Restore the quantity observations sequence
  NEXT_ID=`psql -d "quantity_observations" -t -c "SELECT MAX(id) + 1 FROM quantity_observation"`
  psql -d "quantity_observations" -c "ALTER SEQUENCE quantity_observation_id_seq RESTART WITH $NEXT_ID"



echo
echo "---------------------------------------------------------------------------------"
echo "Leaving Operations Databases Restoration Script"
echo "---------------------------------------------------------------------------------"
echo
