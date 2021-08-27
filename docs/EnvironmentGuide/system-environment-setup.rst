System Environment Setup
========================

Update the ``/etc/environment`` file:
-------------------------------------

Follow the instructions below to ready the ``/etc/environment`` file for
the system installation:

1. Open the ``/etc/environment`` file:

   ::

      sudo nano /etc/environment

2. Add the followings environment variables to the ``/etc/environment``
   file:

   ::

      export HOST_NAME="<kubernetes.machine.name>"
      export API_SERVER_HOST_NAME="<cloud.reportingtool.org>"
      export POSTGRESQL_SERVER_HOST_NAME="<cloud.reportingtool.org>"
      export RABBITMQ_SERVER_HOST_NAME="<rabbitmq.reportingtool.org>"
      export REGISTRY_SERVER_HOST_NAME="<cloud.reportingtool.org>"
      export TRAEFIK_SERVER_HOST_NAME="<traefik.reportingtool.org>"

3. Press Ctrl + X button and the enter Y to save the updates.

4. Reload the ``/etc/environment`` file:

   ::

      . /etc/environment

Update the ``/etc/hosts`` file
------------------------------

Follow the instructions that follow to ready the ``/etc/hosts`` file for
the system installation:

1. Open the ``/etc/hosts`` file:

   ::

      sudo nano /etc/hosts

2. Add the followings hosts line to the ``/etc/hosts`` file:

   ::

      <10.118.16.9> <cloud.reportingtool.org> <rabbitmq.reportingtool.org>
      <traefik.reportingtool.org>
.. note::
-  Please remember to replace the sections with angular brackets with
   actual values.
-  Please note that this assumes that the cloud, rabbitmq and traefik
   are all on a single server.

3. Press Ctrl + X button and the enter Y to save the updates.

4. Reload the ``/etc/hosts`` file:

   ::

      . /etc/hosts

Install jq
----------

jq is a lightweight and flexible command-line JSON processor. Follow the
instructions that follow to install it on your system:

::

   sudo apt-get install jq
