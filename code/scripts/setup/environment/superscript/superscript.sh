#!/bin/bash


green=$(tput setaf 2) 
blue=$(tput setaf 4)
cyan=$(tput setaf 6)
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

echo "Enter the server domain:               "
read serverdomain
sed -i "s/reporter.miles.co.ke/$serverdomain/" $ROOT_DIR/configurations/configurations.json

echo "Enter the domain name for traefik: "
read traefikdom
sed -i "s/traefik.miles.co.ke/$traefikdom/" $ROOT_DIR/configurations/configurations.json

echo "Enter the domain name for rabbitMQ: "
read rabbitmqdom
sed -i "s/rabbitmq.miles.co.ke/$rabbitmqdom/" $ROOT_DIR/configurations/configurations.json

echo "Enter the server's default IP: "
read serverip
sed -i "s/34.122.239.158/$serverip/" $ROOT_DIR/configurations/configurations.json
sed -i "s/34.122.239.158/$serverip/" $ENVIRONMENT_DIR/kubernetes/cluster/addons/traefik/values.yaml

echo "Enter the server's local IP"
read localip
sed -i "s/10.128.0.2/$localip/" $ROOT_DIR/configurations/configurations.json
sed -i "s/10.128.0.2/$localip/" $ENVIRONMENT_DIR/kubernetes/cluster/addons/traefik/values.yaml


echo "Enter the IP allocation range (for example: 10.32.0.0/12): "
read IPALLOC_RANGE
sed -i "s/172.30.0.0/16/$IPALLOC_RANGE/" $ENVIRONMENT_DIR/kubernetes/cluster/addons/weavenet/install.sh




echo " ${blue}Installing Nano editor on the server ${reset}                 "

bash $ENVIRONMENT_DIR/nano/install.sh

echo " ${blue}Installing jq parser on the server ${reset}                   "
bash $ENVIRONMENT_DIR/jq/install.sh

echo " ${blue}Setting up SSL files on the server ${reset}                   "

bash $ENVIRONMENT_DIR/openssl/generate.sh

echo " ${blue}Installing JDK on the server ${reset}                         "

bash $ENVIRONMENT_DIR/jdk/install.sh

echo " ${blue}Installing Apache Maven on the server ${reset}                "

bash $ENVIRONMENT_DIR/maven/install.sh

echo " ${blue}Installing NodeJS on the server ${reset}                      "

bash $ENVIRONMENT_DIR/nodejs/install.sh

echo " ${blue}Installing NPM on the server ${reset}                         "

bash $ENVIRONMENT_DIR/npm/install.sh

echo " ${blue}Installing Angular on the server ${reset}                     "

bash $ENVIRONMENT_DIR/angular/install.sh

echo " ${blue}Installing Docker Engine CE on the server ${reset}                     "

bash $ENVIRONMENT_DIR/docker/install.sh

echo " ${blue}Installing Docker Compose on the server ${reset}                     "

bash $ENVIRONMENT_DIR/docker/compose/install.sh

echo " ${blue}Installing Docker Registry on the server ${reset}                     "

bash $ENVIRONMENT_DIR/docker/registry/install.sh

echo " ${blue}Installing Kubernetes on the server ${reset}                     "

bash $ENVIRONMENT_DIR/kubernetes/install.sh

echo " ${blue}Initializing Kubernetes Cluster on the server ${reset}                     "

bash $ENVIRONMENT_DIR/kubernetes/cluster/initialize.sh

echo " ${blue}Adding Docker Registry Secret to the Kubernetes Cluster ${reset}                     "

bash $ENVIRONMENT_DIR/kubernetes/cluster/secrets/add_docker_registry_secret.sh

echo " ${blue}Adding Weavenet to the Kubernetes Cluster ${reset}                     "

bash $ENVIRONMENT_DIR/kubernetes/cluster/addons/weavenet/install.sh

echo " ${blue}Adding Helm to the Kubernetes Cluster ${reset}                     "

bash $ENVIRONMENT_DIR/kubernetes/cluster/addons/helm/install.sh

echo " ${blue}Adding Traefik to the Kubernetes Cluster ${reset}                     "

bash $ENVIRONMENT_DIR/kubernetes/cluster/addons/traefik/install.sh

echo " ${blue}Adding Local Storage Volumes to the Kubernetes Cluster ${reset}                     "

sudo mkdir -p /mnt/disks

bash $ENVIRONMENT_DIR/kubernetes/cluster/storage/install.sh

echo " ${blue}Adding PostgreSQL to the Kubernetes Cluster ${reset}                     "

bash $ENVIRONMENT_DIR/kubernetes/cluster/addons/postgresql/install.sh
bash $ENVIRONMENT_DIR/kubernetes/cluster/addons/postgresql/client/install.sh

echo " ${blue}Adding RabbitMQ to the Kubernetes Cluster ${reset}                     "

bash $ENVIRONMENT_DIR/kubernetes/cluster/addons/rabbitmq/install.sh

echo "${cyan}           _                                           _      _       _         ${reset} "
echo "${cyan}  ___  ___| |_ _   _ _ __     ___ ___  _ __ ___  _ __ | | ___| |_ ___| |        ${reset} "
echo "${cyan} / __|/ _ \ __| | | |  _ \   / __/ _ \|  _   _ \|  _ \| |/ _ \ __/ _ \ |        ${reset} "
echo "${cyan} \__ \  __/ |_| |_| | |_) | | (_| (_) | | | | | | |_) | |  __/ ||  __/_|        ${reset} "
echo "${cyan} |___/\___|\__|\__,_| .__/   \___\___/|_| |_| |_| .__/|_|\___|\__\___(_)        ${reset} "
echo "${cyan}                    |_|                         |_|                             ${reset} "