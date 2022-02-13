#!/bin/bash

echo 
echo "========================================================================"
echo "Entering Artifact Registration Script"
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
CODE_DIR="$(dirname "$BASEDIR")"
ROOT_DIR="$(dirname "$CODE_DIR")"


# ------------------------------------------------------------------------
# SET DEFAULT WORKING DIRECTORY
# ------------------------------------------------------------------------

cd $BASEDIR


# ------------------------------------------------------------------------
# INITIALIZE SERVER / ARTIFACTS VARIABLES
# ------------------------------------------------------------------------

echo 
REGISTRY_SERVER=$(jq -r '.domains.registry' $ROOT_DIR/code/configurations/configurations.json)
if [[ $REGISTRY_SERVER == null ]]
then
     echo 
     echo -e "${RED_COLOR}Registry Server's Configuration is Missing${NO_COLOR}"
     echo 
     echo "------------------------------------------------------------------------"
     echo "Aborting Artifact Registration"
     echo "------------------------------------------------------------------------"
     echo 

     exit 1
else
     echo -e "${GREEN_COLOR}REGISTRY = ${REGISTRY_SERVER} ${NO_COLOR}"
fi



# -------------------------------------------------------------------------------------
# BUILD, TAG & PUSH LATEST IMAGE
# -------------------------------------------------------------------------------------

#echo
#echo "Building, tagging & pushing latest image"
#echo
docker build -t ${REGISTRY_SERVER}:5043/client:latest $BASEDIR
docker push ${REGISTRY_SERVER}:5043/client:latest


echo 
echo "========================================================================"
echo "Leaving Artifact Registration Script"
echo "========================================================================"
