# Build process
This document describes deployment process using war file on Linux machine.

## Prerequisites
These prerequisites are not strict but reflect an actual deployment environment:

 - Ubuntu 16.04.2 LTS
 - OpenJDK Java 1.8.0_131
 - OpenLDAP 2.4.44
 - Apache HTTP Server (or similar)

## Deployment
During build step, a deployable war should have been produced. War contains all files Tomcat needs in order to run an application.

#### Run
Tomcat will automatically run the application right after having been launched. In order to launch Tomcat execute the following
shell script: `CATALINA_HOME/bin/startup.sh`

#### Logs
Logs are written to `CATALINA_HOME/logs` by default. A concrete file to keep logs can be specified in the following way:
~~~
1. Create your own external application.properties file (see Application configuration chapter)
2. Add this property: logging.file=path_to_log_file/file_name.log
For example: logging.file=${catalina.home}/logs/riha-browser.log
~~~

**NB!** Default Spring base.xml configuration allows to backup max 7 logs archives (10MB each), which will be saved as `file_name.log.1 ... file_name.log.7`. You can create your own `logback-spring.xml` file and specify it's location by adding `logging.config=path_to_your_logback-spring.xml` to external `application.properties` file.

### Application configuration
Sensible application default properties are packaged inside war with name `application.properties`.

It is possible to override application properties by passing parameters on the Command Line when executing the application, or setting system variables. Another way is to create and place `riha-browser.xml` file in `CATALINA_HOME/conf/Catalina/localhost` where you can specify external `application.properties` file location.

Example of `riha-browser.xml`:
~~~
<Context>
    <Parameter name="spring.config.location" value="path_to_external_application.properties_file" />
</Context>
~~~

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

Apache HTTP Server is used for Estonian eID citizen smart card authentication. Apache HTTP Server acts as a proxy/load balancer and must be configured to use SSL (mod_ssl) and require client certificate for predefined resource `/login/esteid`. In case of successful negotiation, Apache HTTP Server forwards request to proxied host and sets two request headers `SSL_CLIENT_S_DN` and `SSL_CLIENT_CERT` from corresponding mod_ssl values.

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
            
    <Location "/login/esteid">
       SSLVerifyClient require
       SSLVerifyDepth 2
    
       RequestHeader set SSL_CLIENT_S_DN "%{SSL_CLIENT_S_DN}s"
       RequestHeader set SSL_CLIENT_CERT "%{SSL_CLIENT_CERT}s"
    </Location>
</VirtualHost>
~~~
More information on `SSLCACerificateFile` generation can be found [here](https://eid.eesti.ee/index.php/Authenticating_in_web_applications#Implementing_authentication_with_an_ID_card)