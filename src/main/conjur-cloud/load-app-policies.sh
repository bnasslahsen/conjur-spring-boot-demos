#!/bin/bash

set -a
source "./../../../.env"
set +a

conjur policy update -f data-branch.yml -b data >> data-branch.log

envsubst < app-hosts.yml > app-hosts.yml.tmp
conjur policy update -b data/bnl -f app-hosts.yml.tmp >> app-hosts.log
rm app-hosts.yml.tmp

conjur variable set -i data/bnl/ocp-apps/url -v "jdbc:h2:mem:testdb"
conjur variable set -i data/bnl/ocp-apps/password -v "vault-password"
conjur variable set -i data/bnl/ocp-apps/username -v "sa"

conjur variable set -i data/bnl/ocp-apps/mysql-url -v "jdbc:mysql://demo-db-mysql.$APP_NAMESPACE.svc.cluster.local:3306/demo-db"
conjur variable set -i data/bnl/ocp-apps/mysql-host -v "demo-db-mysql.$APP_NAMESPACE.svc.cluster.local"
conjur variable set -i data/bnl/ocp-apps/mysql-port -v "3306"
conjur variable set -i data/bnl/ocp-apps/mysql-username -v "demo-db"
conjur variable set -i data/bnl/ocp-apps/mysql-password -v "demo-db"

envsubst < host-grants.yml > host-grants.yml.tmp
conjur policy update -b conjur/authn-jwt -f host-grants.yml.tmp
rm host-grants.yml.tmp
