# Kavka backend

For building and running the application we need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Git](https://git-scm.com/downloads)
- [Maven 3](https://maven.apache.org)

## Clone the kavka backend

Once the Git SCM is installed successfully in the machine we can use it to clean the repository in the local machine. As this is a private repository the valid credential is required for the access.

```
git clone https://gitlab.com/mghimire/my-direct-art-node.git
```

## Running the application locally

There are several ways to run a Spring Boot application on the local machine. One way is to execute the `main` method in the `com.kavka.apiservices.ApiServicesApplication` class from the IDE.

If the maven is installed in the machine then we can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

Alternatively, it is not neccessary to install maven in the machine as the executable file is already included inside the repository itself.

```shell
./mvnw spring-boot:run
```

After running above commands the application will run by default in port `9999`. 
