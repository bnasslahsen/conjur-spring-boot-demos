#!/bin/bash

# Define MySQL credentials
POD_NAME=$(kubectl get pods -l app=demo-db-mysql -o=name)

MYSQL_ROOT_USER=root
MYSQL_ROOT_PASSWORD=demo-db
MYSQL_USER=demo-db
APP_NAMESPACE=bnl-demo-app-ns

# Generate random secret (done by CyberArk CPM in production)
NEW_PASSWORD=$(openssl rand -hex 12 | tr -d '\r\n')


# Connect to MySQL and update password
kubectl exec -n $APP_NAMESPACE $POD_NAME -c demo-db-mysql -- mysql -u $MYSQL_ROOT_USER -p$MYSQL_ROOT_PASSWORD -e "ALTER USER '$MYSQL_USER' IDENTIFIED BY '$NEW_PASSWORD';" 
conjur variable set -i data/bnl/ocp-apps/mysql-password -v $NEW_PASSWORD
