#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Install Kubernetes"
echo "---------------------------------------------------------------------------------"

echo
echo "Adding kubernetes repository key"
wget -qO - https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -

echo
echo "Adding kubernetes repository"
sudo bash -c 'cat <<EOF >/etc/apt/sources.list.d/kubernetes.list
deb http://apt.kubernetes.io/ kubernetes-xenial main
EOF'

echo
echo "Updating the package index"
sudo apt update

echo
echo "Installing kubelet, kubeadm and kubectl"
sudo apt install -y kubelet kubeadm kubectl

echo
echo "Preventing apt update and apt upgrade from updating kubelet, kubeadm and kubectl"
sudo apt-mark hold kubelet kubeadm kubectl

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Install Kubernetes"
echo "---------------------------------------------------------------------------------"




