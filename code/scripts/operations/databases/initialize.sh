#!/bin/sh
# See: https://stackoverflow.com/questions/6405127/how-do-i-specify-a-password-to-psql-non-interactively#6405296
# See: https://stackoverflow.com/questions/14549270/check-if-database-exists-in-postgresql-using-shell

echo
echo "---------------------------------------------------------------------------------"
echo "Entering Operations Databases Initialization Script"
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

# Use the flags 1 and 0 below to configure the databases that you want to initialize
# 1 = on, 0 = off
# ----------------------------------------------------------------------------------

# Single databases
ACCOUNTABILITIES=1
ACCOUNTABILITY_RULES=1
ACCOUNTABILITY_TYPES=1
CATEGORIES=1
CATEGORY_OBSERVATIONS=1
CONTACTS=1
CONTACT_TYPES=1
DATA_AGGREGATION_TASKS=1
DATA_AGGREGATOR=1
DATA_INTEGRATION_TASKS=1
ENUMERATORS=1
OBSERVATION_TYPES=1
PARTIES=1
PARTY_TYPES=1
PHENOMENA_TYPES=1
PLACES=1
QUANTITY_OBSERVATIONS=1
REPORTS=1
KOBO_COLLECT_ADAPTER=1
TASKS=1
TASKS_BACKLOG=1
TASKS_LOGS=1
TASKS_MANAGER=1
TASKS_TYPES=1
TASKS_STATES=1
TIME_PERIODS=1
TIME_POINTS=1
UNITS=1

# Database Collections
ADMIN_UNITS=1


echo
echo "Initializing specific databases"
echo "---------------------------------------------------------------------------------"
echo

# -------------------------------------------------------------------------------------
# PARTIES
# -------------------------------------------------------------------------------------

# Party Types
# -------------------------------------------------------------------------------------
if [ $PARTY_TYPES -eq 1 ]; then

  echo
  echo "Setting up Party Types Database"
  echo

  # drop the party types database if it exists
  psql -c "DROP DATABASE IF EXISTS party_types"

  # create a new party types database
  psql -c "CREATE DATABASE party_types"

  # Create the party type's database objects
  psql -d "party_types" -1 -f "$PROJECT_DIR/library/party-types/src/main/resources/party_types.sql"

  # Load the party type's database data
  psql -d "party_types" -1 -c "\copy party_type(parent_id,name,version) from \
          '$PROJECT_DIR/data/party_types.csv' DELIMITER ',' CSV HEADER"

fi

# Parties
# -------------------------------------------------------------------------------------
if [ $PARTIES -eq 1 ]; then

  echo
  echo "Setting up Parties Database"
  echo

  # drop the parties database if it exists
  psql -c "DROP DATABASE IF EXISTS parties"

  # create a new parties database
  psql -c "CREATE DATABASE parties"

  # Create the parties database objects
  psql -d "parties" -1 -f "$PROJECT_DIR/library/parties/src/main/resources/parties.sql"

fi

# Places
# -------------------------------------------------------------------------------------
if [ $PLACES -eq 1 ]; then

  echo
  echo "Setting up Places Database"
  echo

  # drop the places database if it exists
  psql -c "DROP DATABASE IF EXISTS places"

  # create a new places database
  psql -c "CREATE DATABASE places"

  # add postgis extension
  #psql -d "places" -1 -c "CREATE EXTENSION IF NOT EXISTS postgis;"

  # Create the places database objects
  psql -d "places" -1 -f "$PROJECT_DIR/library/places/src/main/resources/places.sql"

fi

# Enumerators
# ----------------------------------------------------------------------------------
if [ $ENUMERATORS -eq 1 ]; then

  echo
  echo "Loading Enumerators Data"
  echo

  # Load the enumerators data
  psql -d "parties" -1 -c "\copy party(party_type_id,name,version) from \
          '$PROJECT_DIR/data/enumerators.csv' DELIMITER ',' CSV HEADER"

fi

# -------------------------------------------------------------------------------------
# CONTACTS
# -------------------------------------------------------------------------------------

# Contact Types
# -------------------------------------------------------------------------------------
if [ $CONTACT_TYPES -eq 1 ]; then

  echo
  echo "Setting up Contact Types Database"
  echo

  # drop the contact types database if it exists
  psql -c "DROP DATABASE IF EXISTS contact_types"

  # create a new contact types database
  psql -c "CREATE DATABASE contact_types"

  # Create the contact type's database objects
  psql -d "contact_types" -1 -f "$PROJECT_DIR/library/contact-types/src/main/resources/contact_types.sql"

  # Load the contact type's database data
  psql -d "contact_types" -1 -c "\copy contact_type(parent_id,name,version) from \
          '$PROJECT_DIR/data/contact_types.csv' DELIMITER ',' CSV HEADER"

fi

# Contacts
# -------------------------------------------------------------------------------------
if [ $CONTACTS -eq 1 ]; then

  echo
  echo "Setting up Contacts Database"
  echo

  # drop the contacts database if it exists
  psql -c "DROP DATABASE IF EXISTS contacts"

  # create a new contacts database
  psql -c "CREATE DATABASE contacts"

  # Create the contacts database objects
  psql -d "contacts" -1 -f "$PROJECT_DIR/library/contacts/src/main/resources/contacts.sql"

  # Load the party type's database data
  psql -d "contacts" -1 -c "\copy contact(party_id,contact_type_id,value,version) from \
          '$PROJECT_DIR/data/contacts.csv' DELIMITER ',' CSV HEADER"

fi

# -------------------------------------------------------------------------------------
# ACCOUNTABILITIES
# -------------------------------------------------------------------------------------

# Accountability Types
# -------------------------------------------------------------------------------------
if [ $ACCOUNTABILITY_TYPES -eq 1 ]; then

  echo
  echo "Setting up Accountability Types Database"
  echo

  # drop the accountability types database if it exists
  psql -c "DROP DATABASE IF EXISTS accountability_types"

  # create a new accountability types database
  psql -c "CREATE DATABASE accountability_types"

  # Create the accountability type's database objects
  psql -d "accountability_types" -1 -f "$PROJECT_DIR/library/accountability-types/src/main/resources/accountability_types.sql"

  # Load the accountability type's database data
  psql -d "accountability_types" -1 -c "\copy accountability_type(name,version) from \
          '$PROJECT_DIR/data/accountability_types.csv' DELIMITER ',' CSV HEADER"

fi

# Accountability Rules
# -------------------------------------------------------------------------------------
if [ $ACCOUNTABILITY_RULES -eq 1 ]; then

  echo
  echo "Setting up Accountability Rules Database"
  echo

  # drop the accountability rules database if it exists
  psql -c "DROP DATABASE IF EXISTS accountability_rules"

  # create a new accountability rules database
  psql -c "CREATE DATABASE accountability_rules"

  # Create the accountability rule's database objects
  psql -d "accountability_rules" -1 -f "$PROJECT_DIR/library/accountability-rules/src/main/resources/accountability_rules.sql"

  # Load the accountability rule's database data
  psql -d "accountability_rules" -1 -c "\copy accountability_rule(accountability_type_id,parent_party_type_id,subsidiary_party_type_id,version) from \
          '$PROJECT_DIR/data/accountability_rules.csv' DELIMITER ',' CSV HEADER"

fi

# Accountabilities
# -------------------------------------------------------------------------------------
if [ $ACCOUNTABILITIES -eq 1 ]; then

  echo
  echo "Setting up Accountabilities Database"
  echo

  # drop the accountabilities database if it exists
  psql -c "DROP DATABASE IF EXISTS accountabilities"

  # create a new accountabilities database
  psql -c "CREATE DATABASE accountabilities"

  # Create the accountabilities database objects
  psql -d "accountabilities" -1 -f "$PROJECT_DIR/library/accountabilities/src/main/resources/accountabilities.sql"

fi

# -------------------------------------------------------------------------------------
# TIME
# -------------------------------------------------------------------------------------

# TIme Points
# -------------------------------------------------------------------------------------
if [ $TIME_POINTS -eq 1 ]; then

  echo
  echo "Setting up Time Points Database"
  echo

  # drop the time points database if it exists
  psql -c "DROP DATABASE IF EXISTS time_points"

  # create a new time points database
  psql -c "CREATE DATABASE time_points"

  # Create the time point's database objects
  psql -d "time_points" -1 -f "$PROJECT_DIR/library/time-points/src/main/resources/time_points.sql"

fi

# Time Periods
# -------------------------------------------------------------------------------------
if [ $TIME_PERIODS -eq 1 ]; then

  echo
  echo "Setting up Time Periods Database"
  echo

  # drop the time periods database if it exists
  psql -c "DROP DATABASE IF EXISTS time_periods"

  # create a new time periods database
  psql -c "CREATE DATABASE time_periods"

  # Create the time period's database objects
  psql -d "time_periods" -1 -f "$PROJECT_DIR/library/time-periods/src/main/resources/time_periods.sql"

  # Load the time period's weeks data
  psql -d "time_periods" -1 -c "\copy time_period(start_time_point_id,end_time_point_id,version) from \
          '$PROJECT_DIR/data/weeks.csv' DELIMITER ',' CSV HEADER"

fi

# -------------------------------------------------------------------------------------
# OBSERVATIONS
# -------------------------------------------------------------------------------------

# Phenomena Types
# -------------------------------------------------------------------------------------
if [ $PHENOMENA_TYPES -eq 1 ]; then

  echo
  echo "Setting up Phenomena Types Database"
  echo

  # drop the phenomena types database if it exists
  psql -c "DROP DATABASE IF EXISTS phenomena_types"

  # create a new phenomena types database
  psql -c "CREATE DATABASE phenomena_types"

  # Create the phenomena type's database objects
  psql -d "phenomena_types" -1 -f "$PROJECT_DIR/library/phenomena-types/src/main/resources/phenomena_types.sql"

  # Load the phenomena type's database data
  psql -d "phenomena_types" -1 -c "\copy phenomenon_type(name,version) from \
          '$PROJECT_DIR/data/phenomena_types.csv' DELIMITER ',' CSV HEADER"

fi

# Categories
# -------------------------------------------------------------------------------------
if [ $CATEGORIES -eq 1 ]; then

  echo
  echo "Setting up Categories Database"
  echo

  # drop the phenomenon types database if it exists
  psql -c "DROP DATABASE IF EXISTS categories"

  # create a new phenomenon types database
  psql -c "CREATE DATABASE categories"

  # Create the phenomenon type's database objects
  psql -d "categories" -1 -f "$PROJECT_DIR/library/categories/src/main/resources/categories.sql"

  # Load the phenomenon type's database data
  psql -d "categories" -1 -c "\copy category(name,version) from \
          '$PROJECT_DIR/data/categories.csv' DELIMITER ',' CSV HEADER"

fi

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

  # Create the units database objects
  psql -d "units" -1 -f "$PROJECT_DIR/library/units/src/main/resources/units.sql"

  # Load the unit's database data
  psql -d "units" -1 -c "\copy unit(name,abbreviation,version) from \
          '$PROJECT_DIR/data/units.csv' DELIMITER ',' CSV HEADER"

fi

# Observation Types
# -------------------------------------------------------------------------------------
if [ $OBSERVATION_TYPES -eq 1 ]; then

  echo
  echo "Setting up Observations Types Database"
  echo

  # drop the observations types database if it exists
  psql -c "DROP DATABASE IF EXISTS observations_types"

  # create a new observations types database
  psql -c "CREATE DATABASE observations_types"

  # Create the observations type's database objects
  psql -d "observations_types" -1 -f "$PROJECT_DIR/library/observations-types/src/main/resources/observations_types.sql"

  # Load the observations type's database data
  psql -d "observations_types" -1 -c "\copy observation_type(name,version) from \
          '$PROJECT_DIR/data/observations_types.csv' DELIMITER ',' CSV HEADER"

fi

# Category Observations
# -------------------------------------------------------------------------------------
if [ $CATEGORY_OBSERVATIONS -eq 1 ]; then

  echo
  echo "Setting up Category Observations Database"
  echo

  # drop the Category Observations if it exists
  psql -c "DROP DATABASE IF EXISTS category_observations"

  # create a new Category Observations database
  psql -c "CREATE DATABASE category_observations"

  # Create the Category Observations database objects
  psql -d "category_observations" -1 -f "$PROJECT_DIR/library/category-observations/src/main/resources/category_observations.sql"

fi

# Quantity Observations
# -------------------------------------------------------------------------------------
if [ $QUANTITY_OBSERVATIONS -eq 1 ]; then

  echo
  echo "Setting up Quantity Observations Database"
  echo

  # drop the Quantity Observations if it exists
  psql -c "DROP DATABASE IF EXISTS quantity_observations"

  # create a new Quantity Observations database
  psql -c "CREATE DATABASE quantity_observations"

  # Create the Quantity Observations database objects
  psql -d "quantity_observations" -1 -f "$PROJECT_DIR/library/quantity-observations/src/main/resources/quantity_observations.sql"

fi

# -------------------------------------------------------------------------------------
# TASKS
# -------------------------------------------------------------------------------------

# Task Types
# -------------------------------------------------------------------------------------
if [ $TASKS_TYPES -eq 1 ]; then

  echo
  echo "Setting up Task Types Database"
  echo

  # drop the tasks types database if it exists
  psql -c "DROP DATABASE IF EXISTS tasks_types"

  # create a new tasks types database
  psql -c "CREATE DATABASE tasks_types"

  # Create the tasks type's database objects
  psql -d "tasks_types" -1 -f "$PROJECT_DIR/library/tasks-types/src/main/resources/tasks_types.sql"

  # Load the tasks type's database data
  psql -d "tasks_types" -1 -c "\copy task_type(name,version) from \
          '$PROJECT_DIR/data/tasks_types.csv' DELIMITER ',' CSV HEADER"

fi

# Task States
# -------------------------------------------------------------------------------------
if [ $TASKS_STATES -eq 1 ]; then

  echo
  echo "Setting up Task States Database"
  echo

  # drop the tasks states database if it exists
  psql -c "DROP DATABASE IF EXISTS tasks_states"

  # create a new tasks states database
  psql -c "CREATE DATABASE tasks_states"

  # Create the tasks state's database objects
  psql -d "tasks_states" -1 -f "$PROJECT_DIR/library/tasks-states/src/main/resources/tasks_states.sql"

  # Load the tasks state's database data
  psql -d "tasks_states" -1 -c "\copy task_state(name,version) from \
          '$PROJECT_DIR/data/tasks_states.csv' DELIMITER ',' CSV HEADER"

fi

# Tasks
# -------------------------------------------------------------------------------------
if [ $TASKS -eq 1 ]; then

  echo
  echo "Setting up Tasks Database"
  echo

  # drop the tasks database if it exists
  psql -c "DROP DATABASE IF EXISTS tasks"

  # create a new tasks database
  psql -c "CREATE DATABASE tasks"

  # Create the task's database objects
  psql -d "tasks" -1 -f "$PROJECT_DIR/library/tasks/src/main/resources/tasks.sql"

fi

# Data Integration Tasks
# -------------------------------------------------------------------------------------
if [ $DATA_INTEGRATION_TASKS -eq 1 ]; then

  echo
  echo "Setting up Data Integration Tasks Database"
  echo

  # drop the data integration tasks database if it exists
  psql -c "DROP DATABASE IF EXISTS data_integration_tasks"

  # create a new data integration tasks database
  psql -c "CREATE DATABASE data_integration_tasks"

  # Create the data integration tasks database objects
  psql -d "data_integration_tasks" -1 -f "$PROJECT_DIR/library/data-integration-tasks/src/main/resources/data_integration_tasks.sql"

fi

# Data Aggregation Tasks
# -------------------------------------------------------------------------------------
if [ $DATA_AGGREGATION_TASKS -eq 1 ]; then

  echo
  echo "Setting up Data Aggregation Tasks Database"
  echo

  # drop the data aggregation tasks database if it exists
  psql -c "DROP DATABASE IF EXISTS data_aggregation_tasks"

  # create a new data aggregation tasks database
  psql -c "CREATE DATABASE data_aggregation_tasks"

  # Create the data aggregation tasks database objects
  psql -d "data_aggregation_tasks" -1 -f "$PROJECT_DIR/library/data-aggregation-tasks/src/main/resources/data_aggregation_tasks.sql"

fi

# Tasks Logs
# -------------------------------------------------------------------------------------
if [ $TASKS_LOGS -eq 1 ]; then

  echo
  echo "Setting up Task Logs Database"
  echo

  # drop the tasks types database if it exists
  psql -c "DROP DATABASE IF EXISTS tasks_logs"

  # create a new tasks types database
  psql -c "CREATE DATABASE tasks_logs"

  # Create the tasks type's database objects
  psql -d "tasks_logs" -1 -f "$PROJECT_DIR/library/tasks-logs/src/main/resources/tasks_logs.sql"

fi

# Tasks Backlog
# -------------------------------------------------------------------------------------
if [ $TASKS_BACKLOG -eq 1 ]; then

  echo
  echo "Setting up Tasks Backlog Database"
  echo

  # drop the tasks backlog database if it exists
  psql -c "DROP DATABASE IF EXISTS tasks_backlog"

  # create a new tasks backlog database
  psql -c "CREATE DATABASE tasks_backlog"

  # Create the tasks backlog's database objects
  psql -d "tasks_backlog" -1 -f "$PROJECT_DIR/library/tasks-backlog/src/main/resources/tasks_backlog.sql"

fi

echo
echo "Initializing database collections"
echo "---------------------------------------------------------------------------------"
echo


# ----------------------------------------------------------------------------------
# ADMIN UNITS
# ----------------------------------------------------------------------------------
if [ $ADMIN_UNITS -eq 1 ]; then

  echo
  echo "Loading Administrative Units Data"
  echo

  IFS=","
  while read f1 f2 f3 f4 f5 f6; do
    printf '%s\t%s\n' "$f5" "$f6"

    id=${f1:=null}
    partyTypeId=${f2:=null}
    parentId=${f3:=null}
    enumeratorId=${f4:=null}
    code=${f5:=null}
    name=${f6:=null}

    # Create Party Record
    psql -d "parties" -c "INSERT INTO party (party_type_id,name,version) VALUES ($partyTypeId, $name, 1)"

    # Create Administrative Accountability Record
    psql -d "accountabilities" -c "INSERT INTO accountability (accountability_type_id, parent_party_id, subsidiary_party_id, version) VALUES (1, $parentId, $id, 1)"    

    # If dealing with a village, create enumeration hierarchy
    if [ $f2 -eq 14 ]; then
      psql -d "accountabilities" -c "INSERT INTO accountability (accountability_type_id, parent_party_id, subsidiary_party_id, version) VALUES (3, $id, $enumeratorId, 1)"
    fi

    # Create Place Record
    psql -d "places" -c "INSERT INTO place (party_id,geometry,version) VALUES ($id, null, 1)"
    echo

  done <$PROJECT_DIR/data/admin_units.csv

fi


echo
echo "---------------------------------------------------------------------------------"
echo "Leaving Operations Databases Initialization Script"
echo "---------------------------------------------------------------------------------"
echo
