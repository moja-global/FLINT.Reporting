#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Install Traefik"
echo "---------------------------------------------------------------------------------"



# -------------------------------------------------------------------------------------
# INITIALIZE VARIABLES
# -------------------------------------------------------------------------------------

echo
echo "Initializing variables"
BASEDIR="$( cd "$( dirname "$0" )" && pwd )"



# -------------------------------------------------------------------------------------
# ADD TRAEFIK TO THE HELM CHARTS REPO & UPDATE IT
# -------------------------------------------------------------------------------------

echo
echo "Adding Traefik to the Helm Charts Repo"
helm repo add traefik https://helm.traefik.io/traefik

echo
echo "Update the Helm Charts Repo"
helm repo update



# -------------------------------------------------------------------------------------
# INSTALL APPLICATION
# -------------------------------------------------------------------------------------

echo
echo "Installing Traefik"
helm install traefik -f $BASEDIR/values.yaml traefik/traefik


# -------------------------------------------------------------------------------------
# EXPOSE THE APPLICATION
# -------------------------------------------------------------------------------------

# Run the command below to expose the Traefik dashboard
# kubectl port-forward $(kubectl get pods --selector "app.kubernetes.io/name=traefik" --output=name) 9000:9000

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Install Traefik"
echo "---------------------------------------------------------------------------------"

