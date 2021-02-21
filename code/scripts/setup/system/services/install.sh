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
echo "Building Microservices"
echo

# Use the flags 1 and 0 below to configure the services that you want to install
# 1 = on, 0 = off
# ----------------------------------------------------------------------------------
EMISSION_TYPES=1
FLUX_TYPES=1
UNFCCC_VARIABLES=1


# -------------------------------------------------------------------------------------
# FLINT OUTPUT RELATED
# -------------------------------------------------------------------------------------

# Flux Types
# -------------------------------------------------------------------------------------
if [ $FLUX_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/flux-types/install.sh
fi


# -------------------------------------------------------------------------------------
# REPORTING RELATED
# -------------------------------------------------------------------------------------

# Emission Types
# -------------------------------------------------------------------------------------
if [ $EMISSION_TYPES -eq 1 ]; then
	bash $PROJECT_DIR/services/emission-types/install.sh
fi


# UNFCCC Variables
# -------------------------------------------------------------------------------------
if [ $UNFCCC_VARIABLES -eq 1 ]; then
	bash $PROJECT_DIR/services/unfccc-variables/install.sh
fi


echo
echo "---------------------------------------------------------------------------------"
echo "Leaving install-script"
echo "---------------------------------------------------------------------------------"
echo
