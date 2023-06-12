#!/bin/bash

# Download the MYSQL connector jar
curl -LJ https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar -o /opt/mysql-connector-j-8.0.33.jar



# Wait for WildFly to start

# Add MySQL Connector/J module
echo "RUNNING INSTALLATION OF MYSQL"
(sleep 10 && /opt/jboss/wildfly/bin/jboss-cli.sh --connect --command="module add --name=com.mysql.driver --resources=/opt/mysql-connector-j-8.0.33.jar --dependencies=javax.api,javax.transaction.api") &

# Configure the MySQL datasource
(sleep 15 && /opt/jboss/wildfly/bin/jboss-cli.sh --connect --command="/subsystem=datasources/jdbc-driver=mysql:add(driver-name=mysql, driver-module-name=com.mysql.driver, driver-class-name=com.mysql.cj.jdbc.Driver)") &
(sleep 20 && /opt/jboss/wildfly/bin/jboss-cli.sh --connect --command="/subsystem=datasources/data-source=MySqlDS:add(jndi-name=java:/MySqlDS, enabled=true, connection-url=jdbc:mysql://mysql:3306/jboss, driver-name=mysql, user-name=jboss, password=jboss)") &

# Restart WildFly to apply the changes
(sleep 30 && /opt/jboss/wildfly/bin/jboss-cli.sh --connect --command=":shutdown(restart=true)") &
# Download the EAR file from GitHub
(sleep 30 && curl -LJ "https://github.com/rakhiml/ear/raw/main/test_ear.ear" -o "/opt/jboss/wildfly/standalone/deployments/myapp.ear") &

echo "STARTING WILDFLY"
# Start WildFly
/opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0