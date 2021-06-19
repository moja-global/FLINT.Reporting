#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering Artifact Build Script"
echo "---------------------------------------------------------------------------------"


# -------------------------------------------------------------------------------------
# INITIALIZE SHELL COLOR VARIABLES
# -------------------------------------------------------------------------------------

RED_COLOR='\033[0;31m'
GREEN_COLOR='\033[0;32m'
NO_COLOR='\033[0m'


# -------------------------------------------------------------------------------------
# INITIALIZE PATH VARIABLES
# -------------------------------------------------------------------------------------

BASEDIR="$( cd "$( dirname "$0" )" && pwd )"
LIB_DIR="$(dirname "$BASEDIR")"
ROOT_DIR="$(dirname "$LIB_DIR")"


# -------------------------------------------------------------------------------------
# INITIALIZE / VALIDATE SERVER VARIABLES
# -------------------------------------------------------------------------------------

PROFILE=$(jq -r '.profile' $ROOT_DIR/configurations/configurations.json)
if [[ $PROFILE == null ]]
then
     echo
     echo -e "${RED_COLOR}Build Environment Specification is missing${NO_COLOR}"
     echo
     echo "---------------------------------"
     echo "Halting Build"
     echo "---------------------------------"
     exit 1
else
     echo
     echo -e "${GREEN_COLOR}Build Environment Specification Found${NO_COLOR}"
fi


API_SERVER=$(jq -r '.domains.api' $ROOT_DIR/configurations/configurations.json)
if [[ $API_SERVER == null ]]
then
     echo
     echo -e "${RED_COLOR}API Server Configuration is Missing${NO_COLOR}"
     echo
     echo "---------------------------------"
     echo "Halting Build"
     echo "---------------------------------"
     exit 1
else
     echo
     echo -e "${GREEN_COLOR}API Server Configuration Found${NO_COLOR}"
fi


REGISTRY_SERVER=$(jq -r '.domains.registry' $ROOT_DIR/configurations/configurations.json)
if [[ $REGISTRY_SERVER == null ]]
then
     echo
     echo -e "${RED_COLOR}Registry Server Configuration is Missing${NO_COLOR}"
     echo
     echo "---------------------------------"
     echo "Halting Build"
     echo "---------------------------------"
     exit 1
else
     echo
     echo -e "${GREEN_COLOR}Registry Server Configuration Found${NO_COLOR}"
fi


# -------------------------------------------------------------------------------------
# SET WORKING DIRECTORY
# -------------------------------------------------------------------------------------

echo
echo "Changing working directory"
cd $BASEDIR


# -------------------------------------------------------------------------------------
# UPDATE API HOST
# -------------------------------------------------------------------------------------

echo
echo "Updating API Server host value"
sed -i 's/cloud\.miles\.co\.ke/'${API_SERVER}'/g' $BASEDIR/chart/values.yaml


# -------------------------------------------------------------------------------------
# UPDATE DOCKER HOST
# -------------------------------------------------------------------------------------

echo
echo "Updating Docker Registry host value"
sed -i 's/cloud\.miles\.co\.ke/'${REGISTRY_SERVER}'/g' $BASEDIR/pom.xml


# -------------------------------------------------------------------------------------
# BUILD & INSTALL COMPONENT IN MAVEN REPOSITORY
# -------------------------------------------------------------------------------------

echo
echo "Building & installing components into the Maven repository"
echo
bash mvnw clean package spring-boot:repackage -P ${PROFILE}

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving Artifact Build Script"
echo "---------------------------------------------------------------------------------"
