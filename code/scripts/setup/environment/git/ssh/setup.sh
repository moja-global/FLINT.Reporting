#!/bin/bash

DEFAULT_FILE=${HOME}/.ssh/id_rsa

echo
echo "---------------------------------------------------------------------------------"
echo "Setting up SSH key"
echo "---------------------------------------------------------------------------------"
ssh-keygen 

echo
echo "Please enter the file in which the key was saved: default [$DEFAULT_FILE]"
read file
file=${file:=$DEFAULT_FILE}

echo
echo "Starting the SSH agent"
eval `ssh-agent`

echo
echo "Adding key file to SSH agent"
ssh-add -k ~/.ssh/${file}

echo
echo "---------------------------------------------------------------------------------"
echo "Done setting up SSH key"
echo "---------------------------------------------------------------------------------"

