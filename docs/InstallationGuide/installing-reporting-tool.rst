Installing The Reporting Tool
=============================

Installing Default Configuration Data
-------------------------------------

In order to install the Reporting Tool’s default configuration data, we
need to:

1. Open a Linux Terminal.

2. Start a bash session on one of the Cassandra instances running inside
   Kubernetes:

   .. code:: sh

      kubectl exec -it cassandra-0 /bin/bash

3. Invoke the database initialization script:

   .. code:: sh

      bash /tmp/data/init.sh

4. Exit from the Cassandra Instance when done:

   .. code:: sh

      exit

Installing Services
-------------------

In order to add the Reporting Tool services into our Kubernetes Cluster,
we need to:

1. Change the working directory to the Reporting Tool’s System Services
   Setup Scripts Directory:

   .. code:: sh

      cd ~/reporting-tool/scripts/system/service

2. Invoke the services installation script:

   .. code:: sh

      ./install.sh

Installing The User Interface
-----------------------------

In order to add the Reporting Tool services into our Kubernetes Cluster,
we need to:

1. Change the working directory to the Reporting Tool’s System Client
   Setup Scripts Directory:

   .. code:: sh

      cd ~/reporting-tool/scripts/system/client

2. Invoke the client installation script:

   .. code:: sh

      ./install.sh
