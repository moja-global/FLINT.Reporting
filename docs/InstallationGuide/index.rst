Installation Guide
==================

This documentation is the third instalment in a series of guides
detailing how to set up and operate the Reporting Tool. Its role is to
lay out a step by step guide on how to install the Reporting Tool. It
assumes that you have already set up the needed environment as detailed
in the first guide: `Reporting Tool Environment Setup Guide`_ and built
the Reporting Tool Docker Images as detailed in the second guide:
`Reporting Tool Build Guide`_.

Prerequisites
-------------

+---+-----------------------------------------------------------------+
|   | Prerequirement                                                  |
+===+=================================================================+
| 1 | Ascertaining that the Reporting Tool Source Code has been       |
|   | successfully cloned to the build server                         |
+---+-----------------------------------------------------------------+
| 2 | Ascertaining that the Reporting Tool System Environment has     |
|   | been successfully set up                                        |
+---+-----------------------------------------------------------------+
| 3 | Ascertaining that the Reporting Tool Docker Images have been    |
|   | successfully built and registered                               |
+---+-----------------------------------------------------------------+

Outline
-------

+---+-----------------------------------------------------------------+
|   | Step                                                            |
+===+=================================================================+
| 1 | Cloning the Reporting Tool Source Code from Github              |
+---+-----------------------------------------------------------------+
| 2 | Setting up the Reporting Tool System Environment                |
+---+-----------------------------------------------------------------+
| 3 | Compiling the Reporting Tool Source Code and Building Docker    |
|   | Images out of it                                                |
+---+-----------------------------------------------------------------+
| 4 | Adding the built Docker Images to our private Docker Registry   |
|   | (and which Kubernetes has access to)                            |
+---+-----------------------------------------------------------------+
| 5 | Installing Default Data Configurations                          |
+---+-----------------------------------------------------------------+
| 6 | Installing the Microservices                                    |
+---+-----------------------------------------------------------------+
| 7 | Installing the User Interface (Client)                          |
+---+-----------------------------------------------------------------+

We will explore Step 5 to 7 in the section that follows; having already
explored Step 1 and 2 extensively in the Reporting Tool Environment
Setup Guide and Step 3, 4 and 5 in the Reporting Tool Build Guide.

.. _Reporting Tool Environment Setup Guide: ../EnvironmentGuide/index.html
.. _Reporting Tool Build Guide: ../BuildGuide/index.html
