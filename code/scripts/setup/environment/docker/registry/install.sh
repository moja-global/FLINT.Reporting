#!/bin/bash
# Note: The -r option in jq commands will output results with no json quotes


echo
echo "--------------------------------- "
echo "Entering - Install Docker Registry"
echo "--------------------------------- "


echo
echo "Initializing variables"


# Colors
RED_COLOR='\033[0;31m'
GREEN_COLOR='\033[0;32m'
NO_COLOR='\033[0m'


# Directories
REGISTRY_DIR="$( cd "$( dirname "$0" )" && pwd )"
DOCKER_DIR="$(dirname "$REGISTRY_DIR")"
ENVIRONMENT_DIR="$(dirname "$DOCKER_DIR")"
SETUP_DIR="$(dirname "$ENVIRONMENT_DIR")"
SCRIPTS_DIR="$(dirname "$SETUP_DIR")"
ROOT_DIR="$(dirname "$SCRIPTS_DIR")"


echo
echo "Checking if the ca certificate file has been correctly configured and exists"

# Read the file's configuration
CA_CERT_FILE=$(jq -r '.security.certificates.ca' $ROOT_DIR/configurations/configurations.json)

# Ascertain that the configuration is not null
if [[ $CA_CERT_FILE == null ]]
then
     echo
     echo -e "${RED_COLOR}CA Certificate File Configuration is Missing${NO_COLOR}"
     echo
     echo "---------------------------------"
     echo "Leaving - Install Docker Registry"
     echo "---------------------------------"
     exit 1
fi

# Ascertain that the file pointed to by the configuration exists
if [ ! -e $CA_CERT_FILE ] 
then
     echo
     echo -e "${RED_COLOR}CA Certificate File is Missing${NO_COLOR}"
     echo
     echo "---------------------------------"
     echo "Leaving - Install Docker Registry"
     echo "---------------------------------"
     exit 1
else
     echo
     echo -e "${GREEN_COLOR}CA Certificate File Found${NO_COLOR}"
fi


echo
echo "Checking if the server certificate file has been correctly configured and exists"

# Read the file's configuration
SERVER_CERT_FILE=$(jq -r '.security.certificates.server' $ROOT_DIR/configurations/configurations.json)

# Ascertain that the configuration is not null
if [[ $SERVER_CERT_FILE == null ]]
then
     echo
     echo -e "${RED_COLOR}Server Certificate File Configuration is Missing${NO_COLOR}"
     echo
     echo "---------------------------------"
     echo "Leaving - Install Docker Registry"
     echo "---------------------------------"
     exit 1
fi

# Ascertain that the file pointed to by the configuration exists
if [ ! -e $SERVER_CERT_FILE ] 
then
     echo
     echo -e "${RED_COLOR}Server Certificate File is Missing${NO_COLOR}"
     echo
     echo "---------------------------------"
     echo "Leaving - Install Docker Registry"
     echo "---------------------------------"
     exit 1
else
     echo
     echo -e "${GREEN_COLOR}Server Certificate File Found${NO_COLOR}"
fi


echo
echo "Checking if the server key file has been correctly configured and exists"

# Read the file's configuration
SERVER_KEY_FILE=$(jq -r '.security.keys.server' $ROOT_DIR/configurations/configurations.json)

# Ascertain that the configuration is not null
if [[ $SERVER_KEY_FILE == null ]]
then
     echo
     echo -e "${RED_COLOR}Server Key File Configuration is Missing${NO_COLOR}"
     echo
     echo "---------------------------------"
     echo "Leaving - Install Docker Registry"
     echo "---------------------------------"
     exit 1
fi

# Ascertain that the file pointed to by the configuration exists
if [ ! -e $SERVER_KEY_FILE ] 
then
     echo
     echo -e "${RED_COLOR}Server Key File is Missing${NO_COLOR}"
     echo
     echo "---------------------------------"
     echo "Leaving - Install Docker Registry"
     echo "---------------------------------"
     exit 1
else
     echo
     echo -e "${GREEN_COLOR}Server Key File Found${NO_COLOR}"
fi


echo
echo "Checking if the registry server has been correctly configured"

# Server Variables
REGISTRY_SERVER=$(jq '.domains.registry' $ROOT_DIR/configurations/configurations.json)

# Ascertain that the configuration is not null
if [[ $REGISTRY_SERVER == null ]]
then
     echo
     echo -e "${RED_COLOR}Registry Server Configuration is Missing${NO_COLOR}"
     echo
     echo "---------------------------------"
     echo "Leaving - Install Docker Registry"
     echo "---------------------------------"
     exit 1
else
     echo
     echo -e "${GREEN_COLOR}Registry Server Configuration Found${NO_COLOR}"
fi


echo
echo "Updating server name settings in /registry.conf"
sed -i -e 's/server_name.*/server_name '${REGISTRY_SERVER}';/g' $REGISTRY_DIR/registry.conf


echo
sudo apt-get -y install apache2-utils


echo
echo "Adding the Docker Registry CA certificate to /usr/local/share/ca-certificates/docker-reg-cert"
sudo mkdir /usr/local/share/ca-certificates/docker-reg-cert
sudo cp $CA_CERT_FILE /usr/local/share/ca-certificates/docker-reg-cert
sudo update-ca-certificates


echo "Restarting the Docker daemon so that it picks up the changes to our certificate store"
sudo service docker restart


echo
echo "Creating docker-registry directory"
sudo mkdir /docker-registry


echo
echo "Changing the working directory to the docker-registry directory"
cd /docker-registry


echo
echo "Copying the docker-compose.yml file into the docker-registry directory:"
sudo cp ${REGISTRY_DIR}/docker-compose.yml .


echo
echo "Creating a data subdirectory inside the docker-registry directory"
sudo mkdir /docker-registry/data


echo
echo "Creating nginx subdirectory inside the docker-registry directory"
sudo mkdir /docker-registry/nginx


echo
echo "Changing the working directory to the nginx subdirectory"
cd /docker-registry/nginx


echo
echo "Copying registry.conf to the nginx subdirectory:"
sudo cp ${REGISTRY_DIR}/registry.conf /docker-registry/nginx


echo
echo "Adding server key/certificate to the nginx directory"
sudo cp $SERVER_KEY_FILE /docker-registry/nginx/tls.key
sudo cp $SERVER_CERT_FILE /docker-registry/nginx/tls.crt


echo
echo "Adding the default user to the nginx subdirectory:"
default_user="$USER"


echo
echo "Creating $username's user account inside the nginx subdirectory"
sudo htpasswd -b registry.password ${default_user} registry


echo
echo "Copying the docker-registry.service file into the /lib/systemd/system directory:"
sudo cp ${REGISTRY_DIR}/docker-registry.service /lib/systemd/system


echo
echo "Enabling the Docker Registry service"
sudo systemctl enable docker-registry


echo
echo "Starting the Docker Registry Service"
sudo systemctl start docker-registry


echo
echo "Confirming Docker Registry run status"
sudo systemctl status docker-registry --no-pager


echo
echo "---------------------------------"
echo "Leaving - Install Docker Registry"
echo "---------------------------------"
