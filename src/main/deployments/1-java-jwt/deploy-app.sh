#!/bin/bash

set -a
source "./../../../../.env"
set +a

kubectl config set-context --current --namespace="$APP_NAMESPACE"

kubectl delete serviceaccount demo-app-jwt-sa --ignore-not-found=true
kubectl create serviceaccount demo-app-jwt-sa

kubectl delete configmap conjur-connect-spring-jwt --ignore-not-found=true

openssl s_client -connect "$CONJUR_MASTER_HOSTNAME":"$CONJUR_MASTER_PORT" \
  -showcerts </dev/null 2> /dev/null | \
  awk '/BEGIN CERTIFICATE/,/END CERTIFICATE/ {print $0}' \
  > conjur.pem

kubectl create configmap conjur-connect-spring-jwt \
  --from-literal SPRING_PROFILES_ACTIVE=secured-java \
  --from-literal CONJUR_ACCOUNT="$CONJUR_ACCOUNT" \
  --from-literal CONJUR_APPLIANCE_URL="$CYBERARK_CONJUR_FOLLOWER_URL"  \
  --from-literal CONJUR_AUTHENTICATOR_ID="$CONJUR_AUTHENTICATOR_ID"  \
  --from-literal CONJUR_JWT_TOKEN_PATH="/var/run/secrets/kubernetes.io/serviceaccount/token" \
  --from-literal LOGGING_LEVEL_COM_CYBERARK=DEBUG  \
  --from-literal SPRING_MAIN_CLOUD_PLATFORM="NONE" \
  --from-file "CONJUR_SSL_CERTIFICATE=conjur.pem" \
  --from-literal SPRING_SQL_INIT_PLATFORM="mysql" \
  --from-literal DB_SECRET_ADDRESS="vault1/lob1/ocp-safe/Database-MySQL-10.78.10.140-testuser/address" \
  --from-literal DB_SECRET_PORT="vault1/lob1/ocp-safe/Database-MySQL-10.78.10.140-testuser/port" \
  --from-literal DB_SECRET_DATABASE="vault1/lob1/ocp-safe/Database-MySQL-10.78.10.140-testuser/database" \
  --from-literal DB_SECRET_USERNAME="vault1/lob1/ocp-safe/Database-MySQL-10.78.10.140-testuser/username" \
  --from-literal DB_SECRET_PASSWORD="vault1/lob1/ocp-safe/Database-MySQL-10.78.10.140-testuser/password"

# DEPLOYMENT
envsubst < deployment.yml | kubectl replace --force -f -
if ! kubectl wait deployment demo-app-jwt --for condition=Available=True --timeout=90s
  then exit 1
fi

kubectl get services demo-app-jwt
kubectl get pods

rm conjur.pem
