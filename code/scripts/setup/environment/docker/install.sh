#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Install Docker Version 19.03.11 (Community Edition)"
echo "---------------------------------------------------------------------------------"

# Capture the directory within which this script is located as the base directory
BASEDIR="$( cd "$( dirname "$0" )" && pwd )"

# Update the package listing
sudo apt update


# Install a few prerequisite packages which let apt use packages over HTTPS
sudo apt install -y apt-transport-https ca-certificates curl software-properties-common gnupg2


# Add the GPG key for the official Docker repository to your system
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -


# Add the Docker repository to APT sources
sudo add-apt-repository \
"deb [arch=amd64] https://download.docker.com/linux/ubuntu \
$(lsb_release -cs) \
stable"


# Update the package database with the Docker packages from the newly added repo
sudo apt update


# Install Docker
sudo apt install -y --allow-downgrades \
containerd.io=1.2.13-2 \
docker-ce=5:19.03.11~3-0~ubuntu-$(lsb_release -cs) \
docker-ce-cli=5:19.03.11~3-0~ubuntu-$(lsb_release -cs)


# Stop Docker
sudo systemctl stop docker


# Set up Docker Daemon
if [ ! -e /etc/docker/daemon.json ]; then
  sudo cp ${BASEDIR}/daemon.json /etc/docker/
  sudo chmod u+x /etc/docker/daemon.json
fi


# Set up the Docker Service Directory
sudo mkdir -p /etc/systemd/system/docker.service.d


# Reload systemd manager configuration
sudo systemctl daemon-reload


# Restart Docker
sudo systemctl start docker


# Set Docker Service to start on boot
sudo systemctl enable docker


# Check that Docker is running
sudo systemctl status docker


echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Install Docker Version 19.03.11 (Community Edition)"
echo "---------------------------------------------------------------------------------"


