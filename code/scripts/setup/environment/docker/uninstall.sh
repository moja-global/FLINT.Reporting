#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Uninstall Docker (Community Edition)"
echo "---------------------------------------------------------------------------------"

echo
echo "Removing docker-ce"
sudo apt purge -y docker-ce
echo

echo
echo "Removing docker directory"
sudo rm -rf /etc/docker

echo
echo "Removing docker service directory"
sudo rm -rf /etc/systemd/system/docker.service.d

echo
while true; do
    read -p "Delete all images, containers, and volumes as well [y/n]? " yn
    case $yn in
        [Yy]* ) 
		echo "Deleting all images, containers, and volumes"; 
		sudo rm -rf /var/lib/docker 
		break;;
        [Nn]* ) exit;;
        * ) echo "Please answer yes(y) or no(n).";;
    esac
done

echo "Removing any unused packages"
sudo apt autoremove -y


echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Uninstall Docker (Community Edition)"
echo "---------------------------------------------------------------------------------"


