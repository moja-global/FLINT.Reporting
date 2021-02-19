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


# Database Collections



echo
echo "Initializing specific databases"
echo "---------------------------------------------------------------------------------"
echo

# -------------------------------------------------------------------------------------
# 
# -------------------------------------------------------------------------------------



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
