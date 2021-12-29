# scripts
Setup / Operations Scripts

## Setup Scripts 
Environment / System 

## Steps to setting up the Environment locally

1. Fork the repository

2. Clone the repository 

```
    https://github.com/<YOUR_GITHUB_USERNAME>/FLINT.Reporting.git
```

3. Go into the directory containing the superscript.

```
    cd code/scripts/setup/environment/superscript
```
4. Run the superscript to install all packages/dependencies for setting up the system's environment.

```
    bash superscript.sh
```

## Steps to setting up the Environment for the Reporting Tool over the server

1. Setup the server 

2. Fork the repository 

3. Clone the repository on the server.

```
    git@github.com:moja-global/FLINT.Reporting.git system
```

4. Go into the directory containing the superscript.

```
    cd system/code/scripts/setup/environment/superscript
```

5. Run the superscript to install all packages/dependencies for setting up the system's environment

```
    bash superscript.sh
```
6. Entering the information regarding the server 

    i.   Enter the server domain: 

    ``` 
        reporter.miles.co.ke       
    ```      
    ii.  Enter the domain name for traefik: 

    ``` 
        traefik.miles.co.ke
    ```
    iii. Enter the domain name for rabbitMQ. 
    
    ``` 
        rabbitMQ.miles.co.ke
    ```
    iv.  Enter the server's default IP.

    v.   Enter the server's local IP.

    vi.  Enter the IP allocation range.



