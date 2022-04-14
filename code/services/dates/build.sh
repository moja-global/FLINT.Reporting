#!/bin/bash

echo 
echo "========================================================================"
echo "Entering Artifact Build Script"
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
PROFILE=$(jq -r '.profile' $ROOT_DIR/configurations/configurations.json)
if [[ $PROFILE == null ]]
then
     echo 
     echo -e "${RED_COLOR}Build Profile Specification is missing${NO_COLOR}"
     echo 
     echo "------------------------------------------------------------------------"
     echo "Aborting Artifact Build"
     echo "------------------------------------------------------------------------"
     echo 
     exit 1
else
     echo -e "${GREEN_COLOR}PROFILE = ${PROFILE}${NO_COLOR}"
fi

echo 
API_SERVER=$(jq -r '.domains.api' $ROOT_DIR/configurations/configurations.json)
if [[ $API_SERVER == null ]]
then
     echo 
     echo -e "${RED_COLOR}API Server Configuration is Missing${NO_COLOR}"
     echo 
     echo "------------------------------------------------------------------------"
     echo "Aborting Artifact Build"
     echo "------------------------------------------------------------------------"
     echo 
     exit 1
else
     echo -e "${GREEN_COLOR}API SERVER = ${API_SERVER} ${NO_COLOR}"
fi

echo 
cd $BASEDIR
ARTIFACT="dates"
if [[ $ARTIFACT == null ]]
then
     echo 
     echo -e "${RED_COLOR}Artifact's Configuration is Missing${NO_COLOR}"
     echo 
     echo "------------------------------------------------------------------------"
     echo "Aborting Artifact Build"
     echo "------------------------------------------------------------------------"echo 
     echo 
     exit 1
else
     echo -e "${GREEN_COLOR}ARTIFACT = ${ARTIFACT} ${NO_COLOR}"
fi


# ------------------------------------------------------------------------
# UPDATE API HOST
# ------------------------------------------------------------------------

echo 
echo "Updating API Server Host value"
sed -i 's/cloud\.miles\.co\.ke/'${API_SERVER}'/g' $BASEDIR/chart/values.yaml


# ------------------------------------------------------------------------
# BUILD ARTIFACT
# ------------------------------------------------------------------------

echo 
echo "Building artifact"
echo 
bash mvnw clean package spring-boot:repackage -P ${PROFILE}
echo

echo 
echo "========================================================================"
echo "Leaving Artifact Build Script"
echo "========================================================================"
echo 
