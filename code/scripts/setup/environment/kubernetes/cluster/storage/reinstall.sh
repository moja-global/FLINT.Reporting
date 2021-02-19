#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Re-install Storage Volumes"
echo "---------------------------------------------------------------------------------"

BASEDIR=$(dirname "$0")
# extract the environmental variable HOST_NAME and use it as the HOST value
# if not found, use the name of the machine
# See: https://stackoverflow.com/questions/51946393/kubernetes-pod-warning-1-nodes-had-volume-node-affinity-conflict
HOST=${HOST_NAME:-$(uname -n)}
CAPACITY=10Gi
LOCATION=/mnt/disks


echo
echo "Appropriating storage capacity in template"
sed -i -e 's/capacity: .*/capacity: '${CAPACITY}'/g' $BASEDIR/chart/values.yaml

echo
echo "Appropriating storage host name in template"
sed -i -e 's/value: .*/value: '${HOST}'/g' $BASEDIR/chart/values.yaml

echo
read -p "Enter the volume number: "  i

echo
echo "Delete persistent volume ${i} from Kubernetes"
helm uninstall persistence-volume-${i}

echo
echo "Deleting storage folder ${i} from disk"
sudo rm -rf ${LOCATION}/local_storage/vol${i}

echo
echo "Creating vol $i in ${LOCATION}/local_storage/"
sudo mkdir -p ${LOCATION}/local_storage/vol$i
sudo chown -R ${USER} ${LOCATION}/local_storage/vol$i
chmod -R u=rwx,go=r ${LOCATION}/local_storage/vol$i

echo
echo "Appropriating volume name in template"
sed -i -e 's/vol.*/vol'${i}'/g' $BASEDIR/chart/values.yaml

echo
echo "Installing persistent volume"
helm install persistence-volume-${i} $BASEDIR/chart

echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Re-install Storage Volumes"
echo "---------------------------------------------------------------------------------"



