#!/bin/bash

set -a
source "./../../../.env"
set +a

#Set up a Kubernetes Authenticator endpoint in Conjur
envsubst < jwt-authenticator-webservice.yml > jwt-authenticator-webservice.yml.tmp
conjur policy update -f jwt-authenticator-webservice.yml.tmp -b conjur/authn-jwt
rm jwt-authenticator-webservice.yml.tmp

#Enable the JWT Authenticator in Conjur Cloud
#conjur authenticator enable --id authn-jwt/$CONJUR_AUTHENTICATOR_ID