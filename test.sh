#!/bin/bash
mvn clean &&
# UI tests are disabled for now
#pushd frontend &&
#mvn frontend:install-node-and-yarn frontend:yarn frontend:yarn@install-bower frontend:yarn@build frontend:yarn@test &&
#popd &&
mvn test