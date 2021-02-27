#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Generate Client Certificate"
echo "---------------------------------------------------------------------------------"

echo
echo "Changing the working directory to the / directory"
cd /

echo
echo "Removing previous temp files"
if [ -f extfile.cnf ]
then
    sudo rm extfile.cnf
fi

echo
echo "Generating a CSR for the Client"
sudo openssl req -subj '/CN=client' -new -key client.key -out client.csr

echo
echo "Creating an extensions config file to make the key suitable for client authentication"
sudo bash -c 'cat > extfile.cnf' << EOF1
extendedKeyUsage = clientAuth
EOF1

echo
echo "Generating CA signed certificate for the Client"
sudo openssl x509 -req -in client.csr -CA ca.crt -CAkey ca.key -CAcreateserial -out client.crt -days 10000 -extfile extfile.cnf

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Generate Client Certificate"
echo "---------------------------------------------------------------------------------"







