Build Guide
===========

This documentation is the second installment in a series of guides
detailing how to set up and operate the Reporting Tool. Its role is to
lay out a step by step guide on how to build the Reporting Tool source
code into an executable format. It assumes that you have already set up
the needed environment as detailed in the first guide: `Environment
Setup Guide`_.

Prerequisites
-------------

We need to meet two preconditions in order to successfully build the
Reporting Tool:

+---+-----------------------------------------------------------------+
|   | Prerequirement                                                  |
+===+=================================================================+
| 1 | Ascertaining that the Reporting Tool Source Code has been       |
|   | successfully cloned to the build server                         |
+---+-----------------------------------------------------------------+
| 2 | Ascertaining that the Reporting Tool System Environment has     |
|   | been successfully set up                                        |
+---+-----------------------------------------------------------------+

Outline
-------

There are four key steps that we need to take to successfully build the
Reporting Tool Source Code:

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

We will explore each of these steps in detail in the sections that
follow with the exception of Step 1 and 2 which we covered extensively
in the Reporting Tool Environment Setup Guide.

.. _Environment Setup Guide: ../EnvironmentGuide/index.html


.. toctree::
   :hidden:

   building-reporting-tool
