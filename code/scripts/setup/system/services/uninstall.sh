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
echo "uninstalling Microservices"
echo

# Use the flags 1 and 0 below to configure the services that you want to uninstall
# 1 = on, 0 = off
# ----------------------------------------------------------------------------------
CONVERSION_AND_REMAINING_PERIODS=1
COVER_TYPES=1
DATABASES=1
DATES=1
EMISSION_TYPES=1
FLUXES_TO_REPORTING_VARIABLES=1
FLUX_TYPES=1
LAND_USE_CATEGORIES=1
LAND_USES_FLUX_TYPES=1
LAND_USES_FLUX_TYPES_TO_REPORTING_TABLES=1
LOCATIONS=1
POOLS=1
REPORTING_FRAMEWORKS=1
REPORTING_TABLES=1
REPORTING_VARIABLES=1
UNIT_CATEGORIES=1
UNITS=1
VEGETATION_HISTORY_VEGETATION_TYPES=1
VEGETATION_TYPES=1


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


# databases
# -------------------------------------------------------------------------------------
if [ $DATABASES -eq 1 ]; then
	bash $PROJECT_DIR/services/databases/uninstall.sh
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


# locations
# -------------------------------------------------------------------------------------
if [ $LOCATIONS -eq 1 ]; then
	bash $PROJECT_DIR/services/locations/uninstall.sh
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


# pools
# -------------------------------------------------------------------------------------
if [ $POOLS -eq 1 ]; then
	bash $PROJECT_DIR/services/pools/uninstall.sh
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


echo
echo "---------------------------------------------------------------------------------"
echo "Leaving uninstall-script"
echo "---------------------------------------------------------------------------------"
echo
