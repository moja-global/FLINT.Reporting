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
echo "Changing working directory"
cd $BASEDIR



# -------------------------------------------------------------------------------------
# BUILD, TAG & PUSH VERSIONED IMAGE
# -------------------------------------------------------------------------------------

echo
echo "Building, tagging & pushing versioned image"
echo
bash mvnw dockerfile:build@tag-version
bash mvnw dockerfile:tag@tag-version
bash mvnw dockerfile:push@tag-version



# -------------------------------------------------------------------------------------
# BUILD, TAG & PUSH LATEST IMAGE
# -------------------------------------------------------------------------------------

#echo
#echo "Building, tagging & pushing latest image"
#echo
#bash mvnw dockerfile:build@tag-latest
#bash mvnw dockerfile:tag@tag-latest
#bash mvnw dockerfile:push@tag-latest



echo
echo "---------------------------------------------------------------------------------"
echo "Done registering component"
echo "---------------------------------------------------------------------------------"
