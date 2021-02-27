#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Uninstall Docker Registry"
echo "---------------------------------------------------------------------------------"

echo
echo "Stopping the docker-registry service"
sudo systemctl stop docker-registry
echo

echo
echo "Removing the docker-registry Upstart Script"
sudo rm /lib/systemd/system/docker-registry.service

echo
if [ -d "/docker-registry" ]; then

	echo
	echo "Navigating to the docker registry directory"
	cd /docker-registry

	echo
	echo "Removing existing containers"
	docker-compose rm

	echo
	echo "Going back one level"
	cd ..

	echo
	echo "Removing docker-registry directory"
	sudo rm -rf ./docker-registry

	echo
	if [ -d "/usr/local/share/ca-certificates/docker-reg-cert" ]; then

		while true; do
		    read -p "Remove docker-reg-cert directory [y/n]? " yn
		    case $yn in
			[Yy]* ) 

				  echo "Removing docker-reg-cert directory"
				  sudo rm -rf /usr/local/share/ca-certificates/docker-reg-cert

				  echo "Restarting Docker service"
				  sudo service docker restart

				break;;
			[Nn]* ) exit;;
			* ) echo "Please answer yes(y) or no(n).";;
		    esac
		done

	fi
	echo

fi

echo
echo "Uninstalling apache2-utils package"
sudo apt-get purge -y apache2-utils
echo

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Uninstall Docker Registry"
echo "---------------------------------------------------------------------------------"


