#!/bin/bash


echo
echo "---------------------------------------------------------------------------------"
echo "Registering component"
echo "---------------------------------------------------------------------------------"



# -------------------------------------------------------------------------------------
# INITIALIZE VARIABLES
# -------------------------------------------------------------------------------------

echo
echo "Initializing variables"
BASEDIR="$( cd "$( dirname "$0" )" && pwd )"



# -------------------------------------------------------------------------------------
# SET WORKING DIRECTORY
# -------------------------------------------------------------------------------------

echo
echo "Setting the working directory"
cd $BASEDIR



# -------------------------------------------------------------------------------------
# BUILD, TAG & PUSH VERSIONED IMAGE
# -------------------------------------------------------------------------------------

echo
echo "Building, tagging & pushing versioned image"
echo
docker build -t reporter.miles.co.ke:5043/client:0.0.1 $BASEDIR
docker push reporter.miles.co.ke:5043/client:0.0.1



# -------------------------------------------------------------------------------------
# BUILD, TAG & PUSH LATEST IMAGE
# -------------------------------------------------------------------------------------

#echo
#echo "Building, tagging & pushing latest image"
#echo
#docker build -t ${REGISTRY_SERVER_HOST_NAME}:5043/client:latest $BASEDIR
#docker push ${REGISTRY_SERVER_HOST_NAME}:5043/client:latest



echo
echo "---------------------------------------------------------------------------------"
echo "Done registering component"
echo "---------------------------------------------------------------------------------"
