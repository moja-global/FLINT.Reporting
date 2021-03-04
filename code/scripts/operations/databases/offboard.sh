#!/bin/sh
# See: https://stackoverflow.com/questions/6405127/how-do-i-specify-a-password-to-psql-non-interactively#6405296
# See: https://stackoverflow.com/questions/14549270/check-if-database-exists-in-postgresql-using-shell

echo
echo "---------------------------------------------------------------------------------"
echo "Entering Databases Offboarding Script"
echo "---------------------------------------------------------------------------------"
echo



# -------------------------------------------------------------------------------------
# Get the name of the database that is being onboarded
# -------------------------------------------------------------------------------------

echo
echo "Enter the database name"
echo "---------------------------------------------------------------------------------"
read DATABASE



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
# Delete the raw data if and only if it exists in postgres
# -------------------------------------------------------------------------------------

echo
echo "Drop all existing raw ${DATABASE} connections other than yours"
echo "---------------------------------------------------------------------------------"
psql -c "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = '$DATABASE' AND pid <> pg_backend_pid()"


echo
echo "Deleting raw ${DATABASE} data from the reporting tool"
echo "---------------------------------------------------------------------------------"
psql -c "DROP DATABASE $DATABASE"



echo
echo "---------------------------------------------------------------------------------"
echo "Leaving Databases Offboarding Script"
echo "---------------------------------------------------------------------------------"
echo

echo
echo "---------------------------------------------------------------------------------"
echo "Troubleshooting"
echo "---------------------------------------------------------------------------------"

echo
echo "If the automatic raw database deletion operation failed, carry out the following steps"

echo
echo "Step 1: Navigate to the pod hosting the postgresql database:"
echo "kubectl exec -it flint-output-postgresql-0 -- /bin/bash"

echo
echo "Step 2: Log in to the postgresql database:"
echo "psql -h localhost -U postgres"

echo
echo "Step 3: Manually delete the database:"
echo "drop database $DATABASE;"





