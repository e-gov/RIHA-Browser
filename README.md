![Estonian Information System Authority](https://github.com/e-gov/RIHA-Frontend/raw/master/logo/gov-CVI/lions.png "Estonian Information System Authority") ![European Regional Development Fund](https://github.com/e-gov/RIHA-Frontend/raw/master/logo/EU/EU.png "European Regional Development Fund")

# RIHA-Browser [![RIHA Beta version](https://raw.githubusercontent.com/e-gov/RIHA-Frontend/master/logo/RIHA-beta-env.png)](https://test.riha.ee/)

Software application that allows a human user to browse descriptions and approval decisions. Project is divided into frontend and backend modules. Frontend is an Angular 4 module which interacts with the bakcend java application's API.


## Installation

Installation manual is divided into two: [build process](https://github.com/e-gov/RIHA-Browser/blob/master/docs/build.md) and [deployment process](https://github.com/e-gov/RIHA-Browser/blob/master/docs/deploy.md)


## Development profile configuration

Development profile allows to run the application without external dependencies to LDAP or TARA.

In order to user the development profile, the maven profile "dev" must be selected for packaging and necessary user attribute values must be provided under the application-dev.properties configuration file.

## Selenium automated UI tests

- Automated UI tests located in "autotest" module. 
- By default running tests is disabled for this module (through maven-surefire-plugin configuration) in order not to interrupt regular build process.  
- Selenium test are managed and run by [Cucumber](https://cucumber.io) framework.
- In order to execute all tests with default parameters navigate to "autotest" module folder and run 
`mvn test -DskipTests=false` task.<br><br>
Additional parameters: <br>
<b>browser</b> - defines which webdriver should be used by selenium (driver file is downloaded automatically using "webdrivermanager" Maven plugin)<br>
<i>currently supported:</i> 'chrome', 'firefox' <br>
<i>default:</i> 'chrome' <br>
<b>url</b> - defines RIHA application URL<br>
<i>default:</i> 'https://riha-browser-ik.ci.kit' <br><br>
Running tests with additional parameters:<br>
`mvn test -DskipTests=false -Dbrowser=chrome -Durl=https://riha-browser-ik.ci.kit`


 