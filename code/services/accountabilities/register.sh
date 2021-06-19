#!/bin/bash
# See: https://www.theunixschool.com/2012/11/howto-retrieve-extract-tag-value-xml-linux.html
# See: https://stackoverflow.com/questions/45259031/how-to-get-first-match-with-sed


echo
echo "---------------------------------------------------------------------------------"
echo "Entering Artifact Registration Script"
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

echo
REPOSITORY="$(sed -n '/docker\.image\.prefix/{s/.*<docker\.image\.prefix>\(.*\)<\/docker\.image\.prefix>.*/\1/;p}' pom.xml  | head -1)"
echo "REPOSITORY = ${REPOSITORY}"

echo
ARTIFACT="$(mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout)"
echo "ARTIFACT = ${ARTIFACT}"

echo
VERSION="$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)"
echo "VERSION = ${VERSION}"



# -------------------------------------------------------------------------------------
# BUILD DOCKER IMAGE
# -------------------------------------------------------------------------------------

echo
echo "Building Docker Image"
echo
docker image build .


# -------------------------------------------------------------------------------------
# PUSH DOCKER IMAGE
# -------------------------------------------------------------------------------------

echo
#echo "Pushing Docker Image"
echo
docker image push ${REPOSITORY}/${ARTIFACT}:${VERSION}


echo
echo "---------------------------------------------------------------------------------"
echo "Leaving Artifact Registration Script"
echo "---------------------------------------------------------------------------------"
