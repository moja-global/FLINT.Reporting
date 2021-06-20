#!/bin/bash

echo "[BUILD]"
echo "[BUILD] ========================================================================"
echo "[BUILD] Entering Artifact Build Script"
echo "[BUILD] ========================================================================"

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
# INITIALIZE SERVER / ARTIFACTS VARIABLES
# ------------------------------------------------------------------------

echo "[BUILD]"
PROFILE=$(jq -r '.profile' $ROOT_DIR/configurations/configurations.json)
if [[ $PROFILE == null ]]
then
     echo "[BUILD]"
     echo -n "[BUILD] ";  echo -e "${RED_COLOR}Build Profile Specification is missing${NO_COLOR}"
     echo "[BUILD]"
     echo "[BUILD] ------------------------------------------------------------------------"
     echo "[BUILD] Aborting Artifact Build"
     echo "[BUILD] ------------------------------------------------------------------------"
     echo "[BUILD]"
     exit 1
else
     echo -n "[BUILD] ";  echo -e "${GREEN_COLOR}PROFILE = ${PROFILE}${NO_COLOR}"
fi

echo "[BUILD]"
API_SERVER=$(jq -r '.domains.api' $ROOT_DIR/configurations/configurations.json)
if [[ $API_SERVER == null ]]
then
     echo "[BUILD]"
     echo -n "[BUILD] ";  echo -e "${RED_COLOR}API Server Configuration is Missing${NO_COLOR}"
     echo "[BUILD]"
     echo "[BUILD] ------------------------------------------------------------------------"
     echo "[BUILD] Aborting Artifact Build"
     echo "[BUILD] ------------------------------------------------------------------------"
     echo "[BUILD]"
     exit 1
else
     echo -n "[BUILD] ";  echo -e "${GREEN_COLOR}API SERVER = ${API_SERVER} ${NO_COLOR}"
fi

echo "[BUILD]"
ARTIFACT="$(mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout)"
if [[ $ARTIFACT == null ]]
then
     echo "[BUILD]"
     echo -n "[BUILD] ";  echo -e "${RED_COLOR}Artifact's Configuration is Missing${NO_COLOR}"
     echo "[BUILD]"
     echo "[BUILD] ------------------------------------------------------------------------"
     echo "[BUILD] Aborting Artifact Build"
     echo "[BUILD] ------------------------------------------------------------------------"echo "[BUILD]"
     echo "[BUILD]"
     exit 1
else
     echo -n "[BUILD] ";  echo -e "${GREEN_COLOR}ARTIFACT = ${ARTIFACT} ${NO_COLOR}"
fi

# ------------------------------------------------------------------------
# SET WORKING DIRECTORY
# ------------------------------------------------------------------------

cd $BASEDIR

# ------------------------------------------------------------------------
# UPDATE API HOST
# ------------------------------------------------------------------------

echo "[BUILD]"
echo "[BUILD] Updating API Server Host value"
sed -i 's/cloud\.miles\.co\.ke/'${API_SERVER}'/g' $BASEDIR/chart/values.yaml

# ------------------------------------------------------------------------
# BUILD ARTIFACT
# ------------------------------------------------------------------------

echo "[BUILD]"
echo "[BUILD] Building artifact"
echo "[BUILD]"
echo "[BUILD] ------------------------------------------------------------------------"
echo "[BUILD]"
bash mvnw clean package spring-boot:repackage -P ${PROFILE}
echo "[BUILD]"
echo "[BUILD] ------------------------------------------------------------------------"

echo "[BUILD]"
echo "[BUILD] ========================================================================"
echo "[BUILD] Leaving Artifact Build Script"
echo "[BUILD] ========================================================================"
echo "[BUILD]"
