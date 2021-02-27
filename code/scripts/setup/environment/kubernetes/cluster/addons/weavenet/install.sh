#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Install Weavenet"
echo "---------------------------------------------------------------------------------"

#IPALLOC_RANGE=10.32.0.0/12
IPALLOC_RANGE=172.30.0.0/16

echo
kubectl apply -f "https://cloud.weave.works/k8s/net?k8s-version=$(kubectl version | base64 | tr -d '\n')&env.IPALLOC_RANGE=${IPALLOC_RANGE}"

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Install Weavenet"
echo "---------------------------------------------------------------------------------"
