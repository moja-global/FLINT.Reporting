#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Install Helm"
echo "---------------------------------------------------------------------------------"


BASEDIR="$( cd "$( dirname "$0" )" && pwd )"

echo
echo "Grabbing the Helm installer script"
curl -fsSL -o $BASEDIR/get_helm.sh https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3

echo
echo "Adding execution rights to the script"
sudo chmod 700 $BASEDIR/get_helm.sh

echo
echo "Executing the script"
sudo bash $BASEDIR/get_helm.sh

echo
echo "Adding the public stable helm repo for installing the stable charts"
sudo bash helm repo add stable https://kubernetes-charts.storage.googleapis.com/

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Install Helm"
echo "---------------------------------------------------------------------------------"

