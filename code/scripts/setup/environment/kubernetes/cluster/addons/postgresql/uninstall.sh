#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Uninstall PostgreSQL"
echo "---------------------------------------------------------------------------------"

# uninstall postgres
helm uninstall bitnami-postgres

# uninstall postgres persistent volume claims
kubectl delete pvc data-bitnami-postgres-postgresql-0

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Uninstall PostgreSQL"
echo "---------------------------------------------------------------------------------"

