#!/bin/bash


green=$(tput setaf 2) 
blue=$(tput setaf 4)
cyan=$(tput setaf 6)
red=$(tput setaf 1)
reset=$(tput sgr0)
tput bel
echo " ${green}  _____ _ _       _     ____                       _   _                 "        
echo " ${green} |  ___| (_)_ __ | |_  |  _ \ ___ _ __   ___  _ __| |_(_)_ __   __ _     "
echo " ${green} | |_  | | |  _ \| __| | |_) / _ \  _ \ / _ \|  __| __| |  _ \ / _  |    "
echo " ${green} |  _| | | | | | | |_  |  _ \  __/ |_) | (_) | |  | |_| | | | | (_| |    "
echo " ${green} |_|   |_|_|_| |_|\__| |_| \_\___| .__/ \___/|_|   \__|_|_| |_|\__, |    " 
echo " ${green}                                 |_|                           |___/     "
echo " ${green}               _____           _         _   ___                         "
echo " ${green}              |_   _|__   ___ | | __   _/ | / _ \                        "
echo " ${green}                | |/ _ \ / _ \| | \ \ / / || | | |                       "
echo " ${green}                | | (_) | (_) | |  \ V /| || |_| |                       " 
echo " ${green}                |_|\___/ \___/|_|   \_/ |_(_)___/                        "

echo " ${reset}---------------------------------------------------------------------    "
echo "                                                                                  "
echo "                                                                                  "
echo " Setting up the system’s environment for FLINT Reporting Tool                     "
echo "                                                                                  "
echo "                                                                                  "
echo "                                                                                  "
echo " ${blue}Collecting useful information for configurations and security certificates  ${reset}                "

# Directories
SUPER_DIR="$( cd "$( dirname "$0" )" && pwd )"
ENVIRONMENT_DIR="$(dirname "$SUPER_DIR")"
SETUP_DIR="$(dirname "$ENVIRONMENT_DIR")"
SCRIPTS_DIR="$(dirname "$SETUP_DIR")"
ROOT_DIR="$(dirname "$SCRIPTS_DIR")"

# locate the IP Allocation Range section in the script file
echo "Enter the IP allocation range (for example: 10.32.0.0/12): "
read IPALLOC_RANGE
sed -i "s/172.30.0.0/16/$IPALLOC_RANGE/" $ENVIRONMENT_DIR/kubernetes/cluster/addons/weavenet/install.sh


echo " ${blue}Installing NodeJS on the server ${reset} "
if [ -z "$(which nodejs)" ]
then
    echo
    echo " ${red} Nodejs not found, installing it    ${reset} "
    bash $ENVIRONMENT_DIR/nodejs/install.sh
else 
    echo " ${green} Nodejs is installed!"
    echo
fi


echo " ${blue}Installing NPM on the server ${reset} "
if [ -z "$(which npm)" ]
then
    echo
    echo "${red} NPM not found, installing it     ${reset} "
    bash $ENVIRONMENT_DIR/npm/install.sh
else 
    echo " ${green} NPM is installed!"
    echo
fi


echo " ${blue}Installing Angular on the server ${reset} "
if [ -z "$(which ng)" ]
then
    echo
    echo "${red} Angular not found, installing it     ${reset} "
    bash $ENVIRONMENT_DIR/angular/install.sh
else 
    echo " ${green} Angular is installed!"
    echo
fi


echo " ${blue}Installing Docker Engine CE on the server ${reset} "
if [ -z "$(which docker)" ]
then
    echo
    echo "${red} Docker not found, installing it   ${reset}     "
    bash $ENVIRONMENT_DIR/docker/install.sh
else 
    echo " ${green} Docker is installed!"
    echo 
fi


echo " ${blue}Installing Kubernetes on the server ${reset} "
if [ -z "$(which kubectl)" ]
then
    echo
    echo "${red} Kubernetes not found, installing it    ${reset}  "
    bash $ENVIRONMENT_DIR/kubernetes/install.sh
else 
    echo " ${green} Kubernetes is installed!"
    echo
fi


echo " ${blue}Initializing Kubernetes Cluster on the server ${reset} "
bash $ENVIRONMENT_DIR/kubernetes/cluster/initialize.sh


echo " ${blue}Adding Weavenet to the Kubernetes Cluster ${reset} "
bash $ENVIRONMENT_DIR/kubernetes/cluster/addons/weavenet/install.sh


echo " ${blue}Adding Helm to the Kubernetes Cluster ${reset} "
bash $ENVIRONMENT_DIR/kubernetes/cluster/addons/helm/install.sh


echo " ${blue}Adding Traefik to the Kubernetes Cluster ${reset} "
bash $ENVIRONMENT_DIR/kubernetes/cluster/addons/traefik/install.sh


echo " ${blue}Adding Local Storage Volumes to the Kubernetes Cluster ${reset} "
sudo mkdir -p /mnt/disks
bash $ENVIRONMENT_DIR/kubernetes/cluster/storage/install.sh


echo " ${blue}Adding PostgreSQL to the Kubernetes Cluster ${reset} "
bash $ENVIRONMENT_DIR/kubernetes/cluster/addons/postgresql/install.sh
bash $ENVIRONMENT_DIR/kubernetes/cluster/addons/postgresql/client/install.sh


echo " ${blue}Adding RabbitMQ to the Kubernetes Cluster ${reset} "
bash $ENVIRONMENT_DIR/kubernetes/cluster/addons/rabbitmq/install.sh


echo "${cyan}           _                                           _      _       _         ${reset} "
echo "${cyan}  ___  ___| |_ _   _ _ __     ___ ___  _ __ ___  _ __ | | ___| |_ ___| |        ${reset} "
echo "${cyan} / __|/ _ \ __| | | |  _ \   / __/ _ \|  _   _ \|  _ \| |/ _ \ __/ _ \ |        ${reset} "
echo "${cyan} \__ \  __/ |_| |_| | |_) | | (_| (_) | | | | | | |_) | |  __/ ||  __/_|        ${reset} "
echo "${cyan} |___/\___|\__|\__,_| .__/   \___\___/|_| |_| |_| .__/|_|\___|\__\___(_)        ${reset} "
echo "${cyan}                    |_|                         |_|                             ${reset} "
