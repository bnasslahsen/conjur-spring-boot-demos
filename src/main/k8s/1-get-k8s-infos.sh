#!/bin/bash

set -a
source "./../../../.env"
set +a


SA_ISSUER="$(kubectl get --raw /.well-known/openid-configuration | jq -r '.issuer')"
echo "SA_ISSUER=\"$SA_ISSUER\"" > kube-info.txt

JWKS_URI=$SA_ISSUER/keys
echo "JWKS_URI=\"$JWKS_URI\"" >> kube-info.txt

curl -s $JWKS_URI > jwks.json