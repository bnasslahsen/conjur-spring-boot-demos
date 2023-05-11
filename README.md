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
    - `default` : For application without any secrets protection
    - `secured` : For application with Conjur Spring-Boot plugin

## Test the application outside the IDE:
```shell
./mvnw clean package
./run.sh default
./run.sh secured
```

## Building the Docker images
- Build the `conjur-spring-boot-demos`
```shell
./mvnw clean spring-boot:build-image

## For Native images
./mvnw -Pnative clean spring-boot:build-image

## For summon
docker build --platform=linux/amd64 -f src/main/conjur/summon/Dockerfile -t conjur-spring-boot-demos-summon .
```

## Deploy to k8s

- For Application with K8s secrets:
```shell
cd src/main/deployments/0-k8s-secrets
./deploy-app.sh
```

- For JWT profile:
```shell
cd src/main/deployments/1-java-jwt
./deploy-app.sh
```

- For Secrets Provider for k8s with Sidecar container:
```shell
  cd src/main/deployments/2-provider-k8s-secrets
  ./deploy-app.sh
```

- For Push to File with Sidecar container:
    - To test with H2:
```shell
cd src/main/deployments/3-provider-push-to-file
./deploy-app.sh
```

- For Summon:
```shell
cd src/main/deployments/4-summon
  ./deploy-app.sh
```

- For Secretless:
  To deploy the DB:
```shell
cd src/main/deployments/5-secretless/db
  ./deploy-app.sh
```
To deploy the App:
```shell
cd src/main/deployments/5-secretless/app
  ./deploy-app.sh
```
