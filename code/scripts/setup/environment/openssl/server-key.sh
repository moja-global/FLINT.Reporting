#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Generate Server Key"
echo "---------------------------------------------------------------------------------"

echo
echo "Changing the working directory to the / directory"
cd /

echo
echo "Generating the server key in the / directory"
sudo openssl genrsa -out server.key 2048

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Generate Server Key"
echo "---------------------------------------------------------------------------------"









