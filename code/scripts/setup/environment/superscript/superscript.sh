#!bin/bash


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

# Information about the server
echo "Enter the server domain:               "
read serverdomain
sed -i "s/reporter.miles.co.ke/$serverdomain/" $ROOT_DIR/configurations/configurations.json

# update the domains and IP sections of the config file
echo "Enter the domain name for traefik: "
read traefikdom
sed -i "s/traefik.miles.co.ke/$traefikdom/" $ROOT_DIR/configurations/configurations.json

echo "Enter the domain name for rabbitMQ: "
read rabbitmqdom
sed -i "s/rabbitmq.miles.co.ke/$rabbitmqdom/" $ROOT_DIR/configurations/configurations.json

# Update the server ip in the configuartion file.
echo "Enter the server's default IP: "
read serverip
sed -i "s/34.122.239.158/$serverip/" $ROOT_DIR/configurations/configurations.json
sed -i "s/34.122.239.158/$serverip/" $ENVIRONMENT_DIR/kubernetes/cluster/addons/traefik/values.yaml

# locate the external IP section in the configuration file
echo "Enter the server's local IP"
read localip
sed -i "s/10.128.0.2/$localip/" $ROOT_DIR/configurations/configurations.json
sed -i "s/10.128.0.2/$localip/" $ENVIRONMENT_DIR/kubernetes/cluster/addons/traefik/values.yaml

# locate the IP Allocation Range section in the script file
echo "Enter the IP allocation range (for example: 10.32.0.0/12): "
read IPALLOC_RANGE
sed -i "s/172.30.0.0/16/$IPALLOC_RANGE/" $ENVIRONMENT_DIR/kubernetes/cluster/addons/weavenet/install.sh


echo " ${blue}Installing Nano editor on the server ${reset} "
if [ -z "$(which nano)" ] 
then
    echo
    echo " ${red} nano not found, installing it ${reset} "
    bash $ENVIRONMENT_DIR/nano/install.sh
else 
    echo " ${green} Package is installed! "
    echo
fi


echo " ${blue}Installing jq parser on the server ${reset} "
if [ -z "$(which jq)" ]
then
    echo
    echo " ${red} jq parser not found, installing it   ${reset} "
    bash $ENVIRONMENT_DIR/jq/install.sh
else 
    echo " ${green} Package is installed! "
    echo
fi


echo " ${blue}Setting up SSL files on the server ${reset} "
if [ -z "$(which openssl)" ]
then
    echo
    echo " ${red} openssl not found, installing it      ${reset} "
    bash $ENVIRONMENT_DIR/openssl/generate.sh
else 
    echo "${green} Package is installed!"
    echo
fi


echo " ${blue}Installing JDK on the server ${reset} "
if [ -z "$(which java)" ]
then
    echo
    echo " ${red} JDK not found, installing it       ${reset} "
    bash $ENVIRONMENT_DIR/jdk/install.sh
else 
    echo " ${green} Package is installed!"
    echo
fi


echo " ${blue}Installing Apache Maven on the server ${reset} "
if [ -z "$(which mvn)" ]
then
    echo
    echo " ${red} maven not found, installing it    ${reset} "
    bash $ENVIRONMENT_DIR/maven/install.sh
else 
    echo " ${green} Package is installed!"
    echo
fi


echo " ${blue}Installing NodeJS on the server ${reset} "
if [ -z "$(which nodejs)" ]
then
    echo
    echo " ${red} nodejs not found, installing it    ${reset} "
    bash $ENVIRONMENT_DIR/nodejs/install.sh
else 
    echo " ${green} Package is installed!"
    echo
fi


echo " ${blue}Installing NPM on the server ${reset} "
if [ -z "$(which npm)" ]
then
    echo
    echo "${red} npm not found, installing it     ${reset} "
    bash $ENVIRONMENT_DIR/npm/install.sh
else 
    echo " ${green} Package is installed!"
    echo
fi


echo " ${blue}Installing Angular on the server ${reset} "
if [ -z "$(which ng)" ]
then
    echo
    echo "${red} Angular not found, installing it     ${reset} "
    bash $ENVIRONMENT_DIR/angular/install.sh
else 
    echo " ${green} Package is installed!"
    echo
fi


echo " ${blue}Installing Docker Engine CE on the server ${reset} "
if [ -z "$(which docker)" ]
then
    echo
    echo "${red} Docker not found, installing it   ${reset}     "
    bash $ENVIRONMENT_DIR/docker/install.sh
else 
    echo " ${green} Package is installed!"
    echo 
fi

echo " ${blue}Installing Docker Compose on the server ${reset} "
if [ -z "$(which docker-compose)" ]
then
    echo
    echo "${red} Docker Compose not found, installing it    ${reset} "
    bash $ENVIRONMENT_DIR/docker/compose/install.sh
else 
    echo " ${green} Package is installed!"
    echo
fi

echo " ${blue}Installing Docker Registry on the server ${reset} "
if [ -z "$(which docker-registry)" ]
then
    echo
    echo "${red} Docker Registry not found, installing it    ${reset} "
    bash $ENVIRONMENT_DIR/docker/registry/install.sh
else 
    echo " ${green} Package is installed!"
    echo
fi

echo " ${blue}Installing Kubernetes on the server ${reset} "
if [ -z "$(which kubectl)" ]
then
    echo
    echo "${red} Kubernetes not found, installing it    ${reset}  "
    bash $ENVIRONMENT_DIR/kubernetes/install.sh
else 
    echo " ${green} Package is installed!"
    echo
fi


echo " ${blue}Initializing Kubernetes Cluster on the server ${reset} "
bash $ENVIRONMENT_DIR/kubernetes/cluster/initialize.sh


echo " ${blue}Adding Docker Registry Secret to the Kubernetes Cluster ${reset} "
bash $ENVIRONMENT_DIR/kubernetes/cluster/secrets/add_docker_registry_secret.sh


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