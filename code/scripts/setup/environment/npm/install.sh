#!/bin/bash
echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Install npm"
echo "---------------------------------------------------------------------------------"

echo
echo "Updating the local package index"
sudo apt update

echo
echo "Installing the default npm"
sudo apt install -y npm

echo
echo "Installing the build-essential package in order for some npm packages to work"
sudo apt install -y build-essential

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Install npm"
echo "---------------------------------------------------------------------------------"

