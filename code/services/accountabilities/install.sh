#!/bin/bash

echo "[INSTALLATION]"
echo "[INSTALLATION] ========================================================================"
echo "[INSTALLATION] Entering Artifact Installation Script"
echo "[INSTALLATION] ========================================================================"

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

echo "[INSTALLATION]"
ARTIFACT="$(mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout)"
if [[ $ARTIFACT == null ]]
then
     echo "[INSTALLATION]"
     echo -n "[INSTALLATION] ";  echo -e "${RED_COLOR}Artifact's Configuration is Missing${NO_COLOR}"
     echo "[INSTALLATION]"
     echo "[INSTALLATION] ------------------------------------------------------------------------"
     echo "[INSTALLATION] Aborting Artifact Install"
     echo "[INSTALLATION] ------------------------------------------------------------------------"echo "[INSTALLATION]"
     echo "[INSTALLATION]"
     exit 1
else
     echo -n "[INSTALLATION] ";  echo -e "${GREEN_COLOR}ARTIFACT = ${ARTIFACT} ${NO_COLOR}"
fi


# ------------------------------------------------------------------------
# INSTALL ARTIFACT
# ------------------------------------------------------------------------

echo "[INSTALLATION]"
echo "[INSTALLATION] Installing artifact"
echo "[INSTALLATION]"
echo "[INSTALLATION] ------------------------------------------------------------------------"
echo "[INSTALLATION]"
helm install $ARTIFACT $BASEDIR/chart
echo "[INSTALLATION]"
echo "[INSTALLATION] ------------------------------------------------------------------------"

echo "[INSTALLATION]"
echo "[INSTALLATION] ========================================================================"
echo "[INSTALLATION] Leaving Artifact Installation Script"
echo "[INSTALLATION] ========================================================================"
echo "[INSTALLATION]"
