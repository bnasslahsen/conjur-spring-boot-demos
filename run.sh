#!/bin/bash

set -a
source ".env"
set +a

java -jar target/conjur-spring-boot-demos-1.0.jar --spring.profiles.active=$@