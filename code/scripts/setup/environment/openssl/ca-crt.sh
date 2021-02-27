#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Generate CA Certificate"
echo "---------------------------------------------------------------------------------"

echo
echo "Changing the working directory to the / directory"
cd /

echo
echo "Generating the CA's certificate in the / directory"
sudo openssl req -x509 -new -nodes -key ca.key -days 10000 -out ca.crt

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Generate CA Certificate"
echo "---------------------------------------------------------------------------------"
