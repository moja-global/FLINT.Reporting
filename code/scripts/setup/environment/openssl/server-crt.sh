#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Generate Server Certificate"
echo "---------------------------------------------------------------------------------"

echo
echo "Initializing variables"


# Colors
RED_COLOR='\033[0;31m'
GREEN_COLOR='\033[0;32m'
NO_COLOR='\033[0m'


# Directories
OPENSSL_DIR="$( cd "$( dirname "$0" )" && pwd )"
ENVIRONMENT_DIR="$(dirname "$OPENSSL_DIR")"
SETUP_DIR="$(dirname "$ENVIRONMENT_DIR")"
SCRIPTS_DIR="$(dirname "$SETUP_DIR")"
ROOT_DIR="$(dirname "$SCRIPTS_DIR")"


# Server Domain
SERVER_NAME=$(jq '.domains.default' $ROOT_DIR/configurations/configurations.json)
if [[ $SERVER_NAME == null ]]
then
     echo
     echo -e "${RED_COLOR}Default Server Domain Configuration is Missing${NO_COLOR}"
     echo
     echo "-------------------------------------"
     echo "Leaving - Generate Server Certificate"
     echo "-------------------------------------"
     exit 1
else
     echo
     echo -e "${GREEN_COLOR}Default Server Domain Configuration Found${NO_COLOR}"
fi


# Server IP
SERVER_IP=$(jq '.ips.default' $ROOT_DIR/configurations/configurations.json)
if [[ $SERVER_IP == null ]]
then
     echo
     echo -e "${RED_COLOR}Default Server IP Configuration is Missing${NO_COLOR}"
     echo
     echo "-------------------------------------"
     echo "Leaving - Generate Server Certificate"
     echo "-------------------------------------"
     exit 1
else
     echo
     echo -e "${GREEN_COLOR}Default Server IP Configuration Found${NO_COLOR}"
fi


echo
echo "Changing the working directory to the / directory"
cd /

echo
echo "Removing previous extfile.cnf from the / directory"
if [ -f extfile.cnf ]
then
    sudo rm extfile.cnf
fi

echo
echo "Generating a CSR for the Server"
sudo openssl req -subj "/CN=$SERVER_NAME" -new -key server.key -out server.csr

echo "Specifying the IP addresses since TLS connections can be made via IP address as well as DNS name"
echo "&"
echo "Setting the key’s extended usage attributes to be used only for server authentication"
sudo bash -c 'cat > extfile.cnf' << EOF1
subjectAltName = DNS:$SERVER_NAME,IP:$SERVER_IP,IP:127.0.0.1
extendedKeyUsage = serverAuth
EOF1

echo "Generating CA signed certificate for the Server"
sudo openssl x509 -req -in server.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out server.crt -days 10000 -extfile extfile.cnf

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Generate Server Certificate"
echo "---------------------------------------------------------------------------------"
