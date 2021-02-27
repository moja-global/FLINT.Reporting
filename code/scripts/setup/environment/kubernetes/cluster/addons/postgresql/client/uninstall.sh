#!/bin/bash


echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Uninstalling PostgreSQL Client"
echo "---------------------------------------------------------------------------------"

# Uninstall PostgreSQL Client
sudo apt remove -y postgresql-client-common
sudo apt remove -y postgresql-client

# Remove any unused packages
sudo apt autoremove -y

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Uninstalling PostgreSQL Client"
echo "---------------------------------------------------------------------------------"


