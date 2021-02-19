#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Initialize Kubernetes Cluster (Single Node)"
echo "---------------------------------------------------------------------------------"

BASEDIR="$( cd "$( dirname "$0" )" && pwd )"
TAINT_MASTER_NODE=1

echo
echo "Switching off swap"
sudo swapoff -a

echo
echo "Initializing Kubernetes cluster"
sudo kubeadm init --apiserver-advertise-address=0.0.0.0

echo
echo "Removing prior cluster configurations files if found"
rm -rf $HOME/.kube

echo
echo "Making new cluster configuration files directory"
mkdir -p $HOME/.kube

echo
echo "Copying cluster configuration files to the new directory"
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config

echo
echo "Changing the ownership of the configuration files to the current non-root user"
echo "This will allow the user to administer the cluster without having to be root"
sudo chown $(id -u):$(id -g) $HOME/.kube/config

echo
echo "Optionally tainting the cluster's master node"
if [ $TAINT_MASTER_NODE -eq 1 ]
then
     kubectl taint nodes --all node-role.kubernetes.io/master-
fi

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Initialize Kubernetes Cluster (Single Node)"
echo "---------------------------------------------------------------------------------"







