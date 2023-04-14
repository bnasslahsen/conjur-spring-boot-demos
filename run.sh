#!/bin/bash

set -a
source ".env"
set +a

CONJUR_APPLIANCE_URL=https://${CONJUR_MASTER_HOSTNAME}:${CONJUR_MASTER_PORT}

#java -jar target/conjur-spring-boot-demos-1.0.jar --spring.profiles.active=$@

jenv exec java  -Dspring.aot.enabled=true  \
    -Dspring.datasource.url=jdbc:mysql://localhost:3306/demo-db \
    -Dspring.datasource.username=demo-db \
    -Dspring.datasource.password=demo-db \
    -Dconjur.refresh.enabled=true  \
    -agentlib:native-image-agent=config-merge-dir=src/main/resources/META-INF/native-image/ \
    -jar target/conjur-spring-boot-demos-1.0.jar

#jenv exec java  -Dspring.aot.enabled=true  -agentlib:native-image-agent=config-merge-dir=src/main/resources/META-INF/native-image/ -jar target/conjur-spring-boot-demos-1.0.jar