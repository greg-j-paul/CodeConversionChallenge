# Task

## Homework Task
Migrate a specific legacy JBoss Java application to a more modern platform according to the following directives:
- The application for you to migrate is the ‘kitchensink’ JBoss application available in the Red Hat JBoss EAP Quickstarts GitHub repository (no need to migrate any of the other applications listed there)
- The migrated application runtime you must target is the latest stable version of Spring Boot or Quakus (your choice) based on Java 21 (you can host the running application on your laptop, in the cloud, or wherever - you don’t need to target OpenShift as the host environment necessarily, which the application’s README discusses in places).
- Create a new personal public GitHub project and host the source code for your new migrated application there (for just ‘kitchensink’ only). Include a new README where you outline the steps a developer would need to take to build and run your migrated application.
- Try to approach this migration process in the way you might do if the legacy application codebase was far larger, in terms of how you break up the problem into more management tasks, addressing the sort of infrastructure, scaffolding, and software engineering principles you would need to apply to help mitigate risk in the migration work and ensure the quality of what is migrated (during the subsequent playback session, you will be asked questions on the approach you took).
- OPTIONAL STRETCH GOAL: Modify the application to work against a MongoDB database rather than the existing relational target database.


# Setting up source project
1. cd into repo folder
2. Clone [jboss quickstart repo](https://github.com/jboss-developer/jboss-eap-quickstarts/tree/8.0.x)
3. Install python package TODO: add code migrator repo link here

# MAC os installation

## Java installation
1. Install [sdkman](https://sdkman.io/).
2. Open a new terminal window
3. Run `sdk install java 21.0.6-tem`
4. By default it runs through and sets the last install version as default however this can be manually set with `sdk default`
5. `sdk install springboot`

## Maven installation
Install maven using `brew install maven`

## Mongodb installation
`brew tap mongodb/brew`
`brew update`
`brew install mongodb-community@8.0`
`brew services start mongodb-community@8.0`
`brew services stop mongodb-community@8.0`

## Redhat jboss enterprise
Follow along with [official guide](https://docs.redhat.com/en/documentation/red_hat_jboss_enterprise_application_platform/8.0/html-single/red_hat_jboss_enterprise_application_platform_installation_methods/index?extIdCarryOver=true&sc_cid=701f2000001Css5AAC#jboss-eap-8-installation-methods_default)
NOTE: this step will require you to create a redhat account and verify it. For this challenge i used the .jar file as the gui installer link was unavailable.

# Environment variables
Create a .env file with the following content 
```
EAP_HOME=~/EAP-8.0.0
```

# Running original code
Source the .env file and run according to instructions in [kitchensink readme](https://github.com/jboss-developer/jboss-eap-quickstarts/tree/8.0.x/kitchensink)
In a terminal run:
1. `$EAP_HOME/bin/standalone.sh`
2. `mvn wildfly:deploy`
3. `mvn verify -Parq-remot`


# Running the transformation
See preso


# Using transformed code

## Running the tests
Tests use a `mongodb` docker image to run tests please use `mvn test` from terminal.

## Running the Spring boot code
In a terminal please run `mvn spring-boot:run -X`
Once this is done please navigate to `http://localhost:8081/kitchensink/index`

## Notes:
Code for `kitchensink` left as is, will startup on port `8080`. 

Springboot application is set to startup on `8081` and uses a local mongdb instance. To edit these settings please go to `transformed_code/src/main/resources/application.properties`.