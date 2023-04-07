# conjur-spring-boot-demos

A demo application creating using the Spring Framework.
This application requires access to an H2 database.

## Pre-requisites
- OS Linux / MacOS
- podman / docker
- Java 17

## Configure the environment variables
- Set the values of your environment in the file `.env`

## Load the host policies
```shell
cd src/main/conjur
./load-app-policies.sh
```

## Test the application locally from the IDE
- Get the Conjur SSL Certificates
```shell
./get-conjur-certs.sh
```

- Set the environment variables in your IDE: CONJUR_APPLIANCE_URL, CONJUR_ACCOUNT, CONJUR_AUTHN_API_KEY, CONJUR_AUTHN_LOGIN, CONJUR_CERT_FILE
- Run the application with the following profiles:
  - `not-secured` : For application without any secrets protection
  - `secured-api-spring` : For application with Conjur Spring-Boot plugin
  - `secured-api-java` : For pure java/Conjur integration
  - `secured-jwt` : For pure JWT/Spring integration with Conjur


## Test the application outside the IDE:
```shell
./mvnw clean package
./.run.sh not-secured
./.run.sh secured-api-spring
./.run.sh secured-api-java
./.run.sh secured-jwt
```

## Building the Docker images
- Build the `conjur-spring-boot-demos`
```shell
./mvnw clean package
podman build --arch=amd64 -t conjur-spring-boot-demos .
```

## Deploy to k8s

- For Application with K8s secrets:
```shell
cd src/main/deployments/1-k8s-secrets
./deploy-app.sh

- For API KEY profile:
```shell
cd src/main/deployments/2-java-api-key
./deploy-app.sh


- For JWT profile:
cd src/main/deployments/k8s-java-jwt
./deploy-app.sh
```

