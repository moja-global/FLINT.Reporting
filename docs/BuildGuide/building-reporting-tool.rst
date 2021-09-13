Building The Reporting Tool
===========================

Building Reporting Tool Docker Images
-------------------------------------

In order to compile the Reporting Tool Source Code and create Docker
Images out of it, we need to:

1. Change the working directory to the Reporting Tool’s System Services
   Setup Scripts Directory:

   .. code:: sh

      cd ~/reporting-tool/scripts/system/services

..

   This assumes that you cloned the Reporting Tool source code in the
   root of your home directory.

2. Invoke the Reporting Tool Services build script:

   .. code:: sh

      ./build.sh

3. Change the working directory to the Reporting Tool’s System Client
   Setup Scripts Directory:

   .. code:: sh

      cd ~/reporting-tool/scripts/system/client

..

   This assumes that you cloned the Reporting Tool source code in the
   root of your home directory.

4. Invoke the Reporting Tool Client build script:

   .. code:: sh

      ./build.sh

Registering Reporting Tool Docker Images
----------------------------------------

In order to add the built images to our private Docker registry, we need
to:

1. Change the working directory to the Reporting Tool’s System Services
   Setup Scripts Directory:

   .. code:: sh

      cd ~/reporting-tool/scripts/system/service

..

   This assumes that you cloned the Reporting Tool source code in the
   root of your home directory.

2. Invoke the register script:

   .. code:: sh

      ./build.sh

3. Change the working directory to the Reporting Tool’s System Client
   Setup Scripts Directory:

   .. code:: sh

      cd  ~/reporting-tool/scripts/system/client
      ./register.sh

..

   This assumes that you cloned the Reporting Tool source code in the
   root of your home directory.
