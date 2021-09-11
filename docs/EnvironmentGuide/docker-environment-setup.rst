Docker Environment Setup
========================

Install Docker Engine
---------------------

Follow the instructions below to install Docker Engine CE:

1. Navigate to the Docker scripts directory on the server:

   .. code:: sh

      cd ~/scripts/environment/docker

2. Invoke the Docker installation script:

   .. code:: sh

      ./install_docker.sh

Install Docker Compose
----------------------

Follow the instructions below to install Docker Compose:

1. Navigate to the docker-compose scripts directory on the server:

   .. code:: sh

      cd ~/scripts/environment/docker/compose

2. Invoke the docker-compose installation script:

   .. code:: sh

      ./install_docker_compose.sh

Install Docker Registry
-----------------------

.. note::
    If you have Let's Encrypt SSL Set Up, please update the ssl section in the
    ~/scripts/environment/docker/registry/registry.conf to the following specifications:

    ssl on;
    ssl_certificate /etc/letsencrypt/live/cloud.reportingtool.org/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/cloud.reportingtool.org/privkey.pem;

    (assuming of course that the system’s domain is: cloud.reportingtool.org)

Follow the instructions below to install Docker Registry:

1. Navigate to the docker-registry scripts directory on the server:

   .. code:: sh

      cd ~/scripts/environment/docker/registry

2. Invoke the docker-registry installation script:

   .. code:: sh

      ./install_docker_registry.sh
