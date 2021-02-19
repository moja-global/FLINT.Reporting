#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Install RabbitMQ"
echo "---------------------------------------------------------------------------------"

# Directories
RABBITMQ_DIR="$( cd "$( dirname "$0" )" && pwd )"
ADDONS_DIR="$(dirname "$RABBITMQ_DIR")"
CLUSTER_DIR="$(dirname "$ADDONS_DIR")"
KUBERNETES_DIR="$(dirname "$CLUSTER_DIR")"
ENVIRONMENT_DIR="$(dirname "$KUBERNETES_DIR")"
SETUP_DIR="$(dirname "$ENVIRONMENT_DIR")"
SCRIPTS_DIR="$(dirname "$SETUP_DIR")"
ROOT_DIR="$(dirname "$SCRIPTS_DIR")"


RABBITMQ_SERVER=$(jq -r '.domains.rabbitMQ' $ROOT_DIR/configurations/configurations.json)
if [[ $RABBITMQ_SERVER == null ]]
then
     echo
     echo -e "${RED_COLOR}RabbitMQ Server Configuration is Missing${NO_COLOR}"
     echo
     echo "---------------------------------"
     echo "Halting Build"
     echo "---------------------------------"
     exit 1
else
     echo
     echo -e "${GREEN_COLOR}RabbitMQ Server Configuration Found${NO_COLOR}"
fi


# Add Bitnami to the Helm Repositories
helm repo add bitnami https://charts.bitnami.com/bitnami

# Update the RabbitMQ Host Name
sed -i -e 's/hostname: .*/hostname: '${RABBITMQ_SERVER}'/g' $RABBITMQ_DIR/values.yaml

# Install Bitnami's PostgreSQL
helm install bitnami-rabbitmq -f $RABBITMQ_DIR/values.yaml bitnami/rabbitmq

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Install RabbitMQ"
echo "---------------------------------------------------------------------------------"

