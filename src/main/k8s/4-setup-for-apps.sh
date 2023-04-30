#!/bin/bash

set -a
source "./../../../.env"
set +a

kubectl delete configmap conjur-connect --ignore-not-found=true

kubectl create configmap conjur-connect \
  --from-literal CONJUR_ACCOUNT="$CONJUR_ACCOUNT" \
  --from-literal CONJUR_APPLIANCE_URL="$CYBERARK_CONJUR_APPLIANCE_URL" \
  --from-literal CONJUR_AUTHN_URL="$CYBERARK_CONJUR_APPLIANCE_URL"/authn-jwt/"$CONJUR_AUTHENTICATOR_ID" \
  --from-literal AUTHENTICATOR_ID="$CONJUR_AUTHENTICATOR_ID" \
  --from-literal CONJUR_AUTHN_JWT_SERVICE_ID="$CONJUR_AUTHENTICATOR_ID" \
  --from-literal MY_POD_NAMESPACE="$APP_NAMESPACE"\
  --from-file "CONJUR_SSL_CERTIFICATE=$CONJUR_CERT_FILE"