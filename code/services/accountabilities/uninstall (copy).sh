#!/bin/bash

echo 
echo "========================================================================"
echo "Entering Artifact Uninstallation Script"
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
ARTIFACT="$(mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout)"
if [[ $ARTIFACT == null ]]
then
     echo 
     echo -e "${RED_COLOR}Artifact's Configuration is Missing${NO_COLOR}"
     echo 
     echo "------------------------------------------------------------------------"
     echo "Aborting Artifact Uninstallation"
     echo "------------------------------------------------------------------------"echo 
     echo 
     exit 1
else
     echo -e "${GREEN_COLOR}ARTIFACT = ${ARTIFACT} ${NO_COLOR}"
fi

# ------------------------------------------------------------------------
# UNINSTALL ARTIFACT
# ------------------------------------------------------------------------

echo 
echo "Uninstalling artifact"
echo 
echo "------------------------------------------------------------------------"
echo 
helm uninstall $ARTIFACT
echo 
echo "------------------------------------------------------------------------"

echo 
echo "========================================================================"
echo "Leaving Artifact Uninstallation Script"
echo "========================================================================"
echo 