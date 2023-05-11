#!/bin/bash

set -a
source "./../../../../.env"
set +a

kubectl config set-context --current --namespace="$APP_NAMESPACE"

kubectl delete serviceaccount demo-app-api-key-sa --ignore-not-found=true
kubectl create serviceaccount demo-app-api-key-sa

kubectl delete configmap conjur-connect-java-api-key --ignore-not-found=true

openssl s_client -connect "$CONJUR_MASTER_HOSTNAME":"$CONJUR_MASTER_PORT" \
  -showcerts </dev/null 2> /dev/null | \
  awk '/BEGIN CERTIFICATE/,/END CERTIFICATE/ {print $0}' \
  > conjur.pem

kubectl create configmap conjur-connect-java-api-key \
  --from-literal SPRING_PROFILES_ACTIVE=secured \
  --from-literal CONJUR_AUTHN_API_KEY="$CONJUR_AUTHN_API_KEY"  \
  --from-literal CONJUR_ACCOUNT="$CONJUR_ACCOUNT" \
  --from-literal CONJUR_AUTHN_LOGIN="$CONJUR_AUTHN_LOGIN" \
  --from-literal CONJUR_APPLIANCE_URL="$CYBERARK_CONJUR_APPLIANCE_URL"  \
  --from-literal SPRING_MAIN_CLOUD_PLATFORM="NONE" \
  --from-file "CONJUR_SSL_CERTIFICATE=conjur.pem"

# DEPLOYMENT
envsubst < deployment.yml | kubectl replace --force -f -
if ! kubectl wait deployment demo-app-api-key --for condition=Available=True --timeout=90s
  then exit 1
fi

kubectl get services demo-app-api-key
kubectl get pods

rm conjur.pem
