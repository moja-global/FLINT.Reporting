Scripts
=======

The Reporting Tool system has a collection of scripts that have been
designed for use in the setup of its environment. In the chapter on
Environment Setup, we discuss the usage of these scripts in setting up a
Reporting Tool environment. We recommend that the operator downloads and
familiarizes themselves with these scripts.

The download instructions are as given below:

.. note::
    Please note that it is assumed that the operator has a Github Account
    with access to the scripts repository and a Personal Access Token set up.



1. In the target installation server, open a shell session and clone the
   setup scripts into the administrative user's home directory:

::

   cd ~ && \
   git clone \
   --recursive \
   --depth 1 \
   -b MASTER \
   https://<Personal_Access_Token>@github.com/Reporting Tool/scripts.git scripts

.. note::
   Please see “\ `Creating a personal access token for the command line”`_
   for more information on personal access tokens.

.. _Creating a personal access token for the command line”: https://help.github.com/en/github/authenticating-to-github/creating-a-personal-access-token-for-the-command-line

2. Once the cloning is complete, please confirm that you can see a
   non-empty directory named environment under the scripts directory:

::

   cd ~/scripts && ls -l

.. note::
   This will see to it that the subsequent RUN commands are executed from this directory.
