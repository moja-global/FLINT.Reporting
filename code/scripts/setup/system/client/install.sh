#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering Install Client Script"
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
echo "Installing Client"
echo

bash $PROJECT_DIR/client/install.sh

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving Install Client Script"
echo "---------------------------------------------------------------------------------"
echo
