# Architecture Profile (M1 spec input bundle)

## 1. Purpose & domain

The PetClinic application is a **veterinary clinic management system** that provides comprehensive CRUD operations for managing a veterinary practice. It serves clinic administrators who need to track pet owners, their pets, veterinarians, and medical visits. The core domain revolves around three main entities:

**Owner Management**: Pet owners with personal details (name, address, city, telephone) who can own multiple pets. Each owner has a one-to-many relationship with pets, and owners can search by last name (src/test/java/org/springframework/samples/petclinic/rest/OwnerRestControllerTests.java:81-88).

**Pet Management**: Individual pets belonging to owners, categorized by pet types (dog, cat, etc.), with birth dates and visit histories. Pets have many-to-one relationships with both owners and pet types, and one-to-many with visits (src/main/java/org/springframework/samples/petclinic/model/Pet.java:36-50).

**Clinical Operations**: Veterinarians with specialties managing pet visits (examinations, treatments). Vets have many-to-many relationships with specialties, and visits link pets to their medical histories (src/main/java/org/springframework/samples/petclinic/model/Visit.java:36-50).

The system exposes a **REST API under `/api/`** for all operations, returning JSON payloads via DTOs mapped through MapStruct mappers (src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:41-84). Behavioral validation includes proper HTTP status codes (NOT_FOUND for missing entities, CREATED for successful POST, NO_CONTENT for successful PUT/DELETE), binding error handling with JSON error responses, and Spring Security role-based access control via `@PreAuthorize("hasRole(@roles.OWNER_ADMIN)")` (src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:52).

## 2. Components & relationships

```
┌─────────────────┐
│   REST Layer    │ 8 controllers (Owner/Pet/Vet/VisitRestController, etc.)
│  (@RestController)│
└────────┬────────┘
         │ calls via
         ▼
┌─────────────────┐
│  Service Layer  │ ClinicService facade + UserService
│   (@Service)    │
└────────┬────────┘
         │ delegates to
         ▼
┌─────────────────┐
│ Repository Layer│ OwnerRepository, PetRepository, etc.
│   (@Repository) │ (3 impl strategies: JDBC, JPA, SpringData)
└────────┬────────┘
         │ persists
         ▼
┌─────────────────┐
│   Model Layer   │ JPA entities (Owner, Pet, Visit, etc.)
│   (@Entity)     │ + DTOs + Mappers (MapStruct)
└─────────────────┘
```

**Architecture evidence**: The layered architecture follows Spring's standard pattern with REST controllers (@RestController at src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:39) calling services (@Service at src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java:47) which delegate to repositories (@Repository implementations). The MapStruct mappers bridge model entities to DTOs (src/main/java/org/springframework/samples/petclinic/mapper/OwnerMapper.java), creating a clean separation between internal model and external API representation.

**God nodes** (migration/dependency-order.md:8-14): The most depended-upon classes form the domain core:
- `Visit` (18 fan-in) - central medical record linking pets to their history
- `PetType` (18 fan-in) - categorical classification referenced everywhere
- `Pet` (17 fan-in) - primary domain object owned by owners
- `Specialty` (13 fan-in) - veterinary expertise classification
- `Owner` (11 fan-in) - person aggregate root with pet relationships

**Circular dependency group** (migration/dependency-order.md:42-104): A tightly coupled cluster of 53 classes containing all model entities, their repositories, and REST controllers. This group must convert together because mappers (OwnerMapper, PetMapper, etc.) create bi-directional dependencies between entities and DTOs, and repository interfaces reference entity types while services aggregate repository calls.

**Conversion order risk** (migration/dependency-order.md:18-41): BaseEntity, BindingErrorsResponse, and utility classes must convert first to establish the foundation, followed by configuration classes, then the circular group as a unit.

## 3. Integration surfaces

**External REST API**: 
- Base path: `/api/`
- Entity endpoints: `/api/owners`, `/api/pets`, `/api/vets`, `/api/visits`, `/api/petTypes`, `/api/specialties`
- Operations: GET (list, get by ID), POST (create), PUT (update), DELETE (remove)
- Security: `@PreAuthorize("hasRole(@roles.OWNER_ADMIN)")` requires OWNER_ADMIN role (src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:52)
- Error handling: `BindingErrorsResponse` JSON errors (src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:92-94)
- **preserve candidate**: All REST endpoints must remain at `/api/*` paths (migration/findings-inventory.md:339-343)

**Database persistence**: 
- JPA/Hibernate with multiple backend support (HSQLDB, MySQL, PostgreSQL)
- Configuration via `application-{hsqldb,mysql,postgresql}.properties` (src/main/resources/)
- Connection properties: `jdbc.url`, `jdbc.username`, `jdbc.password` (migration/findings-inventory.md:135-137)
- **preserve candidate**: Database connection configuration must remain externalized

**Security**:
- Spring Security with role-based access control
- `BasicAuthenticationConfig` defines security setup (src/main/java/org/springframework/samples/petclinic/security/BasicAuthenticationConfig.java:17,22,40)
- `Roles` class defines `OWNER_ADMIN` constant (src/main/java/org/springframework/samples/petclinic/security/Roles.java:5,8)
- **OPEN DESIGN** (migration/findings-inventory.md:181-185): Security migration strategy not yet decided

**Configuration**:
- Spring Boot property files with environment-specific profiles
- Logging configuration via `logging.level.*` properties (src/main/resources/application.properties:33,34)
- **preserve candidate**: Application properties must remain in Quarkus `application.properties` format

## 4. Behavioral contract sources

**Service layer contract** (src/test/java/org/springframework/samples/petclinic/service/clinicService/AbstractClinicServiceTests.java):
- `findOwnerByLastName("Davis")` returns exactly 2 owners (lines 57-59)
- `findOwnerById(1)` returns owner with last name starting "Franklin" and 1 pet with type "cat" (lines 66-71)
- `saveOwner()` assigns generated ID and makes entity findable by last name (lines 75-90)
- `updateOwner()` modifies existing owner without changing ID (lines 94-100)

**REST layer contract** (src/test/java/org/springframework/samples/petclinic/rest/OwnerRestControllerTests.java):
- GET `/api/owners` returns 200 with owner list or 404 if empty (lines 118-122)
- GET `/api/owners/{id}` returns 200 with owner DTO or 404 if not found (lines 129-136)
- POST `/api/owners` returns 201 with Location header on success, 400 with JSON errors on validation failure (lines 143-155)
- PUT `/api/owners/{id}` returns 204 on success, 400 if body ID doesn't match path ID or validation fails (lines 162-174)
- DELETE `/api/owners/{id}` returns 204 on success, 404 if not found (lines 181-190)

**Contract gaps**: 
- No test coverage for concurrent owner creation attempts
- No negative test cases for invalid pet types or specialty associations
- No validation of pet name uniqueness within owner scope
- Missing boundary tests for pagination, sorting, or filtering beyond last name search

## 5. Modernization surface

**By component**:

**REST Controllers** (8 classes):
- MUST: Convert `@RestController`, `@RequestMapping`, `@GetMapping/@PostMapping/@PutMapping/@DeleteMapping` to JAX-RS annotations (migration/findings-inventory.md:307-311)
- MUST: Replace Spring Security `@PreAuthorize` with Quarkus security or remove authentication (migration/findings-inventory.md:181-185, OPEN DESIGN)
- MUST: Convert `ResponseEntity`, `HttpHeaders`, `UriComponentsBuilder` to JAX-RS equivalents (src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:19-29,44-49)
- MUST: Replace Spring validation `BindingResult` with Bean Validation `@Valid` and ExceptionMapper (src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:87-95)
- SHOULD: Maintain `/api/` base path for backward compatibility

**Services** (4 classes):
- MUST: Convert `@Service`, `@Autowired` constructor injection to `@ApplicationScoped` with CDI `@Inject` (migration/findings-inventory.md:64-96)
- MUST: Replace `@Transactional` with Hibernate `@Transactional` or programmatic transaction management (migration/findings-inventory.md:166-173, OPEN DESIGN)
- MUST: Replace `@Cacheable("vets")` with Quarkus caching or remove if not critical (src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java:48,86)
- SHOULD: Maintain facade pattern separating web layer from persistence

**Repositories** (21 classes across 3 strategies):
- MUST: Convert `@Repository` to CDI beans or interface-based discovery
- MUST: Replace JPA `@PersistenceContext` with `@Inject` EntityManager (migration/findings-inventory.md:324)
- MUST: Replace Spring Data JPA repositories with Quarkus Panache or manual JPA (migration/findings-inventory.md:271-275, OPEN DESIGN)
- MUST: Ensure transaction management aligns with Quarkus/Hibernate patterns

**Models** (15 entities + 7 DTOs + 7 mappers):
- MUST: Convert `javax.persistence.*` to `jakarta.persistence.*` imports (migration/findings-inventory.md:5-62)
- MUST: Convert `javax.validation.*` to `jakarta.validation.*` (migration/findings-inventory.md:5-62)
- SHOULD: Maintain JPA entity relationships and validation constraints
- HARVEST: DTOs and value objects can carry over with jakarta namespace updates

**Configuration** (6 classes):
- MUST: Remove `@SpringBootApplication` bootstrap and main class (migration/findings-inventory.md:245)
- MUST: Convert property files from Spring Boot to Quarkus format (migration/findings-inventory.md:131-165)
- MUST: Replace Spring Security configuration with Quarkus security (src/main/java/org/springframework/samples/petclinic/security/BasicAuthenticationConfig.java:17,22,40)
- MUST: Remove Spring JMX aspects (CallMonitoringAspect) or replace with Micrometer (migration/findings-inventory.md:145-149)

## 6. Domain boundaries

**Single bounded context**: This application represents a **cohesive veterinary clinic domain** without clear subdomain separation. All components (Owner, Pet, Vet, Visit, Specialty) operate within the same problem space - managing clinic operations. The tight coupling in the circular dependency group (53 classes) indicates a monolith rather than separately deployable bounded contexts (migration/dependency-order.md:42-104).

**Coupling rationale**:
- **Entity coupling**: Pet references Owner and PetType; Visit references Pet and (indirectly) Vet; all entities extend BaseEntity (src/main/java/org/springframework/samples/petclinic/model/Owner.java:38,52, src/main/java/org/springframework/samples/petclinic/model/Pet.java:36-50)
- **Repository coupling**: Each repository interface pairs with exactly one entity and all repositories are injected into the same ClinicService facade (src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java:58-72)
- **Mapper coupling**: MapStruct generates bidirectional mapping between entities and DTOs creating compilation dependencies (target/generated-sources/annotations/.../OwnerMapperImpl.java:17, @Component annotation)

**Candidate seams** (for potential future extraction):
- **Owner-Pet aggregate** (Owner, Pet, PetType, Visit entities + OwnerRestController + OwnerRepository + OwnerService)
- **Veterinary operations** (Vet, Specialty, Visit entities + VetRestController + VetRepository)
- **Reference data** (PetType, Specialty as lookup tables + PetTypeRestController + SpecialtyRestController)

However, these seams share the Visit entity and are too intertwined to split without significant refactoring. M2 should keep as single modernization story.

## 7. Class roles & target contract

### REDESIGN (runtime behavior owners - modernize, don't copy)

**Removed — CDI auto-discovery replaces bootstrap:**
- `org.springframework.samples.petclinic.PetClinicApplication` (legacy/src/main/java/.../PetClinicApplication.java:7) — **removed — Quarkus @ApplicationScoped classes auto-discovered**
  - Target: deleted - main class eliminated in Quarkus native bootstrap

**REST Controllers (converted to JAX-RS):**
- `org.springframework.samples.petclinic.rest.OwnerRestController` (legacy/src/main/java/.../rest/OwnerRestController.java:39)
  - **Concurrency**: stateless singleton - thread-safe by design
  - **Resource/cache policy**: no caching - delegates to ClinicService
  - **API contract**: GET returns **404** on missing; POST with `@Valid` rejects with **400** (problem-detail); downstream failures map to **503** via JAX-RS **ExceptionMapper**
  - **Target**: `@ApplicationScoped @Path("/api/owners")` with constructor injection

- `org.springframework.samples.petclinic.rest.PetRestController` (legacy/src/main/java/.../rest/PetRestController.java:32)
  - Same target contract as OwnerRestController for consistent API behavior

- `org.springframework.samples.petclinic.rest.VetRestController` (legacy/src/main/java/.../rest/VetRestController.java:33)
  - Same target contract as OwnerRestController

- `org.springframework.samples.petclinic.rest.VisitRestController` (legacy/src/main/java/.../rest/VisitRestController.java:32)
  - Same target contract as OwnerRestController

- `org.springframework.samples.petclinic.rest.PetTypeRestController` (legacy/src/main/java/.../rest/PetTypeRestController.java:31)
  - Same target contract as OwnerRestController

- `org.springframework.samples.petclinic.rest.SpecialtyRestController` (legacy/src/main/java/.../rest/SpecialtyRestController.java:31)
  - Same target contract as OwnerRestController

- `org.springframework.samples.petclinic.rest.UserRestController` (legacy/src/main/java/.../rest/UserRestController.java:30)
  - Same target contract as OwnerRestController

- `org.springframework.samples.petclinic.rest.RootRestController` (legacy/src/main/java/.../rest/RootRestController.java:38)
  - **removed — Quarkus automatically provides `/q` health/info endpoints**

**Services (converted to CDI):**
- `org.springframework.samples.petclinic.service.ClinicServiceImpl` (legacy/src/main/java/.../service/ClinicServiceImpl.java:47)
  - **Concurrency**: shared singleton with mutable collections - thread-safe via `ConcurrentHashMap` and `compute()` for cached data
  - **Resource/cache policy**: cached vet list requires bounded refresh - **no clear-on-miss** for `@Cacheable("vets")`
  - **Aggregate/derived math**: owner pets sorted by name - **normalize-before-sorting** to ensure stable ordering
  - **Target**: `@ApplicationScoped` with CDI constructor injection

- `org.springframework.samples.petclinic.service.UserServiceImpl` (legacy/src/main/java/.../service/UserServiceImpl.java:10)
  - **Concurrency**: stateless singleton - thread-safe by design
  - **Target**: `@ApplicationScoped` with CDI constructor injection

**Repositories (converted to CDI/JPA):**
- `org.springframework.samples.petclinic.repository.jdbc.JdbcOwnerRepositoryImpl` (legacy/src/main/java/.../repository/jdbc/JdbcOwnerRepositoryImpl.java:54)
  - **Concurrency**: stateless singleton - thread-safe by design
  - **Target**: `@ApplicationScoped` CDI bean with constructor injection

- `org.springframework.samples.petclinic.repository.jdbc.JdbcPetRepositoryImpl` (legacy/src/main/java/.../repository/jdbc/JdbcPetRepositoryImpl.java:54)
  - Same target as JdbcOwnerRepositoryImpl

- `org.springframework.samples.petclinic.repository.jdbc.JdbcPetTypeRepositoryImpl` (legacy/src/main/java/.../repository/jdbc/JdbcPetTypeRepositoryImpl.java:47)
  - Same target as JdbcOwnerRepositoryImpl

- `org.springframework.samples.petclinic.repository.jdbc.JdbcSpecialtyRepositoryImpl` (legacy/src/main/java/.../repository/jdbc/JdbcSpecialtyRepositoryImpl.java:43)
  - Same target as JdbcOwnerRepositoryImpl

- `org.springframework.samples.petclinic.repository.jdbc.JdbcUserRepositoryImpl` (legacy/src/main/java/.../repository/jdbc/JdbcUserRepositoryImpl.java:21)
  - Same target as JdbcOwnerRepositoryImpl

- `org.springframework.samples.petclinic.repository.jdbc.JdbcVetRepositoryImpl` (legacy/src/main/java/.../repository/jdbc/JdbcVetRepositoryImpl.java:56)
  - Same target as JdbcOwnerRepositoryImpl

- `org.springframework.samples.petclinic.repository.jdbc.JdbcVisitRepositoryImpl` (legacy/src/main/java/.../repository/jdbc/JdbcVisitRepositoryImpl.java:51)
  - Same target as JdbcOwnerRepositoryImpl

- `org.springframework.samples.petclinic.repository.jpa.JpaOwnerRepositoryImpl` (legacy/src/main/java/.../repository/jpa/JpaOwnerRepositoryImpl.java:40)
  - **Concurrency**: stateless singleton - thread-safe by design
  - **Transaction**: remove operations require `@Transactional` annotation
  - **Target**: `@ApplicationScoped` CDI bean with `@Inject EntityManager`

- `org.springframework.samples.petclinic.repository.jpa.JpaPetRepositoryImpl` (legacy/src/main/java/.../repository/jpa/JpaPetRepositoryImpl.java:40,80)
  - **Transaction**: remove requires `@Transactional`; findPetTypes uses query
  - Same target as JpaOwnerRepositoryImpl

- `org.springframework.samples.petclinic.repository.jpa.JpaPetTypeRepositoryImpl` (legacy/src/main/java/.../repository/jpa/JJpaPetTypeRepositoryImpl.java:39)
  - Same target as JpaOwnerRepositoryImpl

- `org.springframework.samples.petclinic.repository.jpa.JpaSpecialtyRepositoryImpl` (legacy/src/main/java/.../repository/jpa/JpaSpecialtyRepositoryImpl.java:35)
  - Same target as JpaOwnerRepositoryImpl

- `org.springframework.samples.petclinic.repository.jpa.JpaUserRepositoryImpl` (legacy/src/main/java/.../repository/jpa/JpaUserRepositoryImpl.java:12)
  - Same target as JpaOwnerRepositoryImpl

- `org.springframework.samples.petclinic.repository.jpa.JpaVetRepositoryImpl` (legacy/src/main/java/.../repository/jpa/JpaVetRepositoryImpl.java:37)
  - Same target as JpaOwnerRepositoryImpl

- `org.springframework.samples.petclinic.repository.jpa.JpaVisitRepositoryImpl` (legacy/src/main/java/.../repository/jpa/JpaVisitRepositoryImpl.java:42)
  - **Transaction**: remove requires `@Transactional`
  - Same target as JpaOwnerRepositoryImpl

- `org.springframework.samples.petclinic.repository.springdatajpa.SpringDataPetRepositoryImpl` (legacy/src/main/java/.../repository/springdatajpa/SpringDataPetRepositoryImpl.java:19,42)
  - **OPEN DESIGN** (findings-inventory.md:271-275): decide between Quarkus Panache or manual JPA
  - **Transaction**: remove requires `@Transactional`

- `org.springframework.samples.petclinic.repository.springdatajpa.SpringDataPetTypeRepositoryImpl` (legacy/src/main/java/.../repository/springdatajpa/SpringDataPetTypeRepositoryImpl.java:24)
  - **OPEN DESIGN**: same as SpringDataPetRepositoryImpl

- `org.springframework.samples.petclinic.repository.springdatajpa.SpringDataSpecialtyRepositoryImpl` (legacy/src/main/java/.../repository/springdatajpa/SpringDataSpecialtyRepositoryImpl.java:19)
  - **OPEN DESIGN**: same as SpringDataPetRepositoryImpl

- `org.springframework.samples.petclinic.repository.springdatajpa.SpringDataVisitRepositoryImpl` (legacy/src/main/java/.../repository/springdatajpa/SpringDataVisitRepositoryImpl.java:19,42)
  - **OPEN DESIGN**: same as SpringDataPetRepositoryImpl
  - **Transaction**: remove requires `@Transactional`

**Security Configuration:**
- `org.springframework.samples.petclinic.security.BasicAuthenticationConfig` (legacy/src/main/java/.../security/BasicAuthenticationConfig.java:17,22,40)
  - **removed — Quarkus security configuration replaces Spring Security**

- `org.springframework.samples.petclinic.security.DisableSecurityConfig` (legacy/src/main/java/.../security/DisableSecurityConfig.java:12)
  - **removed — Quarkus security configuration replaces Spring Security**

- `org.springframework.samples.petclinic.util.ApplicationSwaggerConfig` (legacy/src/main/java/.../util/ApplicationSwaggerConfig.java:50,55,83,85)
  - **removed — Quarkus provides built-in OpenAPI/Swagger**

**Utilities:**
- `org.springframework.samples.petclinic.util.CallMonitoringAspect` (src/main/java/org/springframework/samples/petclinic/util/CallMonitoringAspect.java:37,47,52,57,63,68)
  - **removed — replace with Micrometer-based observability** (migration/findings-inventory.md:145-149, OPEN DESIGN)

- `org.springframework.samples.petclinic.util.EntityUtils` (src/main/java/org/springframework/samples/petclinic/util/EntityUtils.java)
  - **removed — replaced by Java Stream API or Apache Commons Collections**

**Configuration/Constants**:
- `org.springframework.samples.petclinic.security.Roles` (src/main/java/org/springframework/samples/petclinic/security/Roles.java:5,8)
  - **Concurrency**: stateless singleton - thread-safe by design
  - **Target**: `@ApplicationScoped` bean with constant definitions for Quarkus security

**Generated Mapper Implementations** (7 classes - @Component annotated):
- `org.springframework.samples.petclinic.mapper.OwnerMapperImpl` (target/generated-sources/annotations/.../OwnerMapperImpl.java:17)
  - **Concurrency**: stateless singleton - thread-safe by design
  - **Target**: `@ApplicationScoped` CDI bean with regenerated Jakarta imports

- `org.springframework.samples.petclinic.mapper.PetMapperImpl` (target/generated-sources/annotations/.../PetMapperImpl.java:17)
  - **Concurrency**: stateless singleton - thread-safe by design
  - **Target**: `@ApplicationScoped` CDI bean with regenerated Jakarta imports

- `org.springframework.samples.petclinic.mapper.PetTypeMapperImpl` (target/generated-sources/annotations/.../PetTypeMapperImpl.java:17)
  - **Concurrency**: stateless singleton - thread-safe by design
  - **Target**: `@ApplicationScoped` CDI bean with regenerated Jakarta imports

- `org.springframework.samples.petclinic.mapper.SpecialtyMapperImpl` (target/generated-sources/annotations/.../SpecialtyMapperImpl.java:17)
  - **Concurrency**: stateless singleton - thread-safe by design
  - **Target**: `@ApplicationScoped` CDI bean with regenerated Jakarta imports

- `org.springframework.samples.petclinic.mapper.UserMapperImpl` (target/generated-sources/annotations/.../UserMapperImpl.java:17)
  - **Concurrency**: stateless singleton - thread-safe by design
  - **Target**: `@ApplicationScoped` CDI bean with regenerated Jakarta imports

- `org.springframework.samples.petclinic.mapper.VetMapperImpl` (target/generated-sources/annotations/.../VetMapperImpl.java:17)
  - **Concurrency**: stateless singleton - thread-safe by design
  - **Target**: `@ApplicationScoped` CDI bean with regenerated Jakarta imports

- `org.springframework.samples.petclinic.mapper.VisitMapperImpl` (target/generated-sources/annotations/.../VisitMapperImpl.java:17)
  - **Concurrency**: stateless singleton - thread-safe by design
  - **Target**: `@ApplicationScoped` CDI bean with regenerated Jakarta imports

### HARVEST (data/value objects - carry over faithfully)

**JPA Entities** (15 classes - all javax→jakarta namespace):
- `org.springframework.samples.petclinic.model.BaseEntity` (legacy/src/main/java/.../model/BaseEntity.java:18-21)
- `org.springframework.samples.petclinic.model.NamedEntity` (legacy/src/main/java/.../model/NamedEntity.java:18,19,21)
- `org.springframework.samples.petclinic.model.Person` (legacy/src/main/java/.../model/Person.java:18,19,21)
- `org.springframework.samples.petclinic.model.Owner` (legacy/src/main/java/.../model/Owner.java:22,23,24)
- `org.springframework.samples.petclinic.model.Pet` (legacy/src/main/java/.../model/Pet.java:22)
- `org.springframework.samples.petclinic.model.PetType` (legacy/src/main/java/.../model/PetType.java:18,19)
- `org.springframework.samples.petclinic.model.Role` (legacy/src/main/java/.../model/Role.java:3-8)
- `org.springframework.samples.petclinic.model.Specialty` (legacy/src/main/java/.../model/Specialty.java:18,19)
- `org.springframework.samples.petclinic.model.User` (legacy/src/main/java/.../model/User.java:6-12)
- `org.springframework.samples.petclinic.model.Vet` (legacy/src/main/java/.../model/Vet.java:22,23)
- `org.springframework.samples.petclinic.model.Visit` (legacy/src/main/java/.../model/Visit.java:20,21)

**Repository Interfaces** (11 classes - pure interfaces):
- `org.springframework.samples.petclinic.repository.OwnerRepository` (legacy/src/main/java/.../repository/OwnerRepository.java)
- `org.springframework.samples.petclinic.repository.PetRepository` (legacy/src/main/java/.../repository/PetRepository.java)
- `org.springframework.samples.petclinic.repository.PetTypeRepository` (legacy/src/main/java/.../repository/PetTypeRepository.java)
- `org.springframework.samples.petclinic.repository.SpecialtyRepository` (legacy/src/main/java/.../repository/SpecialtyRepository.java)
- `org.springframework.samples.petclinic.repository.UserRepository` (legacy/src/main/java/.../repository/UserRepository.java)
- `org.springframework.samples.petclinic.repository.VetRepository` (legacy/src/main/java/.../repository/VetRepository.java)
- `org.springframework.samples.petclinic.repository.VisitRepository` (legacy/src/main/java/.../repository/VisitRepository.java)
- Plus Spring Data JPA interfaces (SpringDataOwnerRepository, SpringDataPetRepository, etc.)

**DTOs** (13 classes - all javax→jakarta namespace):
- `org.springframework.samples.petclinic.dto.OwnerDto` (legacy/target/generated-sources/openapi/.../OwnerDto.java:13,14)
- `org.springframework.samples.petclinic.dto.OwnerAllOfDto` (legacy/target/generated-sources/openapi/.../OwnerAllOfDto.java:11,12)
- `org.springframework.samples.petclinic.dto.OwnerFieldsDto` (legacy/target/generated-sources/openapi/.../OwnerFieldsDto.java:8,9)
- `org.springframework.samples.petclinic.dto.PetDto` (legacy/target/generated-sources/openapi/.../PetDto.java:15,16)
- `org.springframework.samples.petclinic.dto.PetAllOfDto` (legacy/target/generated-sources/openapi/.../PetAllOfDto.java:12,13)
- `org.springframework.samples.petclinic.dto.PetFieldsDto` (legacy/target/generated-sources/openapi/.../PetFieldsDto.java:9,10)
- `org.springframework.samples.petclinic.dto.PetTypeDto` (legacy/target/generated-sources/openapi/.../PetTypeDto.java:8,9)
- `org.springframework.samples.petclinic.dto.VetDto` (legacy/target/generated-sources/openapi/.../VetDto.java:11,12)
- `org.springframework.samples.petclinic.dto.VisitDto` (legacy/target/generated-sources/openapi/.../VisitDto.java:11,12)
- `org.springframework.samples.petclinic.dto.VisitAllOfDto` (legacy/target/generated-sources/openapi/.../VisitAllOfDto.java:8,9)
- `org.springframework.samples.petclinic.dto.VisitFieldsDto` (legacy/target/generated-sources/openapi/.../VisitFieldsDto.java:9,10)
- `org.springframework.samples.petclinic.dto.RoleDto` (legacy/target/generated-sources/openapi/.../RoleDto.java:8,9)
- `org.springframework.samples.petclinic.dto.SpecialtyDto` (legacy/target/generated-sources/openapi/.../SpecialtyDto.java:8,9)
- `org.springframework.samples.petclinic.dto.UserDto` (legacy/target/generated-sources/openapi/.../UserDto.java:11,12)
- `org.springframework.samples.petclinic.dto.RestErrorDto` (legacy/target/generated-sources/openapi/.../RestErrorDto.java:13,14)
- `org.springframework.samples.petclinic.dto.ValidationMessageDto` (legacy/target/generated-sources/openapi/.../ValidationMessageDto.java:10,11)

**Mappers** (7 classes - all javax→jakarta namespace):
- `org.springframework.samples.petclinic.mapper.OwnerMapper` (src/main/java/org/springframework/samples/petclinic/mapper/OwnerMapper.java)
- `org.springframework.samples.petclinic.mapper.PetMapper` (src/main/java/org/springframework/samples/petclinic/mapper/PetMapper.java)
- `org.springframework.samples.petclinic.mapper.PetTypeMapper` (src/main/java/org/springframework/samples/petclinic/mapper/PetTypeMapper.java)
- `org.springframework.samples.petclinic.mapper.SpecialtyMapper` (src/main/java/org/springframework/samples/petclinic/mapper/SpecialtyMapper.java)
- `org.springframework.samples.petclinic.mapper.UserMapper` (src/main/java/org/springframework/samples/petclinic/mapper/UserMapper.java)
- `org.springframework.samples.petclinic.mapper.VetMapper` (src/main/java/org/springframework/samples/petclinic/mapper/VetMapper.java)
- `org.springframework.samples.petclinic.mapper.VisitMapper` (src/main/java/org/springframework/samples/petclinic/mapper/VisitMapper.java)

**Value Objects and Utilities:**
- `org.springframework.samples.petclinic.rest.BindingErrorsResponse` (src/main/java/org/springframework/samples/petclinic/rest/BindingErrorsResponse.java)
- All package-info.java files
