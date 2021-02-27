#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Install Docker Compose"
echo "---------------------------------------------------------------------------------"

VERSION=1.27.4

echo
echo "Downloading and adding Docker Compose to /usr/local/bin/"
sudo curl -L https://github.com/docker/compose/releases/download/${VERSION}/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
echo

echo
echo "Updating Docker Compose permissions"
sudo chmod +x /usr/local/bin/docker-compose
echo

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Install Docker Compose"
echo "---------------------------------------------------------------------------------"


