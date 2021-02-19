#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Uninstall JDK"
echo "---------------------------------------------------------------------------------"

echo
echo "Completely removing OpenJDK from Ubuntu"
sudo apt-get purge -y openjdk-\* icedtea-\* icedtea6-\*

echo
echo "Removing java environmental variables settings"
sudo sed -i '/JAVA_HOME=/d' /etc/environment

echo
echo "Reloading the system environmental variables without rebooting"
set -a; source /etc/environment; set +a;

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Uninstall JDK"
echo "---------------------------------------------------------------------------------"

