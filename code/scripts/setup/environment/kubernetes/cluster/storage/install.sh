#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Install Storage Volumes"
echo "---------------------------------------------------------------------------------"

BASEDIR=$(dirname "$0")

# extract the environmental variable HOST_NAME and use it as the HOST value
# if not found, use the name of the machine
# See: https://stackoverflow.com/questions/51946393/kubernetes-pod-warning-1-nodes-had-volume-node-affinity-conflict
HOST=${HOST_NAME:-$(uname -n)}
VOLUMES=2
CAPACITY=5Gi
LOCATION=/mnt/disks


echo
echo "Appropriating storage capacity in template"
sed -i -e 's/capacity: .*/capacity: '${CAPACITY}'/g' $BASEDIR/chart/values.yaml

echo
echo "Appropriating storage host name in template"
sed -i -e 's/value: .*/value: '${HOST}'/g' $BASEDIR/chart/values.yaml


for i in `seq 1 $VOLUMES`
do

   if [ ! -d "${LOCATION}/local_storage/vol$i" ]
   then

	echo
	echo "Creating vol $i in ${LOCATION}/local_storage/"
	sudo mkdir -p ${LOCATION}/local_storage/vol$i
	sudo chown -R ${USER} ${LOCATION}/local_storage/vol$i
	chmod -R u=rwx,go=r ${LOCATION}/local_storage/vol$i

   fi

   echo
   echo "Appropriating volume name in template"
   sed -i -e 's/vol.*/vol'${i}'/g' $BASEDIR/chart/values.yaml

   echo
   echo "Installing persistent volume"
   helm install persistence-volume-$i $BASEDIR/chart

done


echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Install Storage Volumes"
echo "---------------------------------------------------------------------------------"


