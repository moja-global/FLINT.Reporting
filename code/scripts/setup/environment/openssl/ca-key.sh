#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Generate CA Key"
echo "---------------------------------------------------------------------------------"

echo
echo "Changing the working directory to the / directory"
cd /

echo
echo "Generating the CA's key in the / directory"
sudo openssl genrsa -out ca.key 2048

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Generate CA Key"
echo "---------------------------------------------------------------------------------"


