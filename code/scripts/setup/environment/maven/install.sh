#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Install Maven"
echo "---------------------------------------------------------------------------------"

VERSION=3.6.3

echo
echo "Updating local package index"
sudo apt update

echo
echo "Changing the working directory to /opt"
cd /opt

echo
echo "Downloading Maven"
sudo wget http://www-eu.apache.org/dist/maven/maven-3/${VERSION}/binaries/apache-maven-${VERSION}-bin.tar.gz

echo
echo "Extracting Maven"
sudo tar -xvzf apache-maven-${VERSION}-bin.tar.gz

echo
echo "Renaming the extracted directory to maven"
sudo mv apache-maven-${VERSION} maven

echo
echo "Creating mavenenv.sh file inside /etc/profile.d"
echo "Then adding M2_HOME environmental variable"
echo "Then adding PATH environmental variable"
sudo bash -c 'cat > /etc/profile.d/mavenenv.sh <<- "EOF"
export JAVA_HOME="/usr/lib/jvm/default-java"
export M2_HOME="/opt/maven"
export PATH=${M2_HOME}/bin:${PATH}
EOF'

echo
echo "Updating /etc/profile.d/mavenenv.sh file permissions"
sudo chmod +x /etc/profile.d/mavenenv.sh

echo
echo "Reloading the environmental variables without rebooting"
set -a; . /etc/profile.d/mavenenv.sh; set +a;

echo
echo "Allowing all users in the machine to be able to execute mvn commands"
sudo chmod a+x /opt/maven/bin/mvn

echo
echo "Removing the downloaded archive"
sudo rm apache-maven-${VERSION}-bin.tar.gz


echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Install Maven"
echo "---------------------------------------------------------------------------------"

