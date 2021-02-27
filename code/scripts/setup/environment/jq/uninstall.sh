#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Uninstall jq"
echo "---------------------------------------------------------------------------------"

echo
echo "Removing the installed jq"
sudo apt remove -y jq

echo "Removing any unused packages"
sudo apt autoremove -y

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Uninstall jq"
echo "---------------------------------------------------------------------------------"
