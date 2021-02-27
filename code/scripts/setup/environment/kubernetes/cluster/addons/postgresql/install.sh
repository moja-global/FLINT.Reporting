#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Install PostgreSQL"
echo "---------------------------------------------------------------------------------"

# Capture the directory within which this script is located as the base directory
BASEDIR="$( cd "$( dirname "$0" )" && pwd )"

# Add Bitnami to the Helm Repositories
helm repo add bitnami https://charts.bitnami.com/bitnami

# Install Bitnami's PostgreSQL
helm install bitnami-postgres -f $BASEDIR/values.yaml bitnami/postgresql

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Install PostgreSQL"
echo "---------------------------------------------------------------------------------"

