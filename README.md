# Cloud compiler
## Web based application for compiling and building java projects
### Instructions to install and configure any prerequisites for the development environment:
Prerequisities:
* JDK 8.
* Apache Maven 3.
* Springsource Tool Suite (STS) 3.7.2.
* Apache Tomcat 8.
* MySQL 5.
The environment variable: M3_HOME should be defined with the full path of Maven 3 installation.
The environment varuable: JAVA_HOME should be defined with the full path of JDK 8 installation.
An Internet connection is required to download project dependencies.
### Configuration of development environment:
1. Create a database named: userbase.
2. Create a user: cloudcomp.
3. Run the the scripts: create_db_objects.sql, and baseline_setup.sql using the user: cloudcomp.
4. Run STS.
5. Import the project: Code\cloud-compiler in STS as maven project.
6. Configure the database url, username, and password in the file: database.properties.
7. Right click on the file pom.xml.
8. choose Run As->Maven Build.
9. Fill "clean package".
10. click: Run.
Application supports three types of projects:
* Maven project (in case pom.xml found in the root directory).
* Ant project.
* Java project.

In case Java project detected, application compiles all the java 
Installation:
Copy the web archive: cloud-compiler-0.0.1-SNAPSHOT.war to the directory: %CATALINA_HOME%\webapps (windows), $CATALINA_HOME/webapps (Linux)
