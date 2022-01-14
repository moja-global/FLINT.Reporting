#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Uninstall NodeJS"
echo "---------------------------------------------------------------------------------"


echo
echo "Removing NodeJS"
sudo apt remove -y nodejs


echo
echo "Removing build-essential packages"
sudo apt remove -y build-essential


echo
echo "Removing any other unused packages"
sudo apt autoremove -y


echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Uninstall NodeJS"
echo "---------------------------------------------------------------------------------"

