#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Delete Private Docker Registry Secret"
echo "---------------------------------------------------------------------------------"

echo
kubectl delete secret regcred

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Delete Private Docker Registry Secret"
echo "---------------------------------------------------------------------------------"


