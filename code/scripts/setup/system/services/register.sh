#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering register-script"
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
echo "registering Microservices"
echo

# Use the flags 1 and 0 below to configure the services that you want to register
# 1 = on, 0 = off
# ----------------------------------------------------------------------------------
EMISSION_TYPES=1
FLUX_TYPES=1
FLUXES_TO_REPORTING_VARIABLES=1
POOLS=1
REPORTING_FRAMEWORKS=1
REPORTING_TABLES=1
REPORTING_VARIABLES=1
UNIT_CATEGORIES=1
UNITS=1




# reporting-frameworks
# -------------------------------------------------------------------------------------
if [ $REPORTING_FRAMEWORKS -eq 1 ]; then
	bash $PROJECT_DIR/services/reporting-frameworks/register.sh
fi

# reporting-tables
# -------------------------------------------------------------------------------------
if [ $REPORTING_TABLES -eq 1 ]; then
	bash $PROJECT_DIR/services/reporting-tables/register.sh
fi

# reporting-variables
# -------------------------------------------------------------------------------------
if [ $REPORTING_VARIABLES -eq 1 ]; then
	bash $PROJECT_DIR/services/reporting-variables/register.sh
fi

# emission-types
# -------------------------------------------------------------------------------------
if [ $EMISSION_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/emission-types/register.sh
fi

# pools
# -------------------------------------------------------------------------------------
if [ $POOLS -eq 1 ]; then
	bash $PROJECT_DIR/services/pools/register.sh
fi

# flux-types
# -------------------------------------------------------------------------------------
if [ $FLUX_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/flux-types/register.sh
fi

# fluxes-to-reporting-variables
# -------------------------------------------------------------------------------------
if [ $FLUXES_TO_REPORTING_VARIABLES -eq 1 ]; then
	bash $PROJECT_DIR/services/fluxes-to-reporting-variables/register.sh
fi

# unit-categories
# -------------------------------------------------------------------------------------
if [ $UNIT_CATEGORIES -eq 1 ]; then
	bash $PROJECT_DIR/services/unit-categories/register.sh
fi


# units
# -------------------------------------------------------------------------------------
if [ $UNITS -eq 1 ]; then
	bash $PROJECT_DIR/services/units/register.sh
fi


echo
echo "---------------------------------------------------------------------------------"
echo "Leaving register-script"
echo "---------------------------------------------------------------------------------"
echo
