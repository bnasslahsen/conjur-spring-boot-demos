#!/bin/bash

# Define MySQL credentials
POD_NAME=demo-db-mysql-65c5f547b5-jqq2q

MYSQL_ROOT_USER=root
MYSQL_ROOT_PASSWORD=demo-db
MYSQL_USER=demo-db
NEW_PASSWORD=demo-db4


# Connect to MySQL and update password
kubectl exec -n bnl-demo-app-ns $POD_NAME -c demo-db-mysql -- mysql -u $MYSQL_ROOT_USER -p$MYSQL_ROOT_PASSWORD -e "ALTER USER '$MYSQL_USER' IDENTIFIED BY '$NEW_PASSWORD';" 


conjur variable set -i data/bnl/ocp-apps/mysql-password -v $NEW_PASSWORD
