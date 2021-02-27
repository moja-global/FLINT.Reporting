#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering Register Client Script"
echo "---------------------------------------------------------------------------------"
echo

echo
echo "Setting Up Resource Paths"
echo

# root/project/scripts/setup/system/client
CLIENT_DIR="$(cd "$(dirname "$0")" && pwd)"

# root/project/scripts/setup/system
SYSTEM_DIR="$(dirname "$CLIENT_DIR")"

# root/project/scripts/setup
SETUP_DIR="$(dirname "$SYSTEM_DIR")"

# root/project/scripts
SCRIPTS_DIR="$(dirname "$SETUP_DIR")"

# root/project
PROJECT_DIR="$(dirname "$SCRIPTS_DIR")"

echo
echo "Registering Client"
echo

bash $PROJECT_DIR/client/register.sh

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving Register Client Script"
echo "---------------------------------------------------------------------------------"
echo
