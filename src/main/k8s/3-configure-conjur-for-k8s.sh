#!/bin/bash

set -a
source "./../../../.env"
source kube-info.txt
set +a

conjur variable set -i conjur/authn-jwt/$CONJUR_AUTHENTICATOR_ID/jwks-uri -v $JWKS_URI
conjur variable set -i conjur/authn-jwt/$CONJUR_AUTHENTICATOR_ID/issuer -v $SA_ISSUER
conjur variable set -i conjur/authn-jwt/$CONJUR_AUTHENTICATOR_ID/token-app-property -v "sub"
conjur variable set -i conjur/authn-jwt/$CONJUR_AUTHENTICATOR_ID/identity-path -v "data/bnl/ocp-apps"
