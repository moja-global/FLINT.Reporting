#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Uninstall nano"
echo "---------------------------------------------------------------------------------"

echo
echo "Removing the installed nano"
sudo apt remove -y nano

echo "Removing any unused packages"
sudo apt autoremove -y

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Uninstall nano"
echo "---------------------------------------------------------------------------------"
