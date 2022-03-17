Super Scripts
=================
This docmentation provides a easy way to setup the FLINT Reporting tool.

Steps to setting up the FLINT Reporting Tool
-------------------------------------------
1. In order to set up the first fork the project from the repositery.

2. Clone the repositery in your local  machine.

   .. code:: sh

      git clone https://github.com/<YOUR_GITHUB_USERNAME>/FLINT.Reporting.git
      
3. Go into the directory containing the superscript.

   .. code:: sh

      cd code/scripts/setup/environment/superscript
      
4. Run the superscript to install all packages/dependencies for setting up the system's environment.

   .. code:: sh
     
      bash superscript.sh

Steps to setting up the Environment for the Reporting Tool over the server

5.  Enter the server domain:
   .. code:: sh

      reporter.miles.co.ke       
      
6.  Enter the domain name for traefik:
   .. code:: sh

     traefik.miles.co.ke       
    
7.  Enter the domain name for rabbitMQ.

   .. code:: sh

     rabbitMQ.miles.co.ke
         
8.  Enter the server's default IP.

   .. code:: sh

      IP Address of the server. (For eg: 172.31.25.183)     
9.  Enter the server's local IP.
   .. code:: sh

       IP Address of the server. (For eg: 172.31.25.183)    
10.  Enter the IP allocation range.
   .. code:: sh

      IP Allocation range (For eg: 10.32.0.0/12)  
          
    
