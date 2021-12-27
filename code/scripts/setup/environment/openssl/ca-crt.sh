#!/bin/bash
# Directories
OPENSSL_DIR="$( cd "$( dirname "$0" )" && pwd )"
ENVIRONMENT_DIR="$(dirname "$OPENSSL_DIR")"
SETUP_DIR="$(dirname "$ENVIRONMENT_DIR")"
SCRIPTS_DIR="$(dirname "$SETUP_DIR")"
ROOT_DIR="$(dirname "$SCRIPTS_DIR")"

EXTFILE="$OPENSSL_DIR/cert_ext.cnf"
COMMON_NAME="$1"

function show_usage {
    printf "Usage: $0 [options [parameters]]\n"
    printf "\n"
    printf "Options:\n"
    printf " -cn, Provide Common Name for the certificate\n"
    printf " -h|--help, print help section\n"

    return 0
}

case $1 in
     -cn)
         shift
         COMMON_NAME="$1"
         ;;
     --help|-h)
         show_usage
         exit 0
         ;;
     *)
        ## Use hostname as Common Name
        COMMON_NAME=`/usr/bin/hostname`
        ;;
esac

## Update Common Name in External File
/bin/echo "commonName              = $COMMON_NAME" >> $EXTFILE

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Generate CA Certificate"
echo "---------------------------------------------------------------------------------"

echo
echo "Changing the working directory to the / directory"
cd /

echo
echo "Generating the CA's certificate in the / directory"
sudo openssl req -x509 -new -nodes -key ca.key -days 10000 -out ca.crt -config $EXTFILE

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Generate CA Certificate"
echo "---------------------------------------------------------------------------------"
