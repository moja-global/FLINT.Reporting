#!/bin/bash

echo 
echo "************************************************************************"
echo " Entering Stop Services Script"
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


# root/code/scripts/operations/services
SERVICES_DIR="$(cd "$(dirname "$0")" && pwd)"

# root/code/scripts/operations
OPERATIONS_DIR="$(dirname "$SERVICES_DIR")"

# root/code/scripts
SCRIPTS_DIR="$(dirname "$OPERATIONS_DIR")"

# root/code/
CODE_DIR="$(dirname "$SCRIPTS_DIR")"


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
# UNINSTALL MICROSERVICES
# ------------------------------------------------------------------------

for ((i=${#MICROSERVICES[@]}-1; i>=0; i--)); do
	bash $CODE_DIR/services/${MICROSERVICES[$i]}/uninstall.sh
done



echo 
echo "************************************************************************"
echo " Leaving Stop Services Script"
echo "************************************************************************"
echo 
