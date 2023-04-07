#!/bin/bash

set -a
source "./../../../../.env"
set +a

kubectl config set-context --current --namespace="bnl-demo-app-ns"

kubectl delete serviceaccount demo-app-jwt-sa --ignore-not-found=true
kubectl create serviceaccount demo-app-jwt-sa

kubectl delete secret generic spring-boot-credentials-jwt --ignore-not-found=true

openssl s_client -connect "$CONJUR_MASTER_HOSTNAME":"$CONJUR_MASTER_PORT" \
  -showcerts </dev/null 2> /dev/null | \
  awk '/BEGIN CERTIFICATE/,/END CERTIFICATE/ {print $0}' \
  > conjur.pem

kubectl create secret generic spring-boot-credentials-jwt  \
        --from-literal=conjur-service-id="demo-cluster"  \
        --from-literal=conjur-account="$CONJUR_ACCOUNT" \
        --from-literal=conjur-jwt-token-path="/var/run/secrets/tokens/jwt" \
        --from-literal=conjur-appliance-url="$CONJUR_APPLIANCE_URL"  \
        --from-file "CONJUR_SSL_CERTIFICATE=conjur.pem"

# DEPLOYMENT
envsubst < deployment.yml | kubectl replace --force -f -
if ! kubectl wait deployment demo-app-jwt --for condition=Available=True --timeout=90s
  then exit 1
fi

kubectl get services demo-app-jwt
kubectl get pods

rm conjur.pem
