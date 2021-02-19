#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Uninstall Angular"
echo "---------------------------------------------------------------------------------"

echo
echo "Uninstall Angular globally"
sudo npm uninstall -y -g @angular/cli

echo
echo "Clearing npm cache"
sudo npm cache clean

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Uninstall Angular"
echo "---------------------------------------------------------------------------------"

