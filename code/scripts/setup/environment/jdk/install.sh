#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Install JDK"
echo "---------------------------------------------------------------------------------"

JAVA_HOME="/usr/lib/jvm/default-java/bin"

echo
echo "Updating local package index"
sudo apt update

echo
echo "Installing the default JDK"
sudo apt install -y default-jdk

echo
echo "Adding JAVA_HOME to the environmental variables"
sudo sed -i '$ a JAVA_HOME="'${JAVA_HOME}'"' /etc/environment

echo
echo "Reloading the system environmental variables without rebooting"
set -a; . /etc/environment; set +a;

echo
echo "Setting java defaults to point at the default-java"
sudo update-alternatives --install "/usr/bin/java" "java" "${JAVA_HOME}/java" 1
echo

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Install JDK"
echo "---------------------------------------------------------------------------------"


