# S03 Platform Configuration Tasks

## Task Overview

This task list modernizes the Spring Boot platform configuration to Quarkus, converting the build system, application bootstrap, and configuration properties while preserving all functional behavior established in S02.

**Package Rename:** `org.springframework.samples.petclinic` → `com.demo` (full prefix replacement)

**Deployment:** This story has `deploy=true` - acceptance.path `/petclinic/api/vets` will be implemented with real @Path substance

**Legacy UI Surface Waiver:** This story modernizes platform configuration only. REST API endpoints remain unchanged and are owned by S02. The acceptance endpoint `/petclinic/api/vets` serves to verify platform functionality but does not constitute UI surface changes.

---

## Extensions and BOM Tasks (Rewrite First)

#### T-001: Convert Spring Boot Parent to Quarkus Platform BOM
**Class:** `rewrite`  
**Findings:** `springboot-parent-pom-to-quarkus-00000`, `javaee-pom-to-quarkus-00010`

Replace Spring Boot parent POM with Quarkus platform BOM.

**Target design:** → `pom.xml:13-18`

**Changes:**
- Replace `<parent>` section with Quarkus BOM dependency management
- Set `com.redhat.quarkus.platform` version to `3.27.3.SP1`
- Remove Spring Boot version management

**Evidence:**
- `pom.xml:14-18` - Spring Boot parent version 2.6.2
- Findings rule specifies Quarkus BOM adoption

**Verification:**
- Maven build resolves Quarkus dependencies without version conflicts
- Quarkus platform manages all extension versions

#### T-002: Replace Spring Boot Maven Plugin with Quarkus Plugin
**Class:** `rewrite`  
**Findings:** `springboot-plugins-to-quarkus-0000`, `javaee-pom-to-quarkus-00020`

Replace `spring-boot-maven-plugin` with `quarkus-maven-plugin`.

**Target design:** → `pom.xml:164-184`

**Changes:**
- Remove `spring-boot-maven-plugin` configuration
- Add `quarkus-maven-plugin` with build-info execution
- Configure native compilation profile

**Evidence:**
- `pom.xml:164-184` - spring-boot-maven-plugin with build-info goals

**Verification:**
- `mvn quarkus:dev` starts application in development mode
- `mvn quarkus:build` packages application successfully

#### T-003: Convert Spring Boot Dependencies to Quarkus Extensions
**Class:** `rewrite`  
**Findings:** `javaee-pom-to-quarkus-00030/00040/00050/00060`

Replace Spring Boot starter dependencies with Quarkus extensions.

**Target design:** → `pom.xml:38-160` (dependencies section)

**Changes:**
- `spring-boot-starter-actuator` → `quarkus-smallrye-health`
- `spring-boot-starter-web` → `quarkus-rest-jackson` 
- `spring-boot-starter-data-jpa` → `quarkus-hibernate-orm`
- `spring-boot-starter-validation` → `quarkus-hibernate-validator`
- Update MapStruct processor configuration for Quarkus

**Evidence:**
- `pom.xml:40-85` - Spring Boot starter dependencies
- `pom.xml:139-147` - MapStruct dependencies

**Verification:**
- All Quarkus extensions resolve without conflicts
- JAX-RS and JPA capabilities available

#### T-004: Remove Spring-Specific Dependencies and Update Generated Code
**Class:** `rewrite`  
**Findings:** `removed-javaee-modules-00020`

Remove or exclude Spring-specific dependencies with no Quarkus equivalent and update generated DTO/mapper code.

**Target design:** → `pom.xml` (dependency exclusions)  
**Owns:** `src/main/java/org/springframework/samples/petclinic/dto/OwnerAllOfDto.java`  
**Owns:** `src/main/java/org/springframework/samples/petclinic/dto/OwnerDto.java`  
**Owns:** `src/main/java/org/springframework/samples/petclinic/dto/OwnerFieldsDto.java`  
**Owns:** `src/main/java/org/springframework/samples/petclinic/dto/PetAllOfDto.java`  
**Owns:** `src/main/java/org/springframework/samples/petclinic/dto/PetDto.java`  
**Owns:** `src/main/java/org/springframework/samples/petclinic/dto/PetFieldsDto.java`  
**Owns:** `src/main/java/org/springframework/samples/petclinic/dto/PetTypeDto.java`  
**Owns:** `src/main/java/org/springframework/samples/petclinic/dto/RestErrorDto.java`  
**Owns:** `src/main/java/org/springframework/samples/petclinic/dto/RoleDto.java`  
**Owns:** `src/main/java/org/springframework/samples/petclinic/dto/SpecialtyDto.java`  
**Owns:** `src/main/java/org/springframework/samples/petclinic/dto/UserDto.java`  
**Owns:** `src/main/java/org/springframework/samples/petclinic/dto/ValidationMessageDto.java`  
**Owns:** `src/main/java/org/springframework/samples/petclinic/dto/VetDto.java`  
**Owns:** `src/main/java/org/springframework/samples/petclinic/dto/VisitAllOfDto.java`  
**Owns:** `src/main/java/org/springframework/samples/petclinic/dto/VisitDto.java`  
**Owns:** `src/main/java/org/springframework/samples/petclinic/dto/VisitFieldsDto.java`  
**Owns:** `src/main/java/org/springframework/samples/petclinic/dto/ValidationMessageDto.java`  
**Owns:** `projects/legacy/target/generated-sources/annotations/org/springframework/samples/petclinic/mapper/OwnerMapperImpl.java`  
**Owns:** `projects/legacy/target/generated-sources/annotations/org/springframework/samples/petclinic/mapper/PetMapperImpl.java`  
**Owns:** `projects/legacy/target/generated-sources/annotations/org/springframework/samples/petclinic/mapper/PetTypeMapperImpl.java`  
**Owns:** `projects/legacy/target/generated-sources/annotations/org/springframework/samples/petclinic/mapper/SpecialtyMapperImpl.java`  
**Owns:** `projects/legacy/target/generated-sources/annotations/org/springframework/samples/petclinic/mapper/UserMapperImpl.java`  
**Owns:** `projects/legacy/target/generated-sources/annotations/org/springframework/samples/petclinic/mapper/VetMapperImpl.java`  
**Owns:** `projects/legacy/target/generated-sources/annotations/org/springframework/samples/petclinic/mapper/VisitMapperImpl.java`

**Changes:**
- Remove `springfox-boot-starter` (Swagger dependency)
- Remove `spring-boot-starter-aop` (built into Quarkus)
- Remove `spring-boot-starter-cache` (replaced by Quarkus caching)
- Update OpenAPI generation to use Quarkus native support
- Update all DTO classes to use jakarta imports instead of javax
- Update all generated mapper implementations to use jakarta imports
- Update MapStruct processor configuration for Jakarta EE 9+

**Evidence:**
- `pom.xml:45-50` - Spring AOP and caching starters
- `pom.xml:124-127` - Springfox Swagger dependency
- `src/main/java/org/springframework/samples/petclinic/dto/*.java` - Generated DTO classes
- `projects/legacy/target/generated-sources/annotations/org/springframework/samples/petclinic/mapper/*.java` - Generated mapper implementations

**Verification:**
- No Spring framework dependencies remain (except test scope)
- OpenAPI documentation available via Quarkus
- All DTOs and mappers use Jakarta EE 9+ imports

#### T-005: Update Jakarta XML Binding Dependency
**Class:** `rewrite`  
**Findings:** `javax-to-jakarta-dependencies-00001`, `javax-to-jakarta-dependencies-00003`

Update JAXB API dependency from javax to jakarta namespace.

**Target design:** → `pom.xml:155-159`

**Changes:**
- Replace `javax.xml.bind:jaxb-api` with `jakarta.xml.bind:jakarta.xml-bind-api`
- Update version to compatible Jakarta EE 9+ release

**Evidence:**
- `pom.xml:155-159` - javax.xml.bind dependency version 2.3.0

**Verification:**
- Jakarta XML Binding API available for XML processing
- Compatible with Quarkus Jakarta EE 9+ runtime

---

## Configuration Properties Tasks (Rewrite)

#### T-006: Convert Server Configuration Properties
**Class:** `rewrite`  
**Findings:** `springboot-properties-to-quarkus-00001`

Convert Spring Boot server properties to Quarkus HTTP configuration.

**Target design:** → `src/main/resources/application.properties:20-25`

**Changes:**
- `server.port=9966` → `quarkus.http.port=9966`
- `server.servlet.context-path=/petclinic/` → `quarkus.http.context-path=/petclinic/`
- Remove `spring.mvc.pathmatch.matching-strategy=ant_path_matcher` (JAX-RS default)

**Evidence:**
- `src/main/resources/application.properties:23-24` - Port and context path
- `src/main/resources/application.properties:28` - Path matching strategy

**Verification:**
- Application accessible at `/petclinic/api/*` endpoints
- Port 9966 maintained for backward compatibility

#### T-007: Convert Database Configuration Properties
**Class:** `rewrite`  
**Findings:** `springboot-properties-to-quarkus-00002`

Convert Spring datasource properties to Quarkus Hibernate ORM configuration.

**Target design:** → `src/main/resources/application.properties` + profile configs

**Changes:**
- `spring.datasource.url=jdbc:postgresql://localhost:5432/petclinic` → `quarkus.datasource.url=jdbc:postgresql://localhost:5432/petclinic`
- `spring.datasource.username=postgres` → `quarkus.datasource.username=postgres`
- `spring.datasource.password=petclinic` → `quarkus.datasource.password=petclinic`
- Update all profile-specific configurations (HSQLDB, MySQL, PostgreSQL)

**Evidence:**
- `src/main/resources/application-postgresql.properties:8-13` - PostgreSQL config
- `src/main/resources/application-hsqldb.properties:7-11` - HSQLDB config
- `src/main/resources/application-mysql.properties:8-12` - MySQL config

**Verification:**
- PostgreSQL as primary database (migration.yaml dbService)
- Connection properties externalized for environment-specific use

#### T-008: Convert JPA and Hibernate Properties
**Class:** `rewrite`  
**Findings:** `springboot-properties-to-quarkus-00003`

Convert JPA/Hibernate configuration properties to Quarkus format.

**Target design:** → `src/main/resources/application.properties`

**Changes:**
- `spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect` → `quarkus.hibernate-orm.dialect=org.hibernate.dialect.PostgreSQLDialect`
- `spring.jpa.hibernate.ddl-auto=none` → `quarkus.hibernate-orm.database.generation=validate`
- Add `quarkus.hibernate-orm.sql-load-script=import.sql` for seed data

**Evidence:**
- `src/main/resources/application-postgresql.properties:14` - Hibernate DDL auto
- Schema management via SQL scripts in legacy project

**Verification:**
- Database schema validated against existing structure
- SQL import scripts execute on startup for seed data

#### T-009: Convert Logging Configuration
**Class:** `rewrite`  
**Findings:** `springboot-metrics-to-quarkus-0100`

Convert Spring logging configuration to Quarkus format.

**Target design:** → `src/main/resources/application.properties:33-34`

**Changes:**
- `logging.level.org.springframework=INFO` → `quarkus.log.category."org.springframework".level=INFO`
- Maintain existing logging configuration for compatibility

**Evidence:**
- `src/main/resources/application.properties:33-34` - Spring framework logging

**Verification:**
- Spring framework logging visible at INFO level
- Quarkus logging categories configured correctly

---

## Application Bootstrap Tasks (Infer)

#### T-010: Remove Spring Boot Application Class
**Class:** `infer`  
**Findings:** `springboot-annotations-to-quarkus-00000`

Remove the Spring Boot bootstrap class entirely - Quarkus auto-discovers CDI beans.

**Target design:** → `src/main/java/com/demo/PetClinicApplication.java` (deleted)

**Changes:**
- Delete `PetClinicApplication.java` file
- Ensure no main class required for Quarkus startup
- Verify CDI bean discovery works without `@SpringBootApplication`

**Evidence:**
- `src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java:7-13`
- Extends `SpringBootServletInitializer` for WAR deployment
- Contains `SpringApplication.run()` bootstrap logic

**Target Contract:**
- No main class required for Quarkus application startup
- CDI beans in `src/main/java/com/demo/` package automatically discovered
- Application starts via `quarkus:dev` or JAR execution

**Package Rename:** `org.springframework.samples.petclinic` → `com.demo`

#### T-011: Convert Actuator to Quarkus Health Endpoints
**Class:** `infer`  
**Findings:** `springboot-actuator-to-quarkus-0100`

Replace Spring Boot Actuator health endpoints with Quarkus SmallRye Health.

**Target design:** → Health endpoint configuration

**Changes:**
- Configure Quarkus SmallRye Health extension
- Create database connectivity health check
- Ensure `/q/health` endpoint provides application status
- Integrate with PostgreSQL datasource for readiness checks

**Evidence:**
- `pom.xml:40-42` - spring-boot-starter-actuator dependency
- Spring Boot Actuator provides `/actuator/health` endpoint

**Target Contract:**
- Health endpoint available at `/q/health` (application status)
- Readiness endpoint at `/q/health/ready` (database connectivity)
- Liveness endpoint at `/q/health/live` (application responsiveness)
- Database health check confirms PostgreSQL connectivity

**Acceptance Implementation:** Implement real @Path for `/petclinic/api/vets` endpoint with actual business logic to verify platform functionality

---

## Preservation Tasks (Infer)

#### T-012: Preserve Database Profile Configurations
**Class:** `infer`

Maintain legacy profile-specific configurations for development flexibility.

**Target design:** → Profile-specific property files maintained

**Changes:**
- Maintain `application-hsqldb.properties` for in-memory testing
- Maintain `application-mysql.properties` for MySQL development
- Maintain `application-postgresql.properties` for PostgreSQL primary
- Convert to Quarkus config profiles (`%dev`, `%prod`)

**Evidence:**
- `src/main/resources/application-{hsqldb,mysql,postgresql}.properties`

**Target Contract:**
- Profile-based configuration maintained via Quarkus config profiles
- Default environment uses PostgreSQL profile
- Development profiles available for testing different backends

#### T-013: Preserve Security Configuration Property
**Class:** `infer`

Maintain `petclinic.security.enable=false` property for backward compatibility.

**Target design:** → `src/main/resources/application.properties:41`

**Changes:**
- Preserve `petclinic.security.enable=false` in application configuration
- Document integration point for future Quarkus security implementation
- Ensure flag compatibility with S02 security decisions

**Evidence:**
- `src/main/resources/application.properties:41` - `petclinic.security.enable=false`

**Target Contract:**
- Security flag preserved in application configuration
- Integration point for future Quarkus security story
- Default behavior remains security disabled

---

## Test Coverage Tasks (Infer - S-INFTEST)

#### T-014: Platform Configuration Characterization Tests
**Class:** `infer`  
**Coverage Required:** ≥80% new-code coverage for platform migration

Create comprehensive tests that verify the migrated platform configuration.

**Target design:** → `src/test/java/com/demo/` test suite for Quarkus platform behavior

**Changes:**
- Create `PlatformConfigVerificationTest.java` for build and dev mode verification
- Create `HealthEndpointTest.java` for `/q/health` endpoint testing  
- Create `DatabaseConnectivityTest.java` for PostgreSQL connection validation
- Create `ContextPathTest.java` for `/petclinic/` path verification
- Create `PropertyProfilesTest.java` for all three database profiles testing
- Create `AcceptanceEndpointTest.java` for `/petclinic/api/vets` endpoint with real VetDto response

**Evidence:**
- Platform contracts from brief section
- Legacy Spring Boot configuration files
- S02 functional requirements must remain intact

**Contract Verification:**
- Application builds and starts on Quarkus platform successfully
- Health checks validate application readiness
- Integration with S02 domain functionality maintained
- Full build pipeline green: compile → test → package → native build

**Acceptance Test Implementation:** Create real `/petclinic/api/vets` endpoint implementation in `VetRestController.java` with actual VetDto collection response to verify end-to-end platform functionality

---

## Implementation Verification

All tasks must pass the following verification criteria:

1. **Maven Build:** `mvn -q clean test` succeeds
2. **Dev Mode:** `mvn quarkus:dev` starts without errors  
3. **Health Endpoint:** `/q/health` responds with application status
4. **Database Connectivity:** PostgreSQL connection validated
5. **Context Path:** Application accessible at `/petclinic/api/*`
6. **Acceptance Endpoint:** `/petclinic/api/vets` returns VetDto collection
7. **Native Build:** `mvn quarkus:build -Dquarkus.native.enabled=true` succeeds

---

## Task Dependencies

1. **T-001 → T-002 → T-003 → T-004 → T-005:** Build system conversion (blocking)
2. **T-006 → T-007 → T-008 → T-009:** Configuration conversion (blocking)  
3. **T-010 → T-011:** Application bootstrap (blocking)
4. **T-012 → T-013:** Preservation (non-blocking)
5. **T-014:** Test coverage (verification)

The blocking chain ensures the build system works before attempting to configure, bootstrap, or test the application, following the dependency chain established in migration/dependency-order.md.
