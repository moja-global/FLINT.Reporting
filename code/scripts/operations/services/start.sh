#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering build-script"
echo "---------------------------------------------------------------------------------"
echo

echo
echo "Setting Up Resource Paths"
echo

# root/project/scripts/operations/services
SERVICES_DIR="$(cd "$(dirname "$0")" && pwd)"

# root/project/scripts/operations
OPERATIONS_DIR="$(dirname "$SERVICES_DIR")"

# root/project/scripts
SCRIPTS_DIR="$(dirname "$OPERATIONS_DIR")"

# root/project
PROJECT_DIR="$(dirname "$SCRIPTS_DIR")"


echo
echo "Building Microservices"
echo

# Use the flags 1 and 0 below to configure the microservices that you want to build
# 1 = on, 0 = off
# ----------------------------------------------------------------------------------

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
DATA_SAVER=1
EMISSIONS_ESTIMATOR=1
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

# -------------------------------------------------------------------------------------
# PARTIES
# -------------------------------------------------------------------------------------

# Party Types
# -------------------------------------------------------------------------------------
if [ $PARTY_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/library/party-types/install.sh
fi

# Parties
# -------------------------------------------------------------------------------------
if [ $PARTIES -eq 1 ]; then
	bash $PROJECT_DIR/library/parties/install.sh
fi

# Places
# -------------------------------------------------------------------------------------
if [ $PLACES -eq 1 ]; then
	bash $PROJECT_DIR/library/places/install.sh
fi

# -------------------------------------------------------------------------------------
# CONTACTS
# -------------------------------------------------------------------------------------

# Contact Types
# -------------------------------------------------------------------------------------
if [ $CONTACT_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/library/contact-types/install.sh
fi

# Contacts
# -------------------------------------------------------------------------------------
if [ $CONTACTS -eq 1 ]; then
	bash $PROJECT_DIR/library/contacts/install.sh
fi

# -------------------------------------------------------------------------------------
# ACCOUNTABILITIES
# -------------------------------------------------------------------------------------

# Accountability Types
# -------------------------------------------------------------------------------------
if [ $ACCOUNTABILITY_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/library/accountability-types/install.sh
fi

# Accountability Rules
# -------------------------------------------------------------------------------------
if [ $ACCOUNTABILITY_RULES -eq 1 ]; then
	bash $PROJECT_DIR/library/accountability-rules/install.sh
fi

# Accountabilities
# -------------------------------------------------------------------------------------
if [ $ACCOUNTABILITIES -eq 1 ]; then
	bash $PROJECT_DIR/library/accountabilities/install.sh
fi

# -------------------------------------------------------------------------------------
# TIME
# -------------------------------------------------------------------------------------

# Time Points
# -------------------------------------------------------------------------------------
if [ $TIME_POINTS -eq 1 ]; then
	bash $PROJECT_DIR/library/time-points/install.sh
fi

# Time Periods
# -------------------------------------------------------------------------------------
if [ $TIME_PERIODS -eq 1 ]; then
	bash $PROJECT_DIR/library/time-periods/install.sh
fi

# -------------------------------------------------------------------------------------
# OBSERVATIONS
# -------------------------------------------------------------------------------------

# Phenomena Types
# -------------------------------------------------------------------------------------
if [ $PHENOMENA_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/library/phenomena-types/install.sh
fi

# Categories
# -------------------------------------------------------------------------------------
if [ $CATEGORIES -eq 1 ]; then
	bash $PROJECT_DIR/library/categories/install.sh
fi

# Units
# -------------------------------------------------------------------------------------
if [ $UNITS -eq 1 ]; then
	bash $PROJECT_DIR/library/units/install.sh
fi

# Observation Types
# -------------------------------------------------------------------------------------
if [ $OBSERVATION_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/library/observations-types/install.sh
fi


# Category Observations
# -------------------------------------------------------------------------------------
if [ $CATEGORY_OBSERVATIONS -eq 1 ]; then
	bash $PROJECT_DIR/library/category-observations/install.sh
fi

# Quantity Observations
# -------------------------------------------------------------------------------------
if [ $QUANTITY_OBSERVATIONS -eq 1 ]; then
	bash $PROJECT_DIR/library/quantity-observations/install.sh
fi

# -------------------------------------------------------------------------------------
# DATA MANAGEMENT
# -------------------------------------------------------------------------------------

# Kobo Collect Adapter
# -------------------------------------------------------------------------------------
if [ $KOBO_COLLECT_ADAPTER -eq 1 ]; then
	bash $PROJECT_DIR/library/kobo-collect-adapter/install.sh
fi


# Emissions Estimator
# -------------------------------------------------------------------------------------
if [ $EMISSIONS_ESTIMATOR -eq 1 ]; then
	bash $PROJECT_DIR/library/emissions-estimator/install.sh
fi


# Data Importer
# -------------------------------------------------------------------------------------
if [ $DATA_SAVER -eq 1 ]; then
	bash $PROJECT_DIR/library/data-saver/install.sh
fi

# Data Aggregator
# -------------------------------------------------------------------------------------
if [ $DATA_AGGREGATOR -eq 1 ]; then
	bash $PROJECT_DIR/library/data-aggregator/install.sh
fi

# -------------------------------------------------------------------------------------
# TASKS
# -------------------------------------------------------------------------------------

# Task Types
# -------------------------------------------------------------------------------------
if [ $TASKS_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/library/tasks-types/install.sh
fi

# Task States
# -------------------------------------------------------------------------------------
if [ $TASKS_STATES -eq 1 ]; then
	bash $PROJECT_DIR/library/tasks-states/install.sh
fi

# Tasks
# -------------------------------------------------------------------------------------
if [ $TASKS -eq 1 ]; then
	bash $PROJECT_DIR/library/tasks/install.sh
fi

# Data Importation Tasks
# -------------------------------------------------------------------------------------
if [ $DATA_INTEGRATION_TASKS -eq 1 ]; then
	bash $PROJECT_DIR/library/data-integration-tasks/install.sh
fi

# Data Aggregation Tasks
# -------------------------------------------------------------------------------------
if [ $DATA_AGGREGATION_TASKS -eq 1 ]; then
	bash $PROJECT_DIR/library/data-aggregation-tasks/install.sh
fi

# Tasks Logs
# -------------------------------------------------------------------------------------
if [ $TASKS_LOGS -eq 1 ]; then
	bash $PROJECT_DIR/library/tasks-logs/install.sh
fi

# Tasks Backlog
# -------------------------------------------------------------------------------------
if [ $TASKS_BACKLOG -eq 1 ]; then
	bash $PROJECT_DIR/library/tasks-backlog/install.sh
fi

# Tasks Manager
# -------------------------------------------------------------------------------------
if [ $TASKS_MANAGER -eq 1 ]; then
	bash $PROJECT_DIR/library/tasks-manager/install.sh
fi

# Reports
# ____________________________________________________________________________

if [ $REPORTS -eq 1 ]; then
	bash $PROJECT_DIR/library/reports/install.sh
fi

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving build-script"
echo "---------------------------------------------------------------------------------"
echo
