Source Code Organization
========================

Reporting Tool Microservices
----------------------------

::

   --- project root folder

       --- pom.xml

       --- Dockerfile

       --- build.sh

       --- register.sh

       --- install.sh

       --- uninstall.sh

       --- licenseheader.txt

       --- README.md

       | --- src

           | --- main

               | --- java

                   --- Application.java

                   | --- configurations

                       --- RoutesConfig.java

                   | --- handlers

                   | --- models

                   | --- repositories

                   | --- util

               | --- resources

                   --- application.properties

           | --- test

               | --- java

   | --- chart

       | --- templates

           --- service.yaml

           --- deployment.yaml

           --- ingress.yaml

           --- Chart.yaml

           --- values.yaml

Let's check out on more details on what exactly goes into the folder
structure.

Project Root Folder
^^^^^^^^^^^^^^^^^^^

+-------------------+-------------------------------------------------+
| Artifact          | Description                                     |
+===================+=================================================+
| pom.xml           | XML file containing information about the       |
|                   | service and configuration details used by Maven |
|                   | to build the service.                           |
+-------------------+-------------------------------------------------+
| Dockerfile        | Text file containing the instructions needed to |
|                   | create a new container image for the service in |
|                   | question.                                       |
+-------------------+-------------------------------------------------+
| build.sh          | Shell script containing instructions needed to  |
|                   | conveniently build the service source code      |
|                   | regardless of the path from which it's called.  |
+-------------------+-------------------------------------------------+
| register.sh       | Shell script containing instructions needed to  |
|                   | conveniently build the service’s Docker Image   |
|                   | and add it to a Docker Repository regardless of |
|                   | the path from which it's called.                |
+-------------------+-------------------------------------------------+
| install.sh        | Shell script containing instructions needed to  |
|                   | conveniently deploy the service's container to  |
|                   | a Kubernetes cluster regardless of the path     |
|                   | from which it's called.                         |
+-------------------+-------------------------------------------------+
| uninstall.sh      | Shell script containing instructions needed to  |
|                   | conveniently undeploy the service's container   |
|                   | from a Kubernetes cluster regardless of the     |
|                   | path from which it's called.                    |
+-------------------+-------------------------------------------------+
| licenseheader.txt | Text file container the license header details  |
|                   | that should be copied into each Java source     |
|                   | file that is created within the service’s       |
|                   | folder by Netbeans IDE                          |
+-------------------+-------------------------------------------------+
| README.md         | Markdown file describing the project            |
+-------------------+-------------------------------------------------+

Project Root Folder ``/src/main/java/``
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

================ ========================
Artifact         Description
================ ========================
Application.java The service’s main class
================ ========================

Project Root Folder ``/src/main/java/configurations``
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

+-------------------+-------------------------------------------------+
| Artifact          | Description                                     |
+===================+=================================================+
| RoutesConfig.java | Java file containing a collection of Router     |
|                   | Functions that route incoming requests to the   |
|                   | corresponding handler functions when the router |
|                   | function matches                                |
+-------------------+-------------------------------------------------+

\| \|Config.java \| Any other Java configuration file is by default
added to this folder. Typical examples include a HostsConfig file that
specifies the paths that a service should use to access another service
that it depends on; A RabbitConfig file that specifies RabbitMQ access
parameters; A CassandraConfig and PostgresConfig file that specifies how
to access each database respectively. \|

Project Root Folder ``/src/main/java/handlers``
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

+----------------+----------------------------------------------------+
| Artifact       | Description                                        |
+================+====================================================+
| \*Handler.java | All definitions of functions used to handle HTTP   |
|                | requests and their corresponding implementations   |
|                | are added to this folder.                          |
+----------------+----------------------------------------------------+

Project Root Folder ``/src/main/java/repository``
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

+----------+----------------------------------------------------------+
| Artifact | Description                                              |
+==========+==========================================================+
| \*.java  | All entity classes that directly map to a database       |
|          | resource in PostgreSQL or Cassandra are added to this    |
|          | folder.                                                  |
+----------+----------------------------------------------------------+

Project Root Folder ``/src/main/java/repositories``
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

+-------------------+-------------------------------------------------+
| Artifact          | Description                                     |
+===================+=================================================+
| \*Repository.java | All java files that contain code required to    |
|                   | implement data access layers for various        |
|                   | persistence stores are added to this folder.    |
+-------------------+-------------------------------------------------+

Project Root Folder ``/src/main/java/util``
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

+----------+----------------------------------------------------------+
| Artifact | Description                                              |
+==========+==========================================================+
| \*.java  | All other java files that perform utilitarian functions  |
|          | are added to this folder.                                |
+----------+----------------------------------------------------------+

Project Root Folder ``/src/main/resources``
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

+------------------------+--------------------------------------------+
| Artifact               | Description                                |
+========================+============================================+
| application.properties | Properties file containing all             |
|                        | application-level configurations.          |
|                        | Typically called in the Application.java   |
|                        | file to configure the application on boot  |
|                        | up.                                        |
+------------------------+--------------------------------------------+
| \*.properties          | Property files for passing any other       |
|                        | tunable customizations typically to the    |
|                        | system configuration files e.g             |
|                        | HostsConfig, RabbitMQConfig,               |
|                        | PostgresConfig, CassandraConfig etc        |
+------------------------+--------------------------------------------+

Project Root Folder ``/src/test/java``
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

=========== ======================
Artifact    Description
=========== ======================
\*Test.java All system tests files
=========== ======================

Project Root Folder ``/chart/templates``
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

+-------------------+-------------------------------------------------+
| Artifact          | Description                                     |
+===================+=================================================+
| \*service.yaml    | Helm Chart’s Kubernetes service definition      |
|                   | file. A service exposes an application running  |
|                   | on a set of Pods as a network service.          |
+-------------------+-------------------------------------------------+
| \*deployment.yaml | Helm Chart’s Kubernetes deployment definition   |
|                   | file. A deployment provides declarative updates |
|                   | for Pods and ReplicaSets. The desired state is  |
|                   | described in a Deployment, and the Deployment   |
|                   | Controller changes the actual state to the      |
|                   | desired state at a controlled rate.             |
+-------------------+-------------------------------------------------+
| \*ingress.yaml    | Helm Chart’s Kubernetes ingress definition      |
|                   | file. An ingress exposes HTTP and HTTPS routes  |
|                   | from outside the Kubernetes cluster to services |
|                   | within the Kubernetes cluster. Traffic routing  |
|                   | is controlled by rules defined on the Ingress   |
|                   | resource.                                       |
+-------------------+-------------------------------------------------+

Project Root Folder ``/chart``
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

+-------------+-------------------------------------------------------+
| Artifact    | Description                                           |
+=============+=======================================================+
| Chart.yaml  | Helm Chart’s name and version information.            |
+-------------+-------------------------------------------------------+
| values.yaml | Helm Chart’s configurable values for the Kubernetes   |
|             | service, deployment and ingress files definitions.    |
+-------------+-------------------------------------------------------+

Reporting Tool Client
---------------------

::

   --- project root folder

       --- Dockerfile

       --- build.sh

       --- register.sh

       --- install.sh

       --- uninstall.sh

       --- README.md

       | --- src

           | --- app

               | --- core

               | --- data

               | --- layout

               | --- modules

               | --- shared

           | --- styles

   | --- chart

       | --- templates

           --- service.yaml

           --- deployment.yaml

           --- ingress.yaml

       --- Chart.yaml

       --- values.yaml

Let's check out on more details on what exactly goes into the folder
structure.

Project Root folder
^^^^^^^^^^^^^^^^^^^

+--------------+------------------------------------------------------+
| Artifact     | Description                                          |
+==============+======================================================+
| Dockerfile   | Text file containing the instructions needed to      |
|              | create a new container image for the client in       |
|              | question.                                            |
+--------------+------------------------------------------------------+
| build.sh     | Shell script containing instructions needed to       |
|              | conveniently build the client source code regardless |
|              | of the path from which it's called.                  |
+--------------+------------------------------------------------------+
| register.sh  | Shell script containing instructions needed to       |
|              | conveniently build the client’s Docker Image and add |
|              | it to a Docker Repository regardless of the path     |
|              | from which it's called.                              |
+--------------+------------------------------------------------------+
| install.sh   | Shell script containing instructions needed to       |
|              | conveniently deploy the client's container to a      |
|              | Kubernetes cluster regardless of the path from which |
|              | it's called.                                         |
+--------------+------------------------------------------------------+
| uninstall.sh | Shell script containing instructions needed to       |
|              | conveniently undeploy the client's container from a  |
|              | Kubernetes cluster regardless of the path from which |
|              | it's called.                                         |
+--------------+------------------------------------------------------+
| README.md    | Markdown file describing the project                 |
+--------------+------------------------------------------------------+

Project Root Folder ``/src/app``
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

+--------------+------------------------------------------------------+
| Artifact     | Description                                          |
+==============+======================================================+
| ``/core``    | This app subdirectory is for classes used by         |
|              | app.module. Resources which are always loaded such   |
|              | as route guards, HTTP interceptors, and application  |
|              | level services, such as the ThemeService and logging |
|              | belong in this directory.                            |
+--------------+------------------------------------------------------+
| ``/data``    | This app subdirectory holds the types                |
|              | (models/entities) and services (repositories) for    |
|              | data consumed by the application                     |
+--------------+------------------------------------------------------+
| ``/layout``  | This app subdirectory holds one or more components   |
|              | which act as a layout or are parts of a layout such  |
|              | as a Header, Nav, Footer, etc. and have a in the     |
|              | html for other components to embed within            |
+--------------+------------------------------------------------------+
| ``/modules`` | This app subdirectory holds a collection of modules  |
|              | which are each independent of each other. This       |
|              | allows Angular to load only the module it requires   |
|              | to display the request thereby saving bandwidth and  |
|              | speeding the entire application                      |
+--------------+------------------------------------------------------+
| ``/shared``  | This app subdirectory holds classes and resources    |
|              | which are used in more than one dynamically loaded   |
|              | module.                                              |
+--------------+------------------------------------------------------+

Project Root folder ``/src/styles``
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The ``~/src/styles`` directory is used to store SCSS style sheets for
the application. It can contain themes, Bootstrap, Angular Material, and
any other styles.

Project Root Folder ``/chart/templates``
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

+-------------------+-------------------------------------------------+
| Artifact          | Description                                     |
+===================+=================================================+
| \*service.yaml    | Helm Chart’s Kubernetes service definition      |
|                   | file. A service exposes an application running  |
|                   | on a set of Pods as a network service           |
+-------------------+-------------------------------------------------+
| \*deployment.yaml | Helm Chart’s Kubernetes deployment definition   |
|                   | file. A deployment provides declarative updates |
|                   | for Pods and ReplicaSets. The desired state is  |
|                   | described in a Deployment, and the Deployment   |
|                   | controller changes the actual state to the      |
|                   | desired state at a controlled rate              |
+-------------------+-------------------------------------------------+
| \*ingress.yaml    | Helm Chart’s Kubernetes ingress definition      |
|                   | file. An ingress exposes HTTP and HTTPS routes  |
|                   | from outside the Kubernetes cluster to services |
|                   | within the Kubernetes cluster. Traffic routing  |
|                   | is controlled by rules defined on the Ingress   |
|                   | resource                                        |
+-------------------+-------------------------------------------------+

Project Root folder ``/chart``
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

+-------------+-------------------------------------------------------+
| Artifact    | Description                                           |
+=============+=======================================================+
| Chart.yaml  | Helm Chart’s name and version information.            |
+-------------+-------------------------------------------------------+
| values.yaml | Helm Chart’s configurable values for the Kubernetes   |
|             | service, deployment and ingress files definitions.    |
+-------------+-------------------------------------------------------+
