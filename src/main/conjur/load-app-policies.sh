#!/bin/bash

conjur policy update -f data-branch.yml -b root >> data-branch.log
conjur policy update -f app-hosts.yml -b data/bnl >> app-hosts.log
conjur policy update -f host-grants.yml -b root >> host-grants.log

conjur variable set -i data/bnl/ocp-apps/url -v "jdbc:h2:mem:testdb"
conjur variable set -i data/bnl/ocp-apps/password -v "sa"
conjur variable set -i data/bnl/ocp-apps/username -v "test"
