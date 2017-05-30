#!/bin/bash
cd src/frontend &&
mvn -f ../../pom.xml frontend:install-node-and-yarn frontend:yarn frontend:yarn@install-bower frontend:yarn@build frontend:yarn@test &&
cd ../.. &&
npm install &&
mvn clean package
