#!/bin/bash

kubectl config set-context --current --namespace="bnl-test-app-namespace"

kubectl delete serviceaccount test-app-java-sdk-sa --ignore-not-found=true
kubectl create serviceaccount test-app-java-sdk-sa

kubectl delete secret generic java-sdk-credentials --ignore-not-found=true

openssl s_client -connect emea-cybr.secretsmgr.cyberark.cloud:443 \
  -showcerts </dev/null 2> /dev/null | \
  awk '/BEGIN CERTIFICATE/,/END CERTIFICATE/ {print $0}' \
  > conjur.pem

cat ./../conjur-cloud-ca >> conjur.pem

kubectl create secret generic java-sdk-credentials  \
        --from-literal=conjur-authn-api-key=2esa1621p2h7vw3bhf3cav1cnsr2ab69e33teaz152rvgm3q2rdxv91  \
        --from-literal=conjur-account=conjur \
        --from-literal=conjur-authn-login=host/data/bnl/ocp-team/ocp-apps/test-app-java-sdk-sa \
        --from-literal=conjur-appliance-url=https://emea-cybr.secretsmgr.cyberark.cloud/api  \
        --from-file "CONJUR_SSL_CERTIFICATE=conjur.pem"

# DEPLOYMENT
envsubst < deployment.yml | kubectl replace --force -f -
if ! kubectl wait deployment test-app-java-sdk --for condition=Available=True --timeout=90s
  then exit 1
fi

kubectl get services test-app-java-sdk
kubectl get pods

rm conjur.pem
