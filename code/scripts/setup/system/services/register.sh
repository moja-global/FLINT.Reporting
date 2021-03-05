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
ACCOUNTABILITIES=1
ACCOUNTABILITY_RULES=1
ACCOUNTABILITY_TYPES=1
CONVERSION_AND_REMAINING_PERIODS=1
COVER_TYPES=1
DATABASES=1
DATES=1
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
UNIT_CATEGORIES=1
UNITS=1
VEGETATION_HISTORY_VEGETATION_TYPES=1
VEGETATION_TYPES=1


# accountabilities
# -------------------------------------------------------------------------------------
if [ $ACCOUNTABILITIES -eq 1 ]; then
	bash $PROJECT_DIR/services/accountabilities/register.sh
fi


# accountability-rules
# -------------------------------------------------------------------------------------
if [ $ACCOUNTABILITY_RULES -eq 1 ]; then
	bash $PROJECT_DIR/services/accountability-rules/register.sh
fi


# accountability-types
# -------------------------------------------------------------------------------------
if [ $ACCOUNTABILITY_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/accountability-types/register.sh
fi


# conversion-and-remaining-periods
# -------------------------------------------------------------------------------------
if [ $CONVERSION_AND_REMAINING_PERIODS -eq 1 ]; then
	bash $PROJECT_DIR/services/conversion-and-remaining-periods/register.sh
fi


# cover-types
# -------------------------------------------------------------------------------------
if [ $COVER_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/cover-types/register.sh
fi


# databases
# -------------------------------------------------------------------------------------
if [ $DATABASES -eq 1 ]; then
	bash $PROJECT_DIR/services/databases/register.sh
fi


# dates
# -------------------------------------------------------------------------------------
if [ $DATES -eq 1 ]; then
	bash $PROJECT_DIR/services/dates/register.sh
fi


# emission-types
# -------------------------------------------------------------------------------------
if [ $EMISSION_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/emission-types/register.sh
fi


# fluxes-to-reporting-variables
# -------------------------------------------------------------------------------------
if [ $FLUXES_TO_REPORTING_VARIABLES -eq 1 ]; then
	bash $PROJECT_DIR/services/fluxes-to-reporting-variables/register.sh
fi


# flux-reporting-results
# -------------------------------------------------------------------------------------
if [ $FLUX_REPORTING_RESULTS -eq 1 ]; then
	bash $PROJECT_DIR/services/flux-reporting-results/register.sh
fi


# flux-types
# -------------------------------------------------------------------------------------
if [ $FLUX_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/flux-types/register.sh
fi


# land-use-categories
# -------------------------------------------------------------------------------------
if [ $LAND_USE_CATEGORIES -eq 1 ]; then
	bash $PROJECT_DIR/services/land-use-categories/register.sh
fi


# land-uses-flux-types
# -------------------------------------------------------------------------------------
if [ $LAND_USES_FLUX_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/land-uses-flux-types/register.sh
fi


# land-uses-flux-types-to-reporting-tables
# -------------------------------------------------------------------------------------
if [ $LAND_USES_FLUX_TYPES_TO_REPORTING_TABLES -eq 1 ]; then
	bash $PROJECT_DIR/services/land-uses-flux-types-to-reporting-tables/register.sh
fi


# locations
# -------------------------------------------------------------------------------------
if [ $LOCATIONS -eq 1 ]; then
	bash $PROJECT_DIR/services/locations/register.sh
fi


# parties
# -------------------------------------------------------------------------------------
if [ $PARTIES -eq 1 ]; then
	bash $PROJECT_DIR/services/parties/register.sh
fi


# party-types
# -------------------------------------------------------------------------------------
if [ $PARTY_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/party-types/register.sh
fi


# pools
# -------------------------------------------------------------------------------------
if [ $POOLS -eq 1 ]; then
	bash $PROJECT_DIR/services/pools/register.sh
fi


# quantity-observations
# -------------------------------------------------------------------------------------
if [ $QUANTITY_OBSERVATIONS -eq 1 ]; then
	bash $PROJECT_DIR/services/quantity-observations/register.sh
fi


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


# 
# vegetation-history-vegetation-types
# -------------------------------------------------------------------------------------
if [ $VEGETATION_HISTORY_VEGETATION_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/vegetation-history-vegetation-types/register.sh
fi


# vegetation-types
# -------------------------------------------------------------------------------------
if [ $VEGETATION_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/vegetation-types/register.sh
fi


echo
echo "---------------------------------------------------------------------------------"
echo "Leaving register-script"
echo "---------------------------------------------------------------------------------"
echo
