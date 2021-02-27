#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Uninstall npm"
echo "---------------------------------------------------------------------------------"

echo
echo "Removing the distro-stable npm version"
sudo apt remove -y npm

echo
echo "Removing the build-essential packages"
sudo apt remove -y build-essential

echo "Removing any unused packages"
sudo apt autoremove -y

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Uninstall npm"
echo "---------------------------------------------------------------------------------"
