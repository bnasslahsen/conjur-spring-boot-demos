#!/bin/bash

set -a
source "./../../../../.env"
set +a

kubectl config set-context --current --namespace="bnl-test-app-namespace"

kubectl delete serviceaccount test-app-java-sdk-sa --ignore-not-found=true
kubectl create serviceaccount test-app-java-sdk-sa

kubectl delete secret generic java-sdk-credentials --ignore-not-found=true

openssl s_client -connect "$CONJUR_MASTER_HOSTNAME":"$CONJUR_MASTER_PORT" \
  -showcerts </dev/null 2> /dev/null | \
  awk '/BEGIN CERTIFICATE/,/END CERTIFICATE/ {print $0}' \
  > conjur.pem

kubectl create secret generic java-sdk-credentials  \
        --from-literal=conjur-authn-api-key="$CONJUR_AUTHN_API_KEY"  \
        --from-literal=conjur-account="$CONJUR_ACCOUNT" \
        --from-literal=conjur-authn-login="$CONJUR_AUTHN_LOGIN" \
        --from-literal=conjur-appliance-url="$CONJUR_APPLIANCE_URL"  \
        --from-file "CONJUR_SSL_CERTIFICATE=conjur.pem"

# DEPLOYMENT
envsubst < deployment.yml | kubectl replace --force -f -
if ! kubectl wait deployment test-app-java-sdk --for condition=Available=True --timeout=90s
  then exit 1
fi

kubectl get services test-app-java-sdk
kubectl get pods

rm conjur.pem
