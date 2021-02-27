#!/bin/bash


echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Installing PostgreSQL Client"
echo "---------------------------------------------------------------------------------"



# Update the local package index
sudo apt update



# Install PostgreSQL Client
sudo apt install -y postgresql-client-common
sudo apt install -y postgresql-client


echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Installing PostgreSQL Client"
echo "---------------------------------------------------------------------------------"

