# S02 Core Domain Modernization - Implementation Tasks

## Package Structure Setup

#### T-001: Create target package directory structure

|**Class**: rewrite
|
|**Goal**: Create `src/main/java/com/demo` directory structure with .gitkeep files
|
|**Target design**: Create `src/main/java/com/demo` directory structure
|
|**Task Details**:
- Create `src/main/java/com/demo` directory structure
- Add `.gitkeep` files to maintain empty directory structure in git
- Verify package naming matches `migration.yaml` targetPackage exactly
- **Required**: Directory structure must contain `.gitkeep` or `package-info.java` for git commitability

|**Target**: → `src/main/java/com/demo/`
|**Owns**: Empty directory creation (required for commitability)

---

## Extension and Configuration Setup

#### T-002: Update Quarkus dependencies and remove Spring Boot

|**Class**: rewrite
|
|**Findings**: spring-components-00001, spring-components-00002
|
|**Goal**: Replace Spring Boot dependencies with Quarkus equivalents
|
|**Target design**: Update pom.xml and application configuration for Quarkus
|
|**Task Details**:
- Remove Spring Boot starter dependencies (spring-boot-starter-web, spring-boot-starter-data-jpa, etc.)
- Add Quarkus dependencies: quarkus-rest, quarkus-hibernate-orm, quarkus-jdbc-postgresql/hsqldb, quarkus-smallrye-health
- Update to Quarkus 3.27.3.SP1 with Red Hat platform BOM
- Remove Spring Boot plugin and add Quarkus Maven plugin
- Update Java version to 21 and set JAVA_HOME_21
- Remove Spring Boot main application class and auto-configuration
- Handle AspectJ dependencies for CallMonitoringAspect removal

|**Target**: → `pom.xml` dependencies and build configuration
|**Owns**: `/projects/legacy/pom.xml` AspectJ dependencies for CallMonitoringAspect removal

---

## Model Layer Modernization (HARVEST)

#### T-003: Harvest JPA entities with Jakarta imports

|**Class**: rewrite
|
|**Findings**: javax-to-jakarta-import-00001
|
|**Goal**: Convert all 11 JPA entities from javax to jakarta imports
|
|**Target design**: → `src/main/java/com/demo/model/*.java`
|
|**Task Details**:
- Copy entities from `migration/staging/src/main/java/org/springframework/samples/petclinic/model/`
- Update package: `org.springframework.samples.petclinic.model` → `com.demo.model`
- Replace imports:
  - `javax.persistence.*` → `jakarta.persistence.*`
  - `javax.validation.constraints.*` → `jakarta.validation.constraints.*`
- Preserve all JPA annotations: `@Entity`, `@Table`, `@Column`, `@OneToMany`, `@ManyToOne`, `@JoinColumn`
- Preserve validation annotations: `@NotEmpty`, `@Digits`
- Maintain entity relationships and fetch strategies
- Preserve business methods (getPets(), addPet(), etc.)

|**Target**: → `src/main/java/com/demo/model/` (Owner, Pet, Visit, PetType, Specialty, Vet, Role, User, BaseEntity, NamedEntity, Person)
|**Owns**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/model/*.java`

#### T-004: Harvest repository interfaces with package updates

|**Class**: rewrite
|
|**Goal**: Update repository interface packages without changing functionality
|
|**Target design**: → `src/main/java/com/demo/repository/*.java`
|
|**Task Details**:
- Copy repository interfaces from `migration/staging/src/main/java/org/springframework/samples/petclinic/repository/`
- Update package: `org.springframework.samples.petclinic.repository` → `com.demo.repository`
- Preserve all interface method signatures unchanged
- Maintain generic type parameters and return types
- Update any imports to use target package
- Handle Spring Data JPA interfaces (SpringDataOwnerRepository, etc.)

|**Target**: → `src/main/java/com/demo/repository/` (OwnerRepository, PetRepository, PetTypeRepository, SpecialtyRepository, UserRepository, VetRepository, VisitRepository, SpringData* interfaces)
|**Owns**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/repository/*.java`

#### T-005: Harvest DTOs with Jakarta validation imports

|**Class**: rewrite
|
|**Findings**: javax-to-jakarta-import-00001
|
|**Goal**: Update all DTO classes from javax to jakarta validation
|
|**Target design**: → `src/main/java/com/demo/dto/*.java`
|
|**Task Details**:
- Copy DTOs from `target/generated-sources/openapi/src/main/java/org/springframework/samples/petclinic/dto/`
- Update package: `org.springframework.samples.petclinic.dto` → `com.demo.dto`
- Replace `javax.validation.*` imports → `jakarta.validation.*`
- Preserve all JSON serialization annotations
- Maintain OpenAPI generation compatibility
- Update field validation constraints appropriately

|**Target**: → `src/main/java/com/demo/dto/*.java` (OwnerDto, PetDto, VisitDto, VetDto, PetTypeDto, SpecialtyDto, RoleDto, UserDto, RestErrorDto, ValidationMessageDto)
|**Owns**: `target/generated-sources/openapi/src/main/java/org/springframework/samples/petclinic/dto/*.java`

---

## Service Layer Modernization (REDESIGN)

#### T-006: Harvest MapStruct mappers with Jakarta updates

|**Class**: rewrite
|
|**Findings**: javax-to-jakarta-import-00001
|
|**Goal**: Update mapper interfaces and regenerate implementations
|
|**Target design**: → `src/main/java/com/demo/mapper/*.java`
|
|**Task Details**:
- Copy mapper interfaces from `src/main/java/org/springframework/samples/petclinic/mapper/`
- Update package: `org.springframework.samples.petclinic.mapper` → `com.demo.mapper`
- Regenerate mapper implementations using MapStruct
- Update component scanning for Quarkus: `@Component` → no annotation needed (auto-discovery)
- Ensure DTO mappings use updated target package
- Verify bidirectional mappings between entities and DTOs

|**Target**: → `src/main/java/com/demo/mapper/*.java` (OwnerMapper, PetMapper, PetTypeMapper, SpecialtyMapper, UserMapper, VetMapper, VisitMapper)
|**Owns**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/OwnerMapper.java`, '/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/PetMapper.java', '/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/PetTypeMapper.java', '/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/SpecialtyMapper.java', '/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/UserMapper.java', '/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/VetMapper.java', '/projects/legacy/src/main/java/org/springframework/samples/petclinic/mapper/VisitMapper.java', '/projects/legacy/target/generated-sources/annotations/org/springframework/samples/petclinic/mapper/OwnerMapperImpl.java', '/projects/legacy/target/generated-sources/annotations/org/springframework/samples/petclinic/mapper/PetMapperImpl.java', '/projects/legacy/target/generated-sources/annotations/org/springframework/samples/petclinic/mapper/PetTypeMapperImpl.java', '/projects/legacy/target/generated-sources/annotations/org/springframework/samples/petclinic/mapper/SpecialtyMapperImpl.java', '/projects/legacy/target/generated-sources/annotations/org/springframework/samples/petclinic/mapper/UserMapperImpl.java', '/projects/legacy/target/generated-sources/annotations/org/springframework/samples/petclinic/mapper/VetMapperImpl.java', '/projects/legacy/target/generated-sources/annotations/org/springframework/samples/petclinic/mapper/VisitMapperImpl.java'

#### T-007: Convert ClinicServiceImpl to Quarkus CDI

|**Class**: rewrite
|
|**Findings**: springboot-di-to-quarkus-00003, transaction-to-quarkus-00003, springboot-cache-to-quarkus-00000
|
|**Goal**: Convert Spring @Service to Quarkus @ApplicationScoped with CDI injection
|
|**Target design**: → `src/main/java/com/demo/service/ClinicServiceImpl.java`
|
|**Task Details**:
- Copy from `migration/staging/src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java`
- Update package: `org.springframework.samples.petclinic.service` → `com.demo.service`
- Replace `@Service` → `@ApplicationScoped`
- Replace `@Autowired` constructor injection → `@Inject` (CDI)
- Replace `@Transactional` → `org.hibernate.annotations.Transactional` or remove for read-only methods
- Remove `@Cacheable("vets")` annotation (caching decision deferred)
- Update repository imports to use target package
- Preserve all business logic and method signatures
- Maintain exception handling (DataAccessException)
- Target contract: @ApplicationScoped with CDI constructor injection, thread-safe via ConcurrentHashMap

|**Target**: → `src/main/java/com/demo/service/ClinicServiceImpl.java`
|**Owns**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java`

#### T-008: Convert UserServiceImpl to Quarkus CDI

|**Class**: rewrite
|
|**Findings**: springboot-di-to-quarkus-00003, transaction-to-quarkus-00003
|
|**Goal**: Convert Spring @Service to Quarkus @ApplicationScoped with CDI injection
|
|**Target design**: → `src/main/java/com/demo/service/UserServiceImpl.java`
|
|**Task Details**:
- Copy from `migration/staging/src/main/java/org/springframework/samples/petclinic/service/UserServiceImpl.java`
- Update package: `org.springframework.samples.petclinic.service` → `com.demo.service`
- Replace `@Service` → `@ApplicationScoped`
- Replace `@Autowired` field/constructor injection → `@Inject`
- Replace `@Transactional` → Hibernate `@Transactional` or programmatic transaction
- Update repository imports to use target package
- Preserve all user management business logic
- Maintain exception handling patterns
- Target contract: @ApplicationScoped with CDI constructor injection, thread-safe by design

|**Target**: → `src/main/java/com/demo/service/UserServiceImpl.java`
|**Owns**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/service/UserServiceImpl.java`

---

## Repository Implementation Layer Modernization (REDESIGN)

#### T-009: Convert JDBC repository implementations to CDI

|**Class**: rewrite
|
|**Findings**: springboot-di-to-quarkus-00003
|
|**Goal**: Convert 7 JDBC repository implementations from Spring @Repository to Quarkus CDI
|
|**Target design**: → `src/main/java/com/demo/repository/jdbc/*.java`
|
|**Task Details**:
- Copy JDBC implementations from `migration/staging/src/main/java/org/springframework/samples/petclinic/repository/jdbc/`
- Update package: `org.springframework.samples.petclinic.repository.jdbc` → `com.demo.repository.jdbc`
- Remove `@Repository` and `@Profile` annotations
- Replace `@Autowired` constructor injection → `@Inject`
- Remove `javax.transaction.Transactional` imports (handled at service layer)
- Update model imports to use target package
- Preserve all JDBC template logic and SQL queries
- Maintain DataSource injection and transaction handling
- Target contract: @ApplicationScoped CDI beans with constructor injection, thread-safe by design

|**Target**: → `src/main/java/com/demo/repository/jdbc/` (JdbcOwnerRepositoryImpl, JdbcPetRepositoryImpl, JdbcPetTypeRepositoryImpl, JdbcSpecialtyRepositoryImpl, JdbcUserRepositoryImpl, JdbcVetRepositoryImpl, JdbcVisitRepositoryImpl)
|**Owns**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcOwnerRepositoryImpl.java`, `/projects/legacy/src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetRepositoryImpl.java`, '/projects/legacy/src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcPetTypeRepositoryImpl.java', '/projects/legacy/src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcSpecialtyRepositoryImpl.java', '/projects/legacy/src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcUserRepositoryImpl.java', '/projects/legacy/src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcVetRepositoryImpl.java', '/projects/legacy/src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcVisitRepositoryImpl.java'

#### T-010: Convert JPA repository implementations to CDI

|**Class**: rewrite
|
|**Findings**: springboot-di-to-quarkus-00003, springboot-jpa-to-quarkus-00000
|
|**Goal**: Convert 8 JPA repository implementations from Spring @Repository to Quarkus CDI
|
|**Target design**: → `src/main/java/com/demo/repository/jpa/*.java`
|
|**Task Details**:
- Copy JPA implementations from `migration/staging/src/main/java/org/springframework/samples/petclinic/repository/jpa/`
- Update package: `org.springframework.samples.petclinic.repository.jpa` → `com.demo.repository.jpa`
- Remove `@Repository` and `@Profile` annotations
- Replace `@PersistenceContext` → `@Inject EntityManager`
- Replace `@Transactional` → Hibernate `@Transactional` for remove operations
- Update model imports to use target package
- Preserve all EntityManager operations and JPQL queries
- Maintain transaction boundaries and exception handling
- Target contract: @ApplicationScoped CDI beans with @Inject EntityManager, thread-safe by design

|**Target**: → `src/main/java/com/demo/repository/jpa/` (JpaOwnerRepositoryImpl, JpaPetRepositoryImpl, JpaPetTypeRepositoryImpl, JpaSpecialtyRepositoryImpl, JpaUserRepositoryImpl, JpaVetRepositoryImpl, JpaVisitRepositoryImpl)
|**Owns**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaOwnerRepositoryImpl.java`, `/projects/legacy/src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaPetRepositoryImpl.java`, '/projects/legacy/src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaPetTypeRepositoryImpl.java', '/projects/legacy/src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaSpecialtyRepositoryImpl.java', '/projects/legacy/src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaUserRepositoryImpl.java', '/projects/legacy/src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaVetRepositoryImpl.java', '/projects/legacy/src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaVisitRepositoryImpl.java'

#### T-011: Convert Spring Data JPA repositories to Quarkus approach

|**Class**: rewrite
|
|**Findings**: springboot-jpa-to-quarkus-00000, transaction-to-quarkus-00003
|
|**Goal**: Convert Spring Data JPA repositories to Quarkus-compatible approach
|
|**Target design**: → `src/main/java/com/demo/repository/springdatajpa/*.java`
|
|**Task Details**:
- Copy Spring Data JPA implementations from `migration/staging/src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/`
- Update package: `org.springframework.samples.petclinic.repository.springdatajpa` → `com.demo.repository.springdatajpa`
- Remove `@Repository` and `@Profile` annotations
- Replace Spring Data JPA pattern with manual JPA repositories using EntityManager
- Preserve repository method contracts and query logic
- Update model imports to target package
- Remove Spring Data dependencies and replace with standard JPA
- Ensure transaction management aligns with Quarkus/Hibernate patterns
- Target contract: @ApplicationScoped CDI beans with @Inject EntityManager

|**Target**: → `src/main/java/com/demo/repository/springdatajpa/*.java`
|**Owns**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataPetRepositoryImpl.java`, '/projects/legacy/src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataPetTypeRepositoryImpl.java', '/projects/legacy/src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataSpecialtyRepositoryImpl.java', '/projects/legacy/src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/SpringDataVisitRepositoryImpl.java'

---

## REST Controller Layer Modernization (REDESIGN)

#### T-012: Convert OwnerRestController to JAX-RS

|**Class**: rewrite
|
|**Findings**: springboot-web-to-quarkus-00000, springboot-webmvc-to-quarkus-00000, springboot-security-to-quarkus-00000
|
|**Goal**: Convert Spring @RestController to JAX-RS @Path with proper HTTP semantics
|
|**Target design**: → `src/main/java/com/demo/rest/OwnerRestController.java`
|
|**Task Details**:
- Copy from `migration/staging/src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java`
- Update package: `org.springframework.samples.petclinic.rest` → `com.demo.rest`
- Convert annotations:
  - `@RestController` → `@ApplicationScoped` + `@Path("/api/owners")`
  - `@RequestMapping` → `@GET`, `@POST`, `@PUT`, `@DELETE`
  - `@PathVariable` → `@PathParam`
  - `@RequestBody` → `@Consumes("application/json")`
  - `@RequestParam` → `@QueryParam`
- Replace `ResponseEntity` → JAX-RS `Response` and `ResponseBuilder`
- Replace `UriComponentsBuilder` → JAX-RS `UriBuilder`
- Replace `BindingResult` → Bean Validation `@Valid` and `ExceptionMapper`
- Remove `@PreAuthorize` annotations (security simplified for demo)
- Update imports to use JAX-RS and target package
- Preserve API contract: GET returns 404 on missing, POST returns 201 with Location header
- Maintain JSON serialization through MapStruct mappers
- Target contract: @ApplicationScoped @Path("/api/owners") with constructor injection, stateless singleton

|**Target**: → `src/main/java/com/demo/rest/OwnerRestController.java`
|**Owns**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java`
|**API Contract**: GET /api/owners, GET /api/owners/{id}, POST /api/owners, PUT /api/owners/{id}, DELETE /api/owners/{id}

#### T-013: Convert remaining REST controllers to JAX-RS

|**Class**: rewrite
|
|**Findings**: springboot-web-to-quarkus-00000, springboot-webmvc-to-quarkus-00000
|
|**Goal**: Convert remaining 7 REST controllers from Spring MVC to JAX-RS patterns
|
|**Target design**: → `src/main/java/com/demo/rest/*.java`
|
|**Task Details**:
- Convert PetRestController → `@Path("/api/pets")`
- Convert VetRestController → `@Path("/api/vets")`
- Convert VisitRestController → `@Path("/api/visits")`
- Convert PetTypeRestController → `@Path("/api/petTypes")`
- Convert SpecialtyRestController → `@Path("/api/specialties")`
- Convert UserRestController → `@Path("/api/users")`
- Remove RootRestController (Quarkus provides `/q` endpoints)
- For each controller:
  - Update package to `com.demo.rest`
  - Convert Spring MVC annotations to JAX-RS equivalents
  - Replace ResponseEntity with Response
  - Handle validation with @Valid and ExceptionMapper
  - Update imports to target package and JAX-RS
  - Preserve business logic and API contracts
  - Remove @PreAuthorize annotations
  - Target contract: @ApplicationScoped @Path with constructor injection, stateless singleton

|**Target**: → `src/main/java/com/demo/rest/` (PetRestController, VetRestController, VisitRestController, PetTypeRestController, SpecialtyRestController, UserRestController; RootRestController removed)
|**Owns**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/rest/PetRestController.java`, '/projects/legacy/src/main/java/org/springframework/samples/petclinic/rest/VetRestController.java', '/projects/legacy/src/main/java/org/springframework/samples/petclinic/rest/VisitRestController.java', '/projects/legacy/src/main/java/org/springframework/samples/petclinic/rest/PetTypeRestController.java', '/projects/legacy/src/main/java/org/springframework/samples/petclinic/rest/SpecialtyRestController.java', '/projects/legacy/src/main/java/org/springframework/samples/petclinic/rest/UserRestController.java', '/projects/legacy/src/main/java/org/springframework/samples/petclinic/rest/RootRestController.java'

---

## Configuration and Utility Modernization

#### T-014: Remove and refactor Spring-specific utilities and configurations

|**Class**: rewrite
|
|**Findings**: springboot-di-to-quarkus-00003, springboot-metrics-to-quarkus-0200, springboot-jmx-to-quarkus-00001, springboot-webmvc-to-quarkus-00000
|
|**Goal**: Remove Spring-specific utilities and configurations replaced by Quarkus
|
|**Target design**: Remove or refactor Spring-specific utility classes
|
|**Task Details**:
- Remove `ApplicationSwaggerConfig.java` (Quarkus provides built-in OpenAPI/Swagger)
- Remove `CallMonitoringAspect.java` (replaced by Micrometer/Observability)
- Remove `BasicAuthenticationConfig.java` (replaced by Quarkus security or removal)
- Remove `DisableSecurityConfig.java` (redundant with security approach decision)
- Update `Roles.java` to a thread-safe `@ApplicationScoped` constants bean (stateless singleton; OWNER_ADMIN retained) for Quarkus security
- Remove EntityUtils.java usage and replace with Stream API
- Update any imports and dependencies accordingly
- Ensure no compilation errors from removals
- Target contract: Security utility classes refactored for Quarkus or removed entirely

|**Absorbs**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/util/ApplicationSwaggerConfig.java`, `/projects/legacy/src/main/java/org/springframework/samples/petclinic/util/CallMonitoringAspect.java`, `/projects/legacy/src/main/java/org/springframework/samples/petclinic/security/BasicAuthenticationConfig.java`, `/projects/legacy/src/main/java/org/springframework/samples/petclinic/security/DisableSecurityConfig.java`, '/projects/legacy/src/main/java/org/springframework/samples/petclinic/security/Roles.java'
|**Note**: Complete class removals or refactoring - target classes either deleted or significantly refactored

#### T-015: Convert database configuration to Quarkus format

|**Class**: rewrite
|
|**Findings**: localhost-jdbc-00002
|
|**Goal**: Update database connection configuration for Quarkus datasource
|
|**Target design**: `src/main/resources/application.properties`
|
|**Task Details**:
- Migrate Spring Boot datasource properties to Quarkus format:
  - `spring.datasource.url` → `quarkus.datasource.jdbc.url`
  - `spring.datasource.username` → `quarkus.datasource.username`
  - `spring.datasource.password` → `quarkus.datasource.password`
- Configure Quarkus Hibernate ORM:
  - `quarkus.hibernate-orm.database.generation=update`
  - `quarkus.hibernate-orm.sql-load-script=NoSuchFile` (disable automatic loading)
- Update JDBC driver dependencies to Quarkus extensions
- Configure connection pool settings for Quarkus ( Agrodss replaced by Quarkus built-in)
- Ensure externalized configuration preserved (jdbc.url, jdbc.username, jdbc.password)

|**Target**: → `src/main/resources/application.properties`
|**Preserve**: Database connection externalization for deployment flexibility
|**Preserve**: `server.servlet.context-path` (map to Quarkus HTTP root-path; keep API under preserved context path)
|**Preserve**: `petclinic.security.enable` property remains externalized

---

## Test Modernization and Validation

#### T-016: Create entity characterization tests

|**Class**: infer
|
|**Goal**: Create comprehensive tests for HARVEST entities to validate Jakarta migration
|
|**Target design**: → `src/test/java/com/demo/model/*Test.java`
|
|**Task Details**:
- Create unit tests for all 11 entities (Owner, Pet, Visit, PetType, Specialty, Vet, Role, User, BaseEntity, NamedEntity, Person)
- Test JPA annotations and relationships preserved
- Test validation constraints (@NotEmpty, @Digits) functional
- Test entity lifecycle methods (isNew(), getId(), setId())
- Test bidirectional relationships (Owner hasMany Pets, Pet belongsTo Owner, etc.)
- Test business methods (getPets(), addPet(), getPet(name))
- Verify Jakarta imports (jakarta.persistence.*, jakarta.validation.*) used correctly
- Use JUnit 5, assertJ, and embedded database for testing

|**Target**: → `src/test/java/com/demo/model/` (OwnerTest, PetTest, VisitTest, etc.)
|**Behavioral Pins**: Entity relationships and validation behavior preserved from legacy

#### T-017: Create service layer characterization tests

|**Class**: infer
|
|**Goal**: Create tests to validate service layer business logic preserved
|
|**Target design**: → `src/test/java/com/demo/service/*Test.java`
|
|**Task Details**:
- Create ClinicServiceImpl tests:
  - Test `findOwnerByLastName("Davis")` returns exactly 2 owners (legacy contract)
  - Test `findOwnerById(1)` returns owner with last name "Franklin" and 1 pet of type "cat"
  - Test `saveOwner()` assigns generated ID and makes entity findable
  - Test `updateOwner()` modifies without changing ID
  - Test `findVets()` no caching (simplified approach)
- Create UserServiceImpl tests:
  - Test user CRUD operations preserved
  - Test transaction behavior maintained
- Test CDI injection working correctly with @Inject
- Test exception handling (DataAccessException) preserved
- Test with mocked repositories using Mockito

|**Target**: → `src/test/java/com/demo/service/ClinicServiceTest.java`, `UserServiceTest.java`
|**Behavioral Pins**: Service contracts from AbstractClinicServiceTests.java maintained

#### T-018: Create REST API integration tests

|**Class**: infer
|
|**Goal**: Create JAX-RS endpoint tests to validate API contracts
|
|**Target design**: → `src/test/java/com/demo/rest/*Test.java`
|
|**Task Details**:
- Create OwnerRestController integration tests:
  - Test GET `/api/owners` returns 200 with collection or 404 if empty
  - Test GET `/api/owners/{id}` returns 200 with owner or 404 if not found
  - Test POST `/api/owners` returns 201 with Location header or 400 with validation errors
  - Test PUT `/api/owners/{id}` returns 204 or 400 on validation failure
  - Test DELETE `/api/owners/{id}` returns 204 or 404 if not found
- Test JSON serialization/deserialization through MapStruct mappers
- Test validation error handling with ExceptionMapper
- Test with QuarkusTest and RestAssured
- Test open access (no role-based restrictions per security decision)
- Test all other REST controllers with similar contracts

|**Target**: → `src/test/java/com/demo/rest/OwnerRestControllerTest.java`, etc.
|**API Contract**: HTTP status codes and JSON responses match legacy contracts

#### T-019: Create circular group integration tests

|**Class**: infer
|
|**Goal**: Test end-to-end workflow through the entire circular dependency group
|
|**Target design**: → `src/test/java/com/demo/CircularGroupIntegrationTest.java`
|
|**Task Details**:
- Test complete CRUD workflow: Create Owner → Add Pet → Create Visit
- Test data flows through entities → repositories → services → REST controllers
- Test bidirectional relationships work correctly (Owner-Pet-Visit)
- Test MapStruct DTO mappings in real-world scenarios
- Test JAX-RS endpoints with actual database operations
- Test concurrent access patterns (owner creation attempts)
- Test error scenarios: invalid pet types, missing entities, validation failures
- Verify all 53 classes in circular group work together correctly
- Test with real database (HSQLDB embedded or PostgreSQL test container)

|**Target**: → `src/test/java/com/demo/CircularGroupIntegrationTest.java`
|**Note**: Integration test validates that circular group modernization maintains system integrity

---

## Story Completion Criteria

|**Deployment Status**: `deploy=false`

Per **O-M3ACCEPT** guidance, when `deploy=false`, do not task the full literal `acceptance.path` with Java @Path/endpoint substance. The acceptance.path `/petclinic/api/vets` is deferred to the deploy story (S-AC1/G-OK).

|**Summary of Changes**:
- ✓ Jakarta namespace conversion (javax→jakarta) for 31 model/mapper classes
- ✓ CDI conversion for 2 services and 19 repository implementations  
- ✓ JAX-RS conversion for 8 REST controllers
- ✓ Spring Boot removal and Quarkus dependency setup
- ✓ Database configuration migration
- ✓ Security simplification (removal for demo purposes)
- ✓ Monitoring migration (CallMonitoringAspect → Micrometer/Observability)
- ✓ Comprehensive test coverage for all modernized components

|**Verification**:
- Build passes: `mvn -q clean test`
- All 53 circular group classes compile with Jakarta imports
- REST API endpoints functional with proper HTTP status codes
- Entity relationships maintained across the domain model
- Service layer assertions pass (legacy contract validation)
- CDI beans properly injected via constructor injection
- JPA repositories functional with EntityManager (where applicable)
- Application serves REST API from /api/ endpoints

|**UI Surface Coverage**: 
- **REST API Endpoints**: All `/api/*` endpoints modernized to JAX-RS with proper status codes
- **JSON Contracts**: MapStruct DTO mappings preserved with Jakarta validation
- **HTTP Semantics**: GET returns 404 on missing, POST returns 201/Location, proper error handling
- **Legacy API compatibility**: Base path `/api/*` preserved per `migration.yaml` preserve requirements

|**Preserved Integration Coverage**:
- **Database externalization**: jdbc.url, jdbc.username, jdbc.password properties maintained for deployment flexibility
- **REST API stability**: `/api/*` endpoint paths preserved for backward compatibility
- **petclinic.security.enable**: Simplified approach - security removed for demo purposes (documented in security-decision.md)