#!/bin/bash


DEFAULT_FILE=${HOME}/.ssh/id_rsa

echo
echo "---------------------------------------------------------------------------------"
echo "Sharing SSH key"
echo "---------------------------------------------------------------------------------"

echo
echo "Please enter the file in which the key was saved: default [$DEFAULT_FILE]"
read file
file=${file:=$DEFAULT_FILE}

echo
echo "Starting the SSH agent"
eval `ssh-agent`

echo
echo "Copying the contents of the public key file to ssh-rsa.txt file"
cat ${file}.pub > ./ssh-rsa.txt

echo
echo "---------------------------------------------------------------------------------"
echo "Done sharing SSH key"
echo "---------------------------------------------------------------------------------"


