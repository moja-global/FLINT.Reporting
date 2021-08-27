.. _flintreporting-documentation:

FLINT.Reporting Documentation
=============================

To edit the documentation you need a `GitHub`_ account. Once you have
created one and logged in, you can edit any page by navigating to the
corresponding file and clicking the edit (pen) icon.

This will create a "fork" and further you can create a "pull request",
which will be approved by one of the existing members of the Docs team.
If you have any development experience, you can setup the docs on your
local machine to build the documentation locally.

First make a fork, and then clone the repo:

.. code:: sh

   git clone https://github.com/<GITHUB_USERNAME>/FLINT.Reporting.git
   cd FLINT.Reporting
   cd docs

Replace the ``<GITHUB_USERNAME>`` with your GitHub username. You can
find your username by clicking on your profile picture in the top right
corner of the GitHub website.

We are now in the ``docs`` directory. Let us set the doucmentation up:

.. code:: sh

   virtualenv env
   source env/bin/activate
   pip install -r requirements.txt
   make html

You can now open the documentation site on ``_build/html/index.html`` in
your browser. Make corresponding changes on the documentation site and
then run ``make clean && make html`` to update the documentation. You
can now create a pull request to get your changes merged into the
upstream develop branch.

.. _GitHub: github.com
