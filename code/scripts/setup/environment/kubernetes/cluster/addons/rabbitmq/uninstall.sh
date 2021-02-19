#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Uninstall RabbitMQ"
echo "---------------------------------------------------------------------------------"

# uninstall rabbitmq
helm uninstall bitnami-rabbitmq

# uninstall rabbitmq persistent volume claims
kubectl delete pvc data-bitnami-rabbitmq-0

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Uninstall RabbitMQ"
echo "---------------------------------------------------------------------------------"

