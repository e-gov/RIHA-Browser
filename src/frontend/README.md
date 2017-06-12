# Riha

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 1.0.4.

## Requirements

Maven >= 3.3.1 

Need to run specific goals by id from command line. Ex. mvn frontend:yarn@goal_id

## Build

Run build.sh script from project root directory

It will install node, yarn, other node packages into src/frontend directory to build application based on angular 4.

Also it will run lint to check code style and phantomjs headless browser with  karma to run tests.

After finishing the build, application will with all dependencies in the directory:
src/main/resources

You can run static web server to check application with command from src/frontend directory:
mvn -f ../../pom.xml frontend:yarn@args -Dyarn.args="run static"

The same way you can run build steps separate independ on each other,
but only after finishing the frontend:install-node-and-yarn, frontend:yarn goals in pom.xml by the command:
mvn -f ../../pom.xml frontend:install-node-and-yarn frontend:yarn - to install node, yarn and other node packages
mvn -f ../../pom.xml frontend:yarn@build - for building application into main/resources directory
mvn -f ../../pom.xml frontend:yarn@lint - to check code style
mvn -f ../../pom.xml frontend:yarn@test - to run tests

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `-prod` flag for a production build.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).
Before running the tests make sure you are serving the app via `ng serve`.

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).
