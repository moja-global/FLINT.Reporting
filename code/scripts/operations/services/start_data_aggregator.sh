#!/bin/bash

echo 
echo "************************************************************************"
echo " Entering Start Data Integration Script"
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

MICROSERVICES=("data-aggregator")



# ------------------------------------------------------------------------
# INSTALL MICROSERVICES
# ------------------------------------------------------------------------

for MICROSERVICE in "${MICROSERVICES[@]}"; do

	bash $CODE_DIR/services/$MICROSERVICE/install.sh

done



echo 
echo "************************************************************************"
echo " Leaving Start Data Integration Script"
echo "************************************************************************"
echo 
