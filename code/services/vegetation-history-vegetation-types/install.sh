#!/bin/bash

echo 
echo "========================================================================"
echo "Entering Artifact Installation Script"
echo "========================================================================"

# ------------------------------------------------------------------------
# INITIALIZE SHELL COLOR VARIABLES
# ------------------------------------------------------------------------

RED_COLOR='\033[0;31m'
GREEN_COLOR='\033[0;32m'
NO_COLOR='\033[0m'

# ------------------------------------------------------------------------
# INITIALIZE PATH VARIABLES
# ------------------------------------------------------------------------

BASEDIR="$( cd "$( dirname "$0" )" && pwd )"
LIB_DIR="$(dirname "$BASEDIR")"
ROOT_DIR="$(dirname "$LIB_DIR")"


# ------------------------------------------------------------------------
# SET DEFAULT WORKING DIRECTORY
# ------------------------------------------------------------------------

cd $BASEDIR


# ------------------------------------------------------------------------
# INITIALIZE SERVER / ARTIFACTS VARIABLES
# ------------------------------------------------------------------------

echo 
ARTIFACT="vegetation-history-vegetation-types"
if [[ $ARTIFACT == null ]]
then
     echo 
     echo -e "${RED_COLOR}Artifact's Configuration is Missing${NO_COLOR}"
     
     echo 
     echo "------------------------------------------------------------------------"
     echo "Aborting Artifact Installation"
     echo "------------------------------------------------------------------------"
     echo 
     exit 1
else
     echo -e "${GREEN_COLOR}ARTIFACT = ${ARTIFACT} ${NO_COLOR}"
fi

# ------------------------------------------------------------------------
# UNINSTALL ARTIFACT
# ------------------------------------------------------------------------

echo 
helm install $ARTIFACT $BASEDIR/chart

echo 
echo "========================================================================"
echo "Leaving Artifact Installation Script"
echo "========================================================================"
echo 