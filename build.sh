#!/bin/bash
mvn clean &&
pushd frontend &&
mvn frontend:install-node-and-yarn frontend:yarn frontend:yarn@install-bower frontend:yarn@build-production &&
popd &&
mvn package