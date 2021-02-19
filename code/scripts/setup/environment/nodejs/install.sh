
#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Install NodeJS"
echo "---------------------------------------------------------------------------------"


echo
echo "Updating local package index"
sudo apt update


echo
echo "Installing the default NodeJS version"
sudo apt install -y nodejs


echo
echo "Installing the build-essential package"
sudo apt install -y build-essential


echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Install NodeJS"
echo "---------------------------------------------------------------------------------"

