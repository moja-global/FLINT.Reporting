#!/bin/bash


echo
echo "---------------------------------------------------------------------------------"
echo "Uninstalling component"
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
# GET THE NAME OF THE APPLICATION
# -------------------------------------------------------------------------------------

echo
echo "Taking ${PWD##*/} as the name of the application"
APPLICATION=${PWD##*/}



# -------------------------------------------------------------------------------------
# UNINSTALL THE APPLICATION
# -------------------------------------------------------------------------------------

echo
echo "Uninstalling the application"
echo
helm uninstall $APPLICATION


echo
echo "---------------------------------------------------------------------------------"
echo "Done uninstalling component"
echo "---------------------------------------------------------------------------------"
