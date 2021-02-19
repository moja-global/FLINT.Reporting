#!/bin/bash

echo
echo "---------------------------------------------------------------------------------"
echo "Entering - Uninstall Storage Volumes (Local)"
echo "---------------------------------------------------------------------------------"

BASEDIR=$(dirname "$0")
VOLUMES=2
LOCATION=/mnt/disks


for i in `seq 1 $VOLUMES`
do

   echo
   echo "Delete persistent volume ${i} from Kubernetes"
   helm uninstall persistence-volume-$i

   echo
   echo "Deleting storage folder ${i} from disk"
   sudo rm -rf ${LOCATION}/local_storage/vol$i

done


echo
echo "---------------------------------------------------------------------------------"
echo "Leaving - Uninstall Storage Volumes (Local)"
echo "---------------------------------------------------------------------------------"



