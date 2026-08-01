# S03 Platform Configuration Plan

## Quarkus Mapping Strategy

This plan maps Spring Boot platform configuration to Quarkus equivalents, following the MAPPINGS.md catalog decisions and preserving all functional behavior established in S02.

## REDESIGN Items

### T-001: Remove Spring Boot Application Class
**Finding:** `springboot-annotations-to-quarkus-00000`
**Target design:** → `src/main/java/com/demo/PetClinicApplication.java` (deleted)
**Class:** `infer`

Remove the Spring Boot bootstrap class entirely. Quarkus auto-discovers CDI beans without requiring a main class or `@SpringBootApplication` annotation. This is a fundamental architectural change from Spring's component scanning to Quarkus's CDI-based bean discovery.

**Legacy Evidence:**
- `src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java:7-13`
- Extends `SpringBootServletInitializer` for WAR deployment
- Contains `SpringApplication.run()` bootstrap logic

**Target Contract:**
- No main class required for Quarkus application startup
- CDI beans in `src/main/java/com/demo/` package automatically discovered
- Application starts via `quarkus:dev` or JAR execution without custom bootstrap

### T-002: Convert Spring Boot Parent to Quarkus Platform BOM
**Finding:** `springboot-parent-pom-to-quarkus-00000`, `javaee-pom-to-quarkus-00010`
**Target design:** → `pom.xml:13-18` 
**Class:** `rewrite`

Replace Spring Boot parent POM with Quarkus platform BOM (com.redhat.quarkus.platform version 3.27.3.SP1). The Quarkus BOM provides dependency management for all Quarkus extensions and ensures version compatibility.

**Legacy Evidence:**
- `pom.xml:14-18` - Spring Boot parent version 2.6.2

**Target Contract:**
- Quarkus platform BOM manages all Quarkus extension versions
- Red Hat build of Quarkus 3.27.3.SP1 for enterprise stability
- Eliminates need to specify individual Quarkus dependency versions

### T-003: Replace Spring Boot Maven Plugin with Quarkus Plugin
**Finding:** `springboot-plugins-to-quarkus-0000`, `javaee-pom-to-quarkus-00020`
**Target design:** → `pom.xml:164-184`
**Class:** `rewrite`

Replace `spring-boot-maven-plugin` with `quarkus-maven-plugin` and add native profile configuration for GraalVM native compilation support.

**Legacy Evidence:**
- `pom.xml:164-184` - spring-boot-maven-plugin with build-info execution

**Target Contract:**
- `quarkus:dev` enables hot reload during development
- `quarkus:build` packages application for production deployment
- Native profile supports `quarkus:build -Dquarkus.native.enabled=true`

### T-004: Convert Spring Boot Dependencies to Quarkus Extensions
**Findings:** `javaee-pom-to-quarkus-00030/00040/00050/00060`
**Target design:** → `pom.xml:38-160` (dependencies section)
**Class:** `rewrite`

Replace Spring Boot starter dependencies with Quarkus extensions:

- `spring-boot-starter-actuator` → `quarkus-smallrye-health` (health endpoints at `/q/health`)
- `spring-boot-starter-web` → `quarkus-rest-jackson` (JAX-RS with JSON support)
- `spring-boot-starter-data-jpa` → `quarkus-hibernate-orm` (JPA persistence)
- `spring-boot-starter-validation` → `quarkus-hibernate-validator` (Bean Validation)
- `spring-boot-starter-security` → `quarkus-security` (if security enabled)

**Legacy Evidence:**
- `pom.xml:40-85` - Spring Boot starter dependencies

**Target Contract:**
- REST endpoints available under `/api/` via JAX-RS
- Health checks available under `/q/health` and `/q/health/ready`
- JPA/Hibernate ORM with Panache active record pattern support

### T-005: Remove Spring-Specific Dependencies
**Finding:** `removed-javaee-modules-00020`
**Target design:** → `pom.xml` (dependency exclusions)
**Class:** `rewrite`

Remove or exclude Spring-specific dependencies that have no Quarkus equivalent or are built into Quarkus:

- `springfox-boot-starter` - Replaced by Quarkus native OpenAPI support
- `spring-boot-starter-aop` - AOP built into Quarkus
- `spring-boot-starter-cache` - Caching handled by Quarkus extensions

**Legacy Evidence:**
- `pom.xml:45-50` - Spring AOP and caching starters
- `pom.xml:124-127` - Springfox Swagger dependency

**Target Contract:**
- OpenAPI documentation via Quarkus `quarkus-smallrye-openapi`
- AOP capabilities via Quarkus built-in interceptors
- Caching via `quarkus-cache` extension if needed

### T-006: Update Jakarta XML Binding Dependency
**Findings:** `javax-to-jakarta-dependencies-00001`, `javax-to-jakarta-dependencies-00003`
**Target design:** → `pom.xml:155-159`
**Class:** `rewrite`

Update JAXB API dependency from `javax.xml.bind:jaxb-api` to `jakarta.xml.bind:jakarta.xml.bind-api` to align with Jakarta EE 9+ namespace changes.

**Legacy Evidence:**
- `pom.xml:155-159` - javax.xml.bind dependency version 2.3.0

**Target Contract:**
- Jakarta XML Binding API available for XML processing
- Compatible with Jakarta EE 9+ and Quarkus runtime

### T-007: Convert Server Configuration Properties
**Finding:** `springboot-properties-to-quarkus-00001`
**Target design:** → `src/main/resources/application.properties:20-25`
**Class:** `rewrite`

Convert Spring Boot server properties to Quarkus HTTP configuration:

- `server.port=9966` → `quarkus.http.port=9966`
- `server.servlet.context-path=/petclinic/` → `quarkus.http.context-path=/petclinic/`
- `spring.mvc.pathmatch.matching-strategy=ant_path_matcher` → Quarkus default (no config needed)

**Legacy Evidence:**
- `src/main/resources/application.properties:23-24` - Port and context path
- `src/main/resources/application.properties:28` - Path matching strategy

**Target Contract:**
- Application accessible at `/petclinic/api/*` endpoints
- Port 9966 maintained for backward compatibility
- REST path matching follows JAX-RS standards (no Ant patterns needed)

### T-008: Convert Database Configuration Properties
**Finding:** `springboot-properties-to-quarkus-00002`
**Target design:** → `src/main/resources/application.properties` + profile-specific configs
**Class:** `rewrite`

Convert Spring datasource properties to Quarkus Hibernate ORM configuration:

- `spring.datasource.url=jdbc:postgresql://localhost:5432/petclinic` → `quarkus.datasource.url=jdbc:postgresql://localhost:5432/petclinic`
- `spring.datasource.username=postgres` → `quarkus.datasource.username=postgres`
- `spring.datasource.password=petclinic` → `quarkus.datasource.password=petclinic`
- `spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect` → `quarkus.hibernate-orm.dialect=org.hibernate.dialect.PostgreSQLDialect`

**Legacy Evidence:**
- `src/main/resources/application-postgresql.properties:8-13` - PostgreSQL config

**Target Contract:**
- PostgreSQL as primary database (migration.yaml dbService directive)
- Connection properties externalized for different environments
- Hibernate ORM with PostgreSQL dialect maintained

### T-009: Convert JPA and Hibernate Properties
**Finding:** `springboot-properties-to-quarkus-00003`
**Target design:** → `src/main/resources/application.properties` 
**Class:** `rewrite`

Convert JPA/Hibernate configuration properties:

- `spring.jpa.database=POSTGRESQL` → `quarkus.hibernate-orm.database.generation=validate` (with `quarkus.hibernate-orm.sql-load-script=import.sql`)
- `spring.jpa.hibernate.ddl-auto=none` → `quarkus.hibernate-orm.database.generation=validate` (Flyway handles schema)

**Legacy Evidence:**
- `src/main/resources/application-postgresql.properties:14` - HHHibernate DDL auto
- Schema management via SQL scripts in `src/main/resources/db/postgresql/`

**Target Contract:**
- Database schema validated against existing structure
- SQL import scripts executed on startup (`import.sql`)
- Flyway integration for production schema management

### T-010: Convert Logging Configuration
**Finding:** `springboot-metrics-to-quarkus-0100`
**Target design:** → `src/main/resources/application.properties:33-34`
**Class:** `rewrite`

Convert Spring logging configuration to Quarkus format:

- `logging.level.org.springframework=INFO` → `quarkus.log.category."org.springframework".level=INFO`
- Maintain existing logging levels for backward compatibility

**Legacy Evidence:**
- `src/main/resources/application.properties:33-34` - Spring framework logging level

**Target Contract:**
- Spring framework logging visible at INFO level
- Quarkus logging categories configuration
- Development vs production logging profiles maintained

### T-011: Convert Actuator to Quarkus Health
**Finding:** `springboot-actuator-to-quarkus-0100`
**Target design:** → Health endpoint configuration
**Class:** `infer`

Replace Spring Boot Actuator health endpoints with Quarkus SmallRye Health. This requires configuring health checks and readiness probes that align with the business logic established in S02.

**Legacy Evidence:**
- `pom.xml:40-42` - spring-boot-starter-actuator dependency
- Health endpoint at `/actuator/health` (inferred from Spring Boot defaults)

**Target Contract:**
- Health endpoint available at `/q/health` (application status)
- Readiness endpoint at `/q/health/ready` (database connectivity)
- Liveness endpoint at `/q/health/live` (application responsiveness)
- Database health check confirms PostgreSQL connectivity

**OPEN DESIGN Decision Required:**
- Health check implementation strategy (built-in vs custom checks)
- Integration with S02 service layer for business health indicators

## HARVEST Items

### T-012: Preserve Database Profile Configurations
**Target design:** → Profile-specific property files maintained
**Class:** `rewrite`

Maintain legacy profile-specific configurations for development flexibility:

- `application-hsqldb.properties` for in-memory development testing
- `application-mysql.properties` for MySQL development environment  
- `application-postgresql.properties` for PostgreSQL primary database

**Legacy Evidence:**
- `src/main/resources/application-{hsqldb,mysql,postgresql}.properties`

**Target Contract:**
- Profile-based configuration maintained via Quarkus config profiles (`%dev`, `%prod`)
- Default environment uses PostgreSQL profile
- Development profiles remain available for testing different backends

### T-013: Preserve Security Configuration Property
**Target design:** → `src/main/resources/application.properties:41`
**Class:** `infer`

Maintain `petclinic.security.enable=false` property for backward compatibility with S02 security configuration decisions.

**Legacy Evidence:**
- `src/main/resources/application.properties:41` - `petclinic.security.enable=false`

**Target Contract:**
- Security flag preserved in application configuration
- Integration with Quarkus security configuration (future S-XX story)
- Default behavior remains security disabled

## Test Coverage Requirements

### T-014: Platform Configuration Characterization Tests
**Target design:** → Test coverage for Quarkus platform behavior
**Class:** `infer`

Create tests that verify the migrated platform configuration:

- Maven build succeeds with Quarkus platform
- Quarkus dev mode starts without errors
- Health endpoint `/q/health` responds successfully
- Database connectivity works with PostgreSQL primary
- Context path `/petclinic/` serves application correctly
- Property profiles work correctly for different databases

**Contract Verification:**
- Application builds and starts on Quarkus platform
- Health checks validate application readiness
- Integration with S02 domain functionality maintained

## Implementation Order

1. **Extensions and BOM** (T-002, T-003, T-004, T-005, T-006)
2. **Configuration Properties** (T-007, T-008, T-009, T-010, T-011)
3. **Application Bootstrap** (T-001)
4. **Preservation and Testing** (T-012, T-013, T-014)

This order ensures the build system works before attempting to run or test the application, following the dependency chain established in migration/dependency-order.md.
