#!/bin/bash

echo "[UNINSTALLATION]"
echo "[UNINSTALLATION] ========================================================================"
echo "[UNINSTALLATION] Entering Artifact Uninstallation Script"
echo "[UNINSTALLATION] ========================================================================"

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

echo "[UNINSTALLATION]"
ARTIFACT="$(mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout)"
if [[ $ARTIFACT == null ]]
then
     echo "[UNINSTALLATION]"
     echo -n "[UNINSTALLATION] ";  echo -e "${RED_COLOR}Artifact's Configuration is Missing${NO_COLOR}"
     echo "[UNINSTALLATION]"
     echo "[UNINSTALLATION] ------------------------------------------------------------------------"
     echo "[UNINSTALLATION] Aborting Artifact Uninstallation"
     echo "[UNINSTALLATION] ------------------------------------------------------------------------"echo "[UNINSTALLATION]"
     echo "[UNINSTALLATION]"
     exit 1
else
     echo -n "[UNINSTALLATION] ";  echo -e "${GREEN_COLOR}ARTIFACT = ${ARTIFACT} ${NO_COLOR}"
fi

# ------------------------------------------------------------------------
# SET WORKING DIRECTORY
# ------------------------------------------------------------------------

cd $BASEDIR

# ------------------------------------------------------------------------
# UNINSTALL ARTIFACT
# ------------------------------------------------------------------------

echo "[UNINSTALLATION]"
echo "[UNINSTALLATION] Uninstalling artifact"
echo "[UNINSTALLATION]"
echo "[UNINSTALLATION] ------------------------------------------------------------------------"
echo "[UNINSTALLATION]"
helm uninstall ARTIFACT
echo "[UNINSTALLATION]"
echo "[UNINSTALLATION] ------------------------------------------------------------------------"

echo "[UNINSTALLATION]"
echo "[UNINSTALLATION] ========================================================================"
echo "[UNINSTALLATION] Leaving Artifact Uninstallation Script"
echo "[UNINSTALLATION] ========================================================================"
echo "[UNINSTALLATION]"