#!/bin/bash

echo "[REGISTRATION]"
echo "[REGISTRATION] ========================================================================"
echo "[REGISTRATION] Entering Artifact Registration Script"
echo "[REGISTRATION] ========================================================================"

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

echo "[REGISTRATION]"
REGISTRY_SERVER=$(jq -r '.domains.registry' $ROOT_DIR/configurations/configurations.json)
if [[ $REGISTRY_SERVER == null ]]
then
     echo "[REGISTRATION]"
     echo -n "[REGISTRATION] "; echo -e "${RED_COLOR}Registry Server's Configuration is Missing${NO_COLOR}"
     echo "[REGISTRATION]"
     echo "[REGISTRATION] ------------------------------------------------------------------------"
     echo "[REGISTRATION] Aborting Artifact Registration"
     echo "[REGISTRATION] ------------------------------------------------------------------------"
     echo "[REGISTRATION]"
     
     exit 1
else
     echo -n "[REGISTRATION] "; echo -e "${GREEN_COLOR}REGISTRY = ${REGISTRY_SERVER} ${NO_COLOR}"
fi

echo "[REGISTRATION]"
ARTIFACT="$(mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout)"
if [[ $ARTIFACT == null ]]
then
     echo "[REGISTRATION]"
     echo -n "[REGISTRATION] "; echo -e "${RED_COLOR}Artifact's Configuration is Missing${NO_COLOR}"
     echo "[REGISTRATION]"
     echo "[REGISTRATION] ------------------------------------------------------------------------"
     echo "[REGISTRATION] Aborting Artifact Registration"
     echo "[REGISTRATION] ------------------------------------------------------------------------"
     echo "[REGISTRATION]"
     exit 1
else
     echo -n "[REGISTRATION] "; echo -e "${GREEN_COLOR}ARTIFACT = ${ARTIFACT} ${NO_COLOR}"
fi

echo "[REGISTRATION]"
VERSION="$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)"
if [[ $VERSION == null ]]
then
     echo "[REGISTRATION]"
     echo -n "[REGISTRATION] "; echo -e "${RED_COLOR}Version's Configuration is Missing${NO_COLOR}"
     echo "[REGISTRATION]"
     echo "[REGISTRATION] ---------------------------------"
     echo "[REGISTRATION] Aborting Artifact Registration"
     echo "[REGISTRATION] ---------------------------------"
     echo "[REGISTRATION]"
     exit 1
else
     echo -n "[REGISTRATION] "; echo -e "${GREEN_COLOR}VERSION = ${VERSION} ${NO_COLOR}"
fi


# ------------------------------------------------------------------------
# BUILD DOCKER IMAGE
# ------------------------------------------------------------------------

echo "[REGISTRATION]"
echo "[REGISTRATION] Building Docker Image"
echo "[REGISTRATION]"
echo "[REGISTRATION] ------------------------------------------------------------------------"
echo "[REGISTRATION]"
# See: https://stackoverflow.com/questions/51115856/docker-failed-to-export-image-failed-to-create-image-failed-to-get-layer
DOCKER_BUILDKIT=1 docker build -t ${REGISTRY_SERVER}:5043/${ARTIFACT}:${VERSION} .
echo "[REGISTRATION]"
echo "[REGISTRATION] ------------------------------------------------------------------------"

# ------------------------------------------------------------------------
# PUSH DOCKER IMAGE
# ------------------------------------------------------------------------

echo "[REGISTRATION]"
echo "[REGISTRATION] Registering Docker Image"
echo "[REGISTRATION]"
echo "[REGISTRATION] ------------------------------------------------------------------------"
echo "[REGISTRATION]"
docker image push ${REGISTRY_SERVER}:5043/${ARTIFACT}:${VERSION}
echo "[REGISTRATION]"
echo "[REGISTRATION] ------------------------------------------------------------------------"

echo "[REGISTRATION]"
echo "[REGISTRATION] ========================================================================"
echo "[REGISTRATION] Leaving Artifact Registration Script"
echo "[REGISTRATION] ========================================================================"
echo "[REGISTRATION]"
