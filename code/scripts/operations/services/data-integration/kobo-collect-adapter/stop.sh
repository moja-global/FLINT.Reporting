#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering operation-script"
echo "---------------------------------------------------------------------------------"
echo

echo
echo "Setting Up Resource Paths"
echo

# root/project/scripts/operations/services/data-integration/kobo-collect-adapter
KOBO_COLLECT_ADAPTER_DIR="$(cd "$(dirname "$0")" && pwd)"

# root/project/scripts/operations/services/data-integration
DATA_INTEGRATION_DIR="$(dirname "$KOBO_COLLECT_ADAPTER_DIR")"

# root/project/scripts/operations/services
SERVICES_DIR="$(dirname "$DATA_INTEGRATION_DIR")"

# root/project/scripts/operations
OPERATIONS_DIR="$(dirname "$SERVICES_DIR")"

# root/project/scripts/
SCRIPTS_DIR="$(dirname "$OPERATIONS_DIR")"

# root/project/
PROJECT_DIR="$(dirname "$SCRIPTS_DIR")"

echo
echo "Starting Operation"
echo

# Kobo Collect Adapter
# -------------------------------------------------------------------------------------
bash $PROJECT_DIR/library/kobo-collect-adapter/uninstall.sh

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving operation-script"
echo "---------------------------------------------------------------------------------"
echo
