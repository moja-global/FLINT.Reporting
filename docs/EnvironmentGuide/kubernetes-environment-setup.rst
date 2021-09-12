Kubernetes Environment Setup
============================

Install Kubernetes
------------------

Follow the instructions below to install Kubernetes:

1. Navigate to the Kubernetes scripts directory on the server:

   .. code:: sh

      cd ~/scripts/environment/kubernetes

2. Invoke the Kubernetes installation script:

   .. code:: sh

      ./install_kubernetes.sh

Create Kubernetes Cluster
-------------------------

Follow the instructions below to create a Kubernetes cluster:

1. Navigate to the kubernetes-cluster scripts directory on the server:

   .. code:: sh

      cd ~/scripts/environment/kubernetes/cluster

2. Invoke the kubernetes-cluster initialization script:

   .. code:: sh

      ./initialize_kubernetes_cluster.sh

Add Docker Registry Secret to the Cluster
-----------------------------------------

Follow the instructions below to add Docker Registry secret to the
cluster:

1. Navigate to the kubernetes-cluster-secrets scripts directory on the
   server:

   .. code:: sh

      cd ~/scripts/environment/kubernetes/secrets

2. Invoke the add private docker registry secret script:

   .. code:: sh

      ./add_private_docker_registry_secret.sh

Add Weavenet to the Cluster
---------------------------

Follow the instructions below to add Weavenet to the cluster:

1. Navigate to the weavenet scripts directory on the server:

   .. code:: sh

      cd ~/scripts/environment/weavenet

2. Invoke the meta installation script:

   .. code:: sh

      ./install_weavenet.sh

Install Helm
------------

Follow the instructions below to install Helm:

1. Navigate to the helm scripts directory on the server:

   .. code:: sh

      cd ~/scripts/environment/helm

2. Invoke the helm installation script:

   .. code:: sh

      ./install_helm.sh

Add Traefik to the Cluster
--------------------------

Follow the instructions below to add Traefik to the cluster:

1. Navigate to the traefik scripts directory on the server:

   .. code:: sh

      cd ~/scripts/environment/traefik

2. Invoke the traefik installation script:

   .. code:: sh

      ./install_traefik.sh

Add Storage Volumes to the Cluster
----------------------------------

Follow the instructions below to add local storage volumes to the
cluster:

1. Navigate to the kubernetes local storage scripts directory on the
   server:

   .. code:: sh

      cd ~/scripts/environment/kubernetes/storage

2. Reload the environmental variables:

   .. code:: sh

      . /etc/environment

3. Invoke the local storage volumes installation script:

   .. code:: sh

      ./install_storage_volumes.sh

Add PostgreSQL to the Cluster
-----------------------------

Follow the instructions below to add PostgreSQL to the cluster:

1. Navigate to the PostgreSQL setup scripts directory on the server:

   .. code:: sh

      cd ~/scripts/environment/postgres

2. Invoke the Postgres installation script:

   .. code:: sh

      ./install_postgres.sh

Add Cassandra to the Cluster
----------------------------

Follow the instructions below to add Cassandra to the cluster:

1. Navigate to the Cassandra setup scripts directory on the server:

   .. code:: sh

      cd ~/scripts/environment/cassandra

2. Invoke the Cassandra installation script:

   .. code:: sh

      ./install_cassandra.sh

Add RabbitMQ to the Cluster
---------------------------

Follow the instructions below to add RabbitMQ to the cluster:

1, Navigate to the RabbitMQ scripts directory on the server:

   .. code:: sh

        cd ~/scripts/environment/rabbitmq

2. Invoke the rabbitmq installation script:

   .. code:: sh

      ./install_rabbitmq.sh
