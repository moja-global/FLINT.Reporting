#!/bin/bash

echo 
echo "************************************************************************"
echo " Entering Services Installation Script"
echo "************************************************************************"
echo 

# ------------------------------------------------------------------------
# INITIALIZE SHELL COLOR VARIABLES
# ------------------------------------------------------------------------

RED_COLOR='\033[0;31m'
GREEN_COLOR='\033[0;32m'
NO_COLOR='\033[0m'

# ------------------------------------------------------------------------
# INITIALIZE PATH VARIABLES
# ------------------------------------------------------------------------


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


# ------------------------------------------------------------------------
# INITIALIZE MICROSERVICES ARRAY - IN DEPENDENCY-SENSITIVE ORDER
# ------------------------------------------------------------------------

MICROSERVICES=(
	"accountability-types"
	"cover-types"
	"databases"
	"emission-types"
	"flux-types"
	"party-types"
	"pools"
	"reporting-frameworks"
	"unit-categories"
	"dates"
	"vegetation-types"
	"vegetation-history-vegetation-types"
	"reporting-tables"
	"reporting-variables"
	"units"
	"accountability-rules"
	"parties"
	"accountabilities"
	"land-use-categories"
	"conversion-and-remaining-periods"
	"quantity-observations"
	"crf-tables"
	"locations"
	"flux-reporting-results"
	"fluxes-to-reporting-variables"
	"land-uses-flux-types"
	"land-uses-flux-types-to-reporting-tables"
	"tasks"
	"data-aggregator"
	"data-processor"
	"task-manager")



# ------------------------------------------------------------------------
# INSTALL MICROSERVICES
# ------------------------------------------------------------------------

for MICROSERVICE in "${MICROSERVICES[@]}"; do

	bash $PROJECT_DIR/services/$MICROSERVICE/install.sh

done



echo 
echo "************************************************************************"
echo " Leaving Services Installation Script"
echo "************************************************************************"
echo 
