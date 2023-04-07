#!/bin/bash

conjur policy update -f data-branch.yml -b root >> data-branch.log
conjur policy update -f app-hosts.yml -b data/bnl >> app-hosts.log
conjur policy update -f host-grants.yml -b root >> host-grants.log

conjur variable set -i data/bnl/ocp-apps/url -v "jdbc:h2:mem:testdb"
conjur variable set -i data/bnl/ocp-apps/password -v "sa"
conjur variable set -i data/bnl/ocp-apps/username -v "test"

conjur variable set -i data/bnl/ocp-apps/mysql-host -v "demo-db-mysql.bnl-demo-app-ns.svc.cluster.local"
conjur variable set -i data/bnl/ocp-apps/mysql-port -v "3306"
conjur variable set -i data/bnl/ocp-apps/mysql-username -v "demo-db-mysql"
conjur variable set -i data/bnl/ocp-apps/mysql-password -v "demo-db-mysql"
