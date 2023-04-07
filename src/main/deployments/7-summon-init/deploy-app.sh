#!/bin/bash

set -a
source "./../../../../.env"
set +a

kubectl config set-context --current --namespace="$APP_NAMESPACE"

kubectl delete serviceaccount demo-app-summon-init-sa --ignore-not-found=true
kubectl create serviceaccount demo-app-summon-init-sa

# SUMMON CONFIGMAP
kubectl delete configmap summon-config-init --ignore-not-found=true
envsubst < secrets.template.yml > secrets.yml
kubectl create configmap summon-config-init --from-file=secrets.yml
rm secrets.yml

# DEPLOYMENT
envsubst < deployment.yml | kubectl replace --force -f -
if ! kubectl wait deployment demo-app-summon-init --for condition=Available=True --timeout=90s
  then exit 1
fi

kubectl get services demo-app-summon-init
kubectl get pods
