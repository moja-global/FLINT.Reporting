#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Generate Key and Certificates"
echo "---------------------------------------------------------------------------------"

BASEDIR="$( cd "$( dirname "$0" )" && pwd )"


echo
echo "Checking if the CA certificate is available in the root directory"
if [ ! -f /ca.crt ]; then

    echo
    echo "CA certificate not found"

    if [ ! -f /ca.key ]; then

        echo
    	echo "CA RSA key not found"

        echo
    	echo "Generating CA RSA key"

	cd ${BASEDIR}
	chmod a+x ca-key.sh
	bash $BASEDIR/ca-key.sh
	
    fi
 
    echo
    echo "Generating CA certificate"

    cd ${BASEDIR}
    chmod a+x ca-crt.sh
    bash $BASEDIR/ca-crt.sh
fi

echo
echo "Checking if the server certificate is available in the root directory"
if [ ! -f /server.crt ]; then
 
    echo
    echo "Server certificate not found"

    if [ ! -f /server.key ]; then

        echo
    	echo "Server RSA key not found"

        echo
    	echo "Generating server RSA key"

	cd ${BASEDIR}
	chmod a+x server-key.sh
	bash $BASEDIR/server-key.sh
	
    fi

    if [ ! -f /ca.key ]; then

        echo
    	echo "CA RSA key not found"

        echo
    	echo "Generating CA RSA key"

	cd ${BASEDIR}
	chmod a+x ca-key.sh
	bash $BASEDIR/ca-key.sh

    	echo
    	echo "Re-generating CA certificate"
    	chmod a+x ca-crt.sh
    	bash $BASEDIR/ca-crt.sh
	
    fi

    echo
    echo "Generating server certificate"

    cd ${BASEDIR}
    chmod a+x server-crt.sh
    bash $BASEDIR/server-crt.sh
fi


echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Generate Key and Certificates"
echo "---------------------------------------------------------------------------------"


