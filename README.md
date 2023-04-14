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
  - `default` : For application with Conjur Spring-Boot plugin

## Test the application outside the IDE:
```shell
./mvnw clean package
./run.sh not-secured
./run.sh default
```

## Building the Docker images
- Build the `conjur-spring-boot-demos`
```shell
./mvnw clean spring-boot:build-image

## For Native images
./mvnw -Pnative clean spring-boot:build-image

## For summon
podman build --arch=amd64 -f src/main/conjur/summon/Dockerfile -t conjur-spring-boot-demos-summon .
```

## Deploy to k8s

- For Application with K8s secrets:
```shell
cd src/main/deployments/1-k8s-secrets
./deploy-app.sh
```

- For API KEY profile:
```shell
cd src/main/deployments/2-java-api-key
./deploy-app.sh
```

- For JWT profile:
```shell
cd src/main/deployments/3-java-jwt
./deploy-app.sh
```

- For Secrets Provider for k8s with Init container:
```shell
  cd src/main/deployments/4-secrets-provider-init
  ./deploy-app.sh
```

- For Secrets Provider for k8s with Sidecar container:
```shell
cd src/main/deployments/5-secrets-provider-sidecar
./deploy-app.sh
```

- For Push to File with Sidecar container:
  - To test with H2:
```shell
cd src/main/deployments/6-push-to-file/h2
./deploy-app.sh
./deploy-app-refresh.sh
```
  - To test with MySQL:
```shell
cd src/main/deployments/6-push-to-file/mysql
./deploy-app.sh
./deploy-app-refresh.sh
```

- For Summon with Init Container:
```shell
cd src/main/deployments/7-summon-init
  ./deploy-app.sh
```

- For Summon with Sidecar Container:
```shell
cd src/main/deployments/8-summon-sidecar
  ./deploy-app.sh
```

- For Secretless:
To deploy the DB:
```shell
cd src/main/deployments/9-secretless/db
  ./deploy-app.sh
```
To deploy the App:
```shell
cd src/main/deployments/9-secretless/app
  ./deploy-app.sh
```

### Rotation without an application restart:

