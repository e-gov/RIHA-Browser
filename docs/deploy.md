# Build process
This document describes deployment process using Spring Boot executable jar on linux machine.

## Prerequisites
These prerequisites are not strict but reflect an actual deployment environment:

 - Ubuntu 16.04.2 LTS
 - OpenJDK Java 1.8.0_131
 - OpenLDAP 2.4.44
 - Apache HTTP Server (or similar)

## Deployment
During build step, an Spring Boot executable jar should have been produced. Jar contains Spring Boot application that can be easily started as Unix/Linux service using either `init.d` or `systemd`. For complete documentation please see [Installing Spring Boot applications](https://docs.spring.io/spring-boot/docs/current/reference/html/deployment-install.html#deployment-install).

### Install as a service
Install as a `init.d` service
~~~bash
sudo ln -s /var/riha-browser/backend/target/browser-backend-0.3.0.jar /etc/init.d/riha-browser
~~~
Here `riha-browser` will become a service name that will be used system wide.

Configure automatic start (optional)
~~~bash
sudo update-rc.d riha-browser defaults <priority>
~~~

Secure jar file and restrict its modification (optional)
~~~bash
sudo chmod 500 riha-browser.jar
sudo chattr +i riha-browser.jar
~~~

#### Run
Service will be executed as a user that owns the jar file even when started automatically at boot. Please make sure to change jar file owner to anything but `root`

Start application service. **Note!** Service will be started as the user that owns the jar file
~~~bash
sudo service riha-browser start
~~~

#### Logs
Logs are written to `/var/log/riha-browser.log`

#### PID file
Application`s PID is tracked using `/var/run/riha-browser/riha-browser.pid`

#### Init script configuration
It is possible to configure init script without modifying jar file. Script can be customized by creating configuration file is expected to be placed next to the jar file with same name as jar but suffixed with `.conf`. For example if jar path is `/var/riha-browser/backend/target/browser-backend-0.3.0.jar`, then configuration should be placed to `/var/riha-browser/backend/target/browser-backend-0.3.0.conf` file.

### Application configuration
Sensible application default properties are packaged inside jar with name `application.properties`.

Spring Boot provides numerous ways to configure application like command line arguments, configuration JSON embedded in an environment variable or system property, JNDI attributes, etc. One of the easiest ways to configure Spring Boot application is to place configuration file named `application.properties` next to the jar file.

Please refer to Spring Boot [Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config) documentation for more information.

#### Configuration file example
Please see [application.properties](../backend/src/main/resources/application.properties) file for up to date configuration.

### OpenLDAP
OpenLDAP installation and initial configuration is not covered here. It is assumed that OpenLDAP server is already configured and running.

Although LDAP structure is not restricted, some of the attributes, like group `CN`, must be properly formatted. Here is LDIF sample of a single group and user:
~~~
dn: dc=riha,dc=ria,dc=ee
objectClass: organization
objectClass: dcObject
objectClass: top
dc: riha
o: RIHA LDAP Server

dn: ou=Groups,dc=riha,dc=ria,dc=ee
objectClass: organizationalUnit
objectClass: top
ou: Groups

dn: ou=Users,dc=riha,dc=ria,dc=ee
objectClass: organizationalUnit
objectClass: top
ou: Users

dn: cn=12345678-kirjeldaja,ou=Groups,dc=riha,dc=ria,dc=ee
objectClass: extensibleObject
objectClass: organizationalRole
objectClass: top
cn: 12345678-kirjeldaja
displayName: ACME OU

dn: uid=47101010033,ou=Users,dc=riha,dc=ria,dc=ee
objectClass: extensibleObject
objectClass: organizationalPerson
objectClass: person
objectClass: top
cn: Mari-Liis Männik
sn: Männik
givenName: Mari-Liis
mail: mari.mannik@eesti.ee
uid: 47101010033
memberOf: cn=12345678-kirjeldaja,ou=Groups,dc=riha,dc=ria,dc=ee
~~~
Here, test user `Mari-Liis Männik` (`uid=47101010033,ou=Users,dc=riha,dc=ria,dc=ee`) belongs to organiation `Acme OU` (`cn=12345678-kirjeldaja,ou=Groups,dc=riha,dc=ria,dc=ee`) with role `kirjeldaja`. During authorization user will be searched by `uid` attribute, containing user personal code. User may be a member of many groups as configured by adding corresponding `memberOf` attribute.

Group `cn=12345678-kirjeldaja,ou=Groups,dc=riha,dc=ria,dc=ee` represents both organization and role in that organization. Every group `CN` attribute consists of organization code and role name separated by dash. Every group object contains `displayName` attribute that contains name of the organization.

Please see [application.properties](../backend/src/main/resources/application.properties) file for LDAP authentication configuration sample.

### Apache HTTP Server
Use of Apache HTTP Server is not strongly required and can be replaced by any server or application that are capable of client authentication during SSL handshake.

Apache HTTP Server is used for Estonian eID citizen smart card authentication. Apache HTTP Server acts as a proxy/load balancer and must be configured to use SSL (mod_ssl) and require client certificate for predefined resource `/idlogin`. In case of successful negotiation, Apache HTTP Server forwards request to proxied host and sets two request headers `SSL_CLIENT_S_DN` and `SSL_CLIENT_CERT` from corresponding mod_ssl values.

Here is Apache HTTP Server as a proxy configuration example:
~~~
<VirtualHost _default_:443>
    DocumentRoot "/usr/local/apache2/htdocs"
    ServerName riha.ee:443
    
    SSLEngine on
    SSLCertificateFile "/usr/local/apache2/conf/server.crt"
    SSLCertificateKeyFile "/usr/local/apache2/conf/server.key"
    SSLCACertificateFile "/usr/local/apache2/conf/id.crt"
    
    RequestHeader unset SSL_CLIENT_S_DN
    RequestHeader unset SSL_CLIENT_CERT
        
    ProxyPass / http://riha.example.com/
    ProxyPassReverse / http://riha.example.com/
            
    <Location "/idlogin">
       SSLVerifyClient require
       SSLVerifyDepth 2
    
       RequestHeader set SSL_CLIENT_S_DN "%{SSL_CLIENT_S_DN}s"
       RequestHeader set SSL_CLIENT_CERT "%{SSL_CLIENT_CERT}s"
    </Location>
</VirtualHost>
~~~
More information on `SSLCACerificateFile` generation can be found [here](https://eid.eesti.ee/index.php/Authenticating_in_web_applications#Implementing_authentication_with_an_ID_card)