#!/bin/bash

set -a
source "./../../../../.env"
set +a

kubectl config set-context --current --namespace="$APP_NAMESPACE"

kubectl delete serviceaccount demo-app-secrets-provider-init-sa --ignore-not-found=true
kubectl create serviceaccount demo-app-secrets-provider-init-sa

# DB SECRETS
envsubst < k8s-secrets.yml | kubectl replace --force -f -

# Service account role binding
envsubst < service-account-role.yml | kubectl replace --force -f -

# DEPLOYMENT
envsubst < deployment.yml | kubectl replace --force -f -
if ! kubectl wait deployment demo-app-secrets-provider-init --for condition=Available=True --timeout=90s
  then exit 1
fi

kubectl get services demo-app-secrets-provider-init
kubectl get pods
