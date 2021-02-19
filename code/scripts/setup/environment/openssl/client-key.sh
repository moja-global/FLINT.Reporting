#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Generate Client Key"
echo "---------------------------------------------------------------------------------"

echo
echo "Changing the working directory to /"
cd /

echo
echo "Generating the client key"
sudo openssl genrsa -out client.key 2048

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Generate Client Key"
echo "---------------------------------------------------------------------------------"







