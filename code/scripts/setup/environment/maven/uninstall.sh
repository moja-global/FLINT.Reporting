#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Uninstall Maven"
echo "---------------------------------------------------------------------------------"

echo
echo "Removing maven"
sudo rm -rf /opt/maven

echo
echo "Removing Maven environmental variables"
sudo rm /etc/profile.d/mavenenv.sh

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Uninstall Maven"
echo "---------------------------------------------------------------------------------"

