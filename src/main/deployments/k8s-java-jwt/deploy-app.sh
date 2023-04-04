#!/bin/bash

kubectl config set-context --current --namespace="bnl-test-app-namespace"

kubectl delete serviceaccount test-app-java-sdk-jwt-sa --ignore-not-found=true
kubectl create serviceaccount test-app-java-sdk-jwt-sa

kubectl delete secret generic java-sdk-credentials-jwt --ignore-not-found=true

openssl s_client -connect emea-cybr.secretsmgr.cyberark.cloud:443 \
  -showcerts </dev/null 2> /dev/null | \
  awk '/BEGIN CERTIFICATE/,/END CERTIFICATE/ {print $0}' \
  > conjur.pem

cat ./../conjur-cloud-ca >> conjur.pem

kubectl create secret generic java-sdk-credentials-jwt  \
        --from-literal=conjur-service-id=ocp-cluster-dev  \
        --from-literal=conjur-account=conjur \
        --from-literal=conjur-jwt-token-path=/var/run/secrets/tokens/jwt \
        --from-literal=conjur-appliance-url=https://emea-cybr.secretsmgr.cyberark.cloud/api  \
        --from-file "CONJUR_SSL_CERTIFICATE=conjur.pem"

# DEPLOYMENT
envsubst < deployment.yml | kubectl replace --force -f -
if ! kubectl wait deployment test-app-java-sdk-jwt --for condition=Available=True --timeout=90s
  then exit 1
fi

kubectl get services test-app-java-sdk-jwt
kubectl get pods

rm conjur.pem
