# conjur-spring-boot-demos

A demo application creating using the Spring Framework.
This application requires access to an H2 database.

## Pre-requisites
- OS Linux / MacOS
- podman / docker
- Java 17


## Building the Docker images
If you don't have access to dockerhub and you need to build the images then, after cloning this repository:

- Build the `conjur-spring-boot-demos`

```shell
./mvnw clean package
# or with podman run -it --rm -v $HOME/.m2:/root/.m2 -v "$(pwd)":/build -w /build maven mvn clean package
podman build --arch=amd64 -t conjur-spring-boot-demos .
```
