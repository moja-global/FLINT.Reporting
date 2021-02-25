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
EMISSION_TYPES=1
FLUX_TYPES=1
FLUXES_TO_UNFCCC_VARIABLES=1
REPORTING_FRAMEWORKS=1
REPORTING_TABLES=1
UNFCCC_VARIABLES=1
UNIT_CATEGORIES=1
UNITS=1



# -------------------------------------------------------------------------------------
# REPORTING
# -------------------------------------------------------------------------------------

# reporting-frameworks
# -------------------------------------------------------------------------------------
if [ $REPORTING_FRAMEWORKS -eq 1 ]; then
	bash $PROJECT_DIR/services/reporting-frameworks/uninstall.sh
fi

# emission-types
# -------------------------------------------------------------------------------------
if [ $EMISSION_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/emission-types/uninstall.sh
fi


# flux-types
# -------------------------------------------------------------------------------------
if [ $FLUX_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/flux-types/uninstall.sh
fi


# reporting-tables
# -------------------------------------------------------------------------------------
if [ $REPORTING_TABLES -eq 1 ]; then
	bash $PROJECT_DIR/services/reporting-tables/uninstall.sh
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



# unfccc-variables
# -------------------------------------------------------------------------------------
if [ $UNFCCC_VARIABLES -eq 1 ]; then
	bash $PROJECT_DIR/services/unfccc-variables/uninstall.sh
fi


# fluxes-to-unfccc-variables
# -------------------------------------------------------------------------------------
if [ $FLUXES_TO_UNFCCC_VARIABLES -eq 1 ]; then
	bash $PROJECT_DIR/services/fluxes-to-unfccc-variables/uninstall.sh
fi


echo
echo "---------------------------------------------------------------------------------"
echo "Leaving uninstall-script"
echo "---------------------------------------------------------------------------------"
echo
