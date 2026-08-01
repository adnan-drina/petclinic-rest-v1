# S03 Platform Configuration Specification

## Observed Legacy Behavior

This story modernizes the Spring Boot platform configuration to Quarkus, converting the build system, application bootstrap, and configuration properties while preserving all functional behavior established in S02.

### Main Application Bootstrap

**File:** `src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java`

```java
package org.springframework.samples.petclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class PetClinicApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PetClinicApplication.class, args);
    }
}
```

**Legacy Behavior:**
- Spring Boot `@SpringBootApplication` enables auto-configuration, component scanning, and configuration properties
- Main method bootstraps the Spring application context
- Extends `SpringBootServletInitializer` for WAR deployment support
- Quarkus does not require a main class - CDI beans are auto-discovered without bootstrap

### Build Configuration

**File:** `pom.xml`

**Legacy Spring Boot Parent Configuration:**
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.6.2</version>
</parent>
```

**Legacy Spring Boot Dependencies:**
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- ... other Spring Boot dependencies ... -->
</dependencies>
```

**Legacy Maven Plugin:**
```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <executions>
        <execution>
            <goals>
                <goal>build-info</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

**Legacy Behavior:**
- Spring Boot parent manages dependency versions and provides sensible defaults
- `spring-boot-starter-actuator` provides health/metrics endpoints at `/actuator`
- `spring-boot-starter-web` enables REST web services with embedded Tomcat
- `spring-boot-maven-plugin` packages and runs the application

### Application Properties Configuration

**File:** `src/main/resources/application.properties`

```properties
spring.profiles.active=hsqldb,spring-data-jpa
server.port=9966
server.servlet.context-path=/petclinic/
spring.mvc.pathmatch.matching-strategy=ant_path_matcher
logging.level.org.springframework=INFO
petclinic.security.enable=false
```

**File:** `src/main/resources/application-hsqldb.properties`

```properties
spring.datasource.url=jdbc:hsqldb:mem:petclinic
spring.datasource.username=sa 
spring.datasource.password=
spring.jpa.database=HSQL
spring.jpa.database-platform=org.hibernate.dialect.HSQLDialect
```

**File:** `src/main/resources/application-mysql.properties`

```properties
spring.datasource.url = jdbc:mysql://localhost:3306/petclinic?useUnicode=true
spring.datasource.username=pc
spring.datasource.password=petclinic
spring.jpa.database=MYSQL
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

**File:** `src/main/resources/application-postgresql.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/petclinic
spring.datasource.username=postgres
spring.datasource.password=petclinic
spring.jpa.database=POSTGRESQL
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

**Legacy Behavior:**
- Multiple Spring profiles for different database backends (HSQLDB, MySQL, PostgreSQL)
- Server runs on port 9966 with context path `/petclinic/`
- Application path matching uses Ant-style patterns
- Security is disabled by default via `petclinic.security.enable=false`
- Database connections use standard JDBC URLs with username/password authentication

## Target API Contract

### Build System Contract
- **Maven build succeeds** with Quarkus platform BOM (com.redhat.quarkus.platform 3.27.3.SP1)
- **Quarkus dev mode** starts without errors on port 8080
- **Native compilation** completes successfully
- **Health endpoint** available at `/q/health` (replaces `/actuator/health`)
- **All legacy dependencies** converted to Quarkus equivalents

### Platform Configuration Contract
- **Context path preserved:** `/petclinic/` maintained for backward compatibility
- **Database configuration externalized:** PostgreSQL as primary, HSQLDB/MySQL profiles optional
- **Security configuration preserved:** `petclinic.security.enable` maintained
- **Logging configuration maintained:** Spring logging levels converted to Quarkus equivalents

### Database Contract
- **Primary database:** PostgreSQL only (migration.yaml dbService directive)
- **Connection properties externalized:** `jdbc.url`, `jdbc.username`, `jdbc.password` preserved
- **Hibernate ORM maintained:** JPA entities and queries unchanged
- **Multi-backend support:** Legacy profiles available for development/testing

## Evidence Sources

- **Main class removal:** `src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java:7-13`
- **Spring Boot parent:** `pom.xml:14-18` with version 2.6.2
- **Spring dependencies:** `pom.xml:40-85` (actuator, web, data-jpa, security, validation)
- **Maven plugins:** `pom.xml:164-336` (spring-boot-maven-plugin, jacoco, jib)
- **Property configuration:** `src/main/resources/application.properties:19-42`
- **Database profiles:** `application-{hsqldb,mysql,postgresql}.properties` with JDBC URLs
- **Context path:** `application.properties:24` - `server.servlet.context-path=/petclinic/`
- **Security flag:** `application.properties:41` - `petclinic.security.enable=false`
