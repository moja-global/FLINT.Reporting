#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering uninstall-script"
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
echo "Uninstalling Microservices"
echo

# Use the flags 1 and 0 below to configure the services that you want to uninstall
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
	bash $PROJECT_DIR/services/accountabilities/uninstall.sh
fi


# accountability-rules
# -------------------------------------------------------------------------------------
if [ $ACCOUNTABILITY_RULES -eq 1 ]; then
	bash $PROJECT_DIR/services/accountability-rules/uninstall.sh
fi


# accountability-types
# -------------------------------------------------------------------------------------
if [ $ACCOUNTABILITY_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/accountability-types/uninstall.sh
fi


# conversion-and-remaining-periods
# -------------------------------------------------------------------------------------
if [ $CONVERSION_AND_REMAINING_PERIODS -eq 1 ]; then
	bash $PROJECT_DIR/services/conversion-and-remaining-periods/uninstall.sh
fi


# cover-types
# -------------------------------------------------------------------------------------
if [ $COVER_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/cover-types/uninstall.sh
fi



# dates
# -------------------------------------------------------------------------------------
if [ $DATES -eq 1 ]; then
	bash $PROJECT_DIR/services/dates/uninstall.sh
fi


# emission-types
# -------------------------------------------------------------------------------------
if [ $EMISSION_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/emission-types/uninstall.sh
fi


# fluxes-to-reporting-variables
# -------------------------------------------------------------------------------------
if [ $FLUXES_TO_REPORTING_VARIABLES -eq 1 ]; then
	bash $PROJECT_DIR/services/fluxes-to-reporting-variables/uninstall.sh
fi


# flux-reporting-results
# -------------------------------------------------------------------------------------
if [ $FLUX_REPORTING_RESULTS -eq 1 ]; then
	bash $PROJECT_DIR/services/flux-reporting-results/uninstall.sh
fi


# flux-types
# -------------------------------------------------------------------------------------
if [ $FLUX_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/flux-types/uninstall.sh
fi


# land-use-categories
# -------------------------------------------------------------------------------------
if [ $LAND_USE_CATEGORIES -eq 1 ]; then
	bash $PROJECT_DIR/services/land-use-categories/uninstall.sh
fi


# land-uses-flux-types
# -------------------------------------------------------------------------------------
if [ $LAND_USES_FLUX_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/land-uses-flux-types/uninstall.sh
fi


# land-uses-flux-types-to-reporting-tables
# -------------------------------------------------------------------------------------
if [ $LAND_USES_FLUX_TYPES_TO_REPORTING_TABLES -eq 1 ]; then
	bash $PROJECT_DIR/services/land-uses-flux-types-to-reporting-tables/uninstall.sh
fi


# locations
# -------------------------------------------------------------------------------------
if [ $LOCATIONS -eq 1 ]; then
	bash $PROJECT_DIR/services/locations/uninstall.sh
fi


# parties
# -------------------------------------------------------------------------------------
if [ $PARTIES -eq 1 ]; then
	bash $PROJECT_DIR/services/parties/uninstall.sh
fi


# party-types
# -------------------------------------------------------------------------------------
if [ $PARTY_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/party-types/uninstall.sh
fi


# pools
# -------------------------------------------------------------------------------------
if [ $POOLS -eq 1 ]; then
	bash $PROJECT_DIR/services/pools/uninstall.sh
fi


# quantity-observations
# -------------------------------------------------------------------------------------
if [ $QUANTITY_OBSERVATIONS -eq 1 ]; then
	bash $PROJECT_DIR/services/quantity-observations/uninstall.sh
fi


# reporting-frameworks
# -------------------------------------------------------------------------------------
if [ $REPORTING_FRAMEWORKS -eq 1 ]; then
	bash $PROJECT_DIR/services/reporting-frameworks/uninstall.sh
fi

# reporting-tables
# -------------------------------------------------------------------------------------
if [ $REPORTING_TABLES -eq 1 ]; then
	bash $PROJECT_DIR/services/reporting-tables/uninstall.sh
fi


# reporting-variables
# -------------------------------------------------------------------------------------
if [ $REPORTING_VARIABLES -eq 1 ]; then
	bash $PROJECT_DIR/services/reporting-variables/uninstall.sh
fi


# unit-categories
# -------------------------------------------------------------------------------------
if [ $UNIT_CATEGORIES -eq 1 ]; then
	bash $PROJECT_DIR/services/unit-categories/uninstall.sh
fi


# units
# -------------------------------------------------------------------------------------
if [ $UNITS -eq 1 ]; then
	bash $PROJECT_DIR/services/units/uninstall.sh
fi


# 
# vegetation-history-vegetation-types
# -------------------------------------------------------------------------------------
if [ $VEGETATION_HISTORY_VEGETATION_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/vegetation-history-vegetation-types/uninstall.sh
fi


# vegetation-types
# -------------------------------------------------------------------------------------
if [ $VEGETATION_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/vegetation-types/uninstall.sh
fi


# -------------------------------------------------------------------------------------
# Dependent Microservices
# -------------------------------------------------------------------------------------


# task
# -------------------------------------------------------------------------------------
if [ $TASK -eq 1 ]; then
	bash $PROJECT_DIR/services/task/uninstall.sh
fi


# task-manager
# -------------------------------------------------------------------------------------
if [ $TASK_MANAGER -eq 1 ]; then
	bash $PROJECT_DIR/services/task-manager/uninstall.sh
fi


# databases
# -------------------------------------------------------------------------------------
if [ $DATABASES -eq 1 ]; then
	bash $PROJECT_DIR/services/databases/uninstall.sh
fi


# data-processor
# -------------------------------------------------------------------------------------
if [ $DATA_PROCESSOR -eq 1 ]; then
	bash $PROJECT_DIR/services/data-processor/uninstall.sh
fi


# data-aggregator
# -------------------------------------------------------------------------------------
if [ $DATA_AGGREGATOR -eq 1 ]; then
	bash $PROJECT_DIR/services/data-aggregator/uninstall.sh
fi



# crf-tables
# -------------------------------------------------------------------------------------
if [ $CRF_TABLES -eq 1 ]; then
	bash $PROJECT_DIR/services/crf-tables/uninstall.sh
fi


echo
echo "---------------------------------------------------------------------------------"
echo "Leaving uninstall-script"
echo "---------------------------------------------------------------------------------"
echo
