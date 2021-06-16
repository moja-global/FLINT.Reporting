#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering install-script"
echo "---------------------------------------------------------------------------------"
echo

echo
echo "Setting Up Resource Paths"
echo

# root/project/scripts/setup/system/services
SERVICES_DIR="$(cd "$(dirname "$0")" && pwd)"

# root/project/scripts/setup/system
SYSTEM_DIR="$(dirname "$SERVICES_DIR")"

# root/project/scripts/setup
SETUP_DIR="$(dirname "$SYSTEM_DIR")"

# root/project/scripts
SCRIPTS_DIR="$(dirname "$SETUP_DIR")"

# root/project
PROJECT_DIR="$(dirname "$SCRIPTS_DIR")"

echo
echo "installing Microservices"
echo

# Use the flags 1 and 0 below to configure the services that you want to install
# 1 = on, 0 = off
# ----------------------------------------------------------------------------------
ACCOUNTABILITIES=1
ACCOUNTABILITY_RULES=1
ACCOUNTABILITY_TYPES=1
CONVERSION_AND_REMAINING_PERIODS=1
COVER_TYPES=1
CRF_TABLES=1
DATABASES=1
DATES=1
DATA_AGGREGATOR=1
DATA_PROCESSOR=1
EMISSION_TYPES=1
FLUXES_TO_REPORTING_VARIABLES=1
FLUX_REPORTING_RESULTS=1
FLUX_TYPES=1
LAND_USE_CATEGORIES=1
LAND_USES_FLUX_TYPES=1
LAND_USES_FLUX_TYPES_TO_REPORTING_TABLES=1
LOCATIONS=1
PARTIES=1
PARTY_TYPES=1
POOLS=1
QUANTITY_OBSERVATIONS=1
REPORTING_FRAMEWORKS=1
REPORTING_TABLES=1
REPORTING_VARIABLES=1
TASK=1
TASK_MANAGER=1
UNIT_CATEGORIES=1
UNITS=1
VEGETATION_HISTORY_VEGETATION_TYPES=1
VEGETATION_TYPES=1



# -------------------------------------------------------------------------------------
# Independent Microservices
# -------------------------------------------------------------------------------------


# accountabilities
# -------------------------------------------------------------------------------------
if [ $ACCOUNTABILITIES -eq 1 ]; then
	bash $PROJECT_DIR/services/accountabilities/install.sh
fi


# accountability-rules
# -------------------------------------------------------------------------------------
if [ $ACCOUNTABILITY_RULES -eq 1 ]; then
	bash $PROJECT_DIR/services/accountability-rules/install.sh
fi


# accountability-types
# -------------------------------------------------------------------------------------
if [ $ACCOUNTABILITY_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/accountability-types/install.sh
fi


# conversion-and-remaining-periods
# -------------------------------------------------------------------------------------
if [ $CONVERSION_AND_REMAINING_PERIODS -eq 1 ]; then
	bash $PROJECT_DIR/services/conversion-and-remaining-periods/install.sh
fi


# cover-types
# -------------------------------------------------------------------------------------
if [ $COVER_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/cover-types/install.sh
fi



# dates
# -------------------------------------------------------------------------------------
if [ $DATES -eq 1 ]; then
	bash $PROJECT_DIR/services/dates/install.sh
fi


# emission-types
# -------------------------------------------------------------------------------------
if [ $EMISSION_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/emission-types/install.sh
fi


# fluxes-to-reporting-variables
# -------------------------------------------------------------------------------------
if [ $FLUXES_TO_REPORTING_VARIABLES -eq 1 ]; then
	bash $PROJECT_DIR/services/fluxes-to-reporting-variables/install.sh
fi


# flux-reporting-results
# -------------------------------------------------------------------------------------
if [ $FLUX_REPORTING_RESULTS -eq 1 ]; then
	bash $PROJECT_DIR/services/flux-reporting-results/install.sh
fi


# flux-types
# -------------------------------------------------------------------------------------
if [ $FLUX_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/flux-types/install.sh
fi


# land-use-categories
# -------------------------------------------------------------------------------------
if [ $LAND_USE_CATEGORIES -eq 1 ]; then
	bash $PROJECT_DIR/services/land-use-categories/install.sh
fi


# land-uses-flux-types
# -------------------------------------------------------------------------------------
if [ $LAND_USES_FLUX_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/land-uses-flux-types/install.sh
fi


# land-uses-flux-types-to-reporting-tables
# -------------------------------------------------------------------------------------
if [ $LAND_USES_FLUX_TYPES_TO_REPORTING_TABLES -eq 1 ]; then
	bash $PROJECT_DIR/services/land-uses-flux-types-to-reporting-tables/install.sh
fi


# locations
# -------------------------------------------------------------------------------------
if [ $LOCATIONS -eq 1 ]; then
	bash $PROJECT_DIR/services/locations/install.sh
fi


# parties
# -------------------------------------------------------------------------------------
if [ $PARTIES -eq 1 ]; then
	bash $PROJECT_DIR/services/parties/install.sh
fi


# party-types
# -------------------------------------------------------------------------------------
if [ $PARTY_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/party-types/install.sh
fi


# pools
# -------------------------------------------------------------------------------------
if [ $POOLS -eq 1 ]; then
	bash $PROJECT_DIR/services/pools/install.sh
fi


# quantity-observations
# -------------------------------------------------------------------------------------
if [ $QUANTITY_OBSERVATIONS -eq 1 ]; then
	bash $PROJECT_DIR/services/quantity-observations/install.sh
fi


# reporting-frameworks
# -------------------------------------------------------------------------------------
if [ $REPORTING_FRAMEWORKS -eq 1 ]; then
	bash $PROJECT_DIR/services/reporting-frameworks/install.sh
fi

# reporting-tables
# -------------------------------------------------------------------------------------
if [ $REPORTING_TABLES -eq 1 ]; then
	bash $PROJECT_DIR/services/reporting-tables/install.sh
fi


# reporting-variables
# -------------------------------------------------------------------------------------
if [ $REPORTING_VARIABLES -eq 1 ]; then
	bash $PROJECT_DIR/services/reporting-variables/install.sh
fi


# unit-categories
# -------------------------------------------------------------------------------------
if [ $UNIT_CATEGORIES -eq 1 ]; then
	bash $PROJECT_DIR/services/unit-categories/install.sh
fi


# units
# -------------------------------------------------------------------------------------
if [ $UNITS -eq 1 ]; then
	bash $PROJECT_DIR/services/units/install.sh
fi


# 
# vegetation-history-vegetation-types
# -------------------------------------------------------------------------------------
if [ $VEGETATION_HISTORY_VEGETATION_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/vegetation-history-vegetation-types/install.sh
fi


# vegetation-types
# -------------------------------------------------------------------------------------
if [ $VEGETATION_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/vegetation-types/install.sh
fi


# -------------------------------------------------------------------------------------
# Dependent Microservices
# -------------------------------------------------------------------------------------


# task
# -------------------------------------------------------------------------------------
if [ $TASK -eq 1 ]; then
	bash $PROJECT_DIR/services/task/install.sh
fi


# task-manager
# -------------------------------------------------------------------------------------
if [ $TASK_MANAGER -eq 1 ]; then
	bash $PROJECT_DIR/services/task-manager/install.sh
fi


# databases
# -------------------------------------------------------------------------------------
if [ $DATABASES -eq 1 ]; then
	bash $PROJECT_DIR/services/databases/install.sh
fi


# data-processor
# -------------------------------------------------------------------------------------
if [ $DATA_PROCESSOR -eq 1 ]; then
	bash $PROJECT_DIR/services/data-processor/install.sh
fi


# data-aggregator
# -------------------------------------------------------------------------------------
if [ $DATA_AGGREGATOR -eq 1 ]; then
	bash $PROJECT_DIR/services/data-aggregator/install.sh
fi



# crf-tables
# -------------------------------------------------------------------------------------
if [ $CRF_TABLES -eq 1 ]; then
	bash $PROJECT_DIR/services/crf-tables/install.sh
fi


echo
echo "---------------------------------------------------------------------------------"
echo "Leaving install-script"
echo "---------------------------------------------------------------------------------"
echo
