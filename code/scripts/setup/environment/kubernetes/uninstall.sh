#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Uninstall Kubernetes"
echo "---------------------------------------------------------------------------------"


echo
sudo kubeadm reset
sudo apt purge -y --allow-change-held-packages kubelet kubeadm kubectl kubernetes-cni kube*
sudo apt autoremove -y 
sudo rm -rf ~/.kube
sudo rm -rf /etc/kubernetes
sudo rm -rf /var/lib/kubelet
sudo rm -rf /var/lib/etc/kubernetes

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Uninstall Kubernetes"
echo "---------------------------------------------------------------------------------"
