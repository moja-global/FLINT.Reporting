#!/bin/bash

echo 
echo "************************************************************************"
echo " Entering Stop Data Integration Script"
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

MICROSERVICES=("data-processor")



# ------------------------------------------------------------------------
# UNINSTALL MICROSERVICES
# ------------------------------------------------------------------------

for ((i=${#MICROSERVICES[@]}-1; i>=0; i--)); do
	bash $PROJECT_DIR/services/${MICROSERVICES[$i]}/uninstall.sh
done



echo 
echo "************************************************************************"
echo " Leaving Stop Data Integration Script"
echo "************************************************************************"
echo 
