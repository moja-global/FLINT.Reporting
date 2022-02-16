Kubernetes Environment Setup
============================

Install Kubernetes
------------------

Follow the instructions below to install Kubernetes:

1. Navigate to the Kubernetes scripts directory on the server:

   .. code:: sh

      FLINT.Reporting/code/scripts/setup/environment/kubernetes

2. Invoke the Kubernetes installation script:

   .. code:: sh

      bash install.sh

Create Kubernetes Cluster
-------------------------

Follow the instructions below to create a Kubernetes cluster:

1. Navigate to the kubernetes-cluster scripts directory on the server:

   .. code:: sh

      FLINT.Reporting/code/scripts/setup/environment/kubernetes/cluster

2. Invoke the kubernetes-cluster initialization script:

   .. code:: sh

      bash initialize.sh

Add Docker Registry Secret to the Cluster
-----------------------------------------

Follow the instructions below to add Docker Registry secret to the
cluster:

1. Navigate to the kubernetes-cluster-secrets scripts directory on the
   server:

   .. code:: sh

    FLINT.Reporting/code/scripts/setup/environment/kubernetes/cluster/secrets

2. Invoke the add private docker registry secret script:

   .. code:: sh

      bash add_private_docker_registry_secret.sh

Add Weavenet to the Cluster
---------------------------

Follow the instructions below to add Weavenet to the cluster:

1. Navigate to the weavenet scripts directory on the server:

   .. code:: sh

      FLINT.Reporting/code/scripts/setup/environment/kubernetes/cluster/addons/weavenet

2. Invoke the meta installation script:

   .. code:: sh

      bash install.sh

Install Helm
------------

Follow the instructions below to install Helm:

1. Navigate to the helm scripts directory on the server:

   .. code:: sh

   FLINT.Reporting/code/scripts/setup/environment/kubernetes/cluster/addons/helm

2. Invoke the helm installation script:

   .. code:: sh

      bash install.sh

Add Traefik to the Cluster
--------------------------

Follow the instructions below to add Traefik to the cluster:

1. Navigate to the traefik scripts directory on the server:

   .. code:: sh

       FLINT.Reporting/code/scripts/setup/environment/kubernetes/cluster/addons/traefik

2. Invoke the traefik installation script:

   .. code:: sh

      bash install.sh

Add Storage Volumes to the Cluster
----------------------------------

Follow the instructions below to add local storage volumes to the
cluster:

1. Navigate to the kubernetes local storage scripts directory on the
   server:

   .. code:: sh

       FLINT.Reporting/code/scripts/setup/environment/kubernetes/cluster/addons/storage

2. Reload the environmental variables:

   .. code:: sh

      . /etc/environment

3. Invoke the local storage volumes installation script:

   .. code:: sh

      bash install.sh

Add PostgreSQL to the Cluster
-----------------------------

Follow the instructions below to add PostgreSQL to the cluster:

1. Navigate to the PostgreSQL setup scripts directory on the server:

   .. code:: sh

       FLINT.Reporting/code/scripts/setup/environment/kubernetes/cluster/addons/postgresql

2. Invoke the Postgres installation script:

   .. code:: sh

      bash install.sh

Add RabbitMQ to the Cluster
---------------------------

Follow the instructions below to add RabbitMQ to the cluster:

1, Navigate to the RabbitMQ scripts directory on the server:

   .. code:: sh

        FLINT.Reporting/code/scripts/setup/environment/kubernetes/cluster/addons/rabbitmq

2. Invoke the rabbitmq installation script:

   .. code:: sh

      bash install.sh
