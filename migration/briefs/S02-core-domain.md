# S02: Core domain and circular group modernization

<!-- The brief is the self-contained work order for one modernization
     story. Bar: a competent developer or a fresh session starts the
     story from THIS FILE ALONE. Fill every section; delete none. -->

## Goal & position

What this story achieves and why it is next: its place in the roadmap,
what it unblocks, which stories it depends on (cite
dependency-order.md / architecture-profile.md).

This story modernizes the tightly coupled circular group of 53 classes containing all model entities, repositories, services, mappers, and REST controllers. These classes must convert together due to bidirectional dependencies between entities and DTOs, repository interfaces referencing entity types, and services aggregating repository calls. This is the core application functionality that delivers working CRUD operations via REST API. This story depends on S01's foundation classes (BaseEntity, BindingErrorsResponse) and delivers a fully functional PetClinic application with Jakarta namespaces, Quarkus CDI, and JAX-RS endpoints.

## In scope

The exact legacy classes/files this story modernizes. For each, quote
the load-bearing legacy code (the lines being transformed — imports,
annotations, key methods), so the story never starts from a blank
read:

### Model Entities (HARVEST with javax→jakarta updates)
- `src/main/java/org/springframework/samples/petclinic/model/NamedEntity.java`
- `src/main/java/org/springframework/samples/petclinic/model/Person.java`
- `src/main/java/org/springframework/samples/petclinic/model/Owner.java`
  ```java
  import javax.persistence.*;
  import javax.validation.constraints.Digits;
  import javax.validation.constraints.NotEmpty;

  @Entity
  @Table(name = "owners")
  public class Owner extends Person {
      @Column(name = "address")
      private String address;
  ```

- `src/main/java/org/springframework/samples/petclinic/model/Pet.java`
- `src/main/java/org/springframework/samples/petclinic/model/PetType.java`
- `src/main/java/org/springframework/samples/petclinic/model/Role.java`
- `src/main/java/org/springframework/samples/petclinic/model/Specialty.java`
- `src/main/java/org/springframework/samples/petclinic/model/User.java`
- `src/main/java/org/springframework/samples/petclinic/model/Vet.java`
- `src/main/java/org/springframework/samples/petclinic/model/Visit.java`

### Repository Interfaces
- `src/main/java/org/springframework/samples/petclinic/repository/OwnerRepository.java`
- `src/main/java/org/springframework/samples/petclinic/repository/PetRepository.java`
- All other repository interfaces

### Repository Implementations (REDESIGN - CDI conversion)
- `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcOwnerRepositoryImpl.java`
  ```java
  @Repository
  @Profile("jdbc")
  public class JdbcOwnerRepositoryImpl implements OwnerRepository {

      @Autowired
      public JdbcOwnerRepositoryImpl(DataSource dataSource) {
  ```

- `src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaOwnerRepositoryImpl.java`
  ```java
  import javax.persistence.EntityManager;
  import javax.persistence.PersistenceContext;

  @Repository
  @Profile("jpa")
  public class JpaOwnerRepositoryImpl implements OwnerRepository {

      @PersistenceContext
      private EntityManager entityManager;
  ```

- All other JDBC/JPA implementations including Spring Data JPA

### Mappers (HARVEST with jakarta updates)
- `src/main/java/org/springframework/samples/petclinic/mapper/OwnerMapper.java`
  ```java
  @Mapper(uses = PetMapper.class)
  public interface OwnerMapper {

      OwnerDto toOwnerDto(Owner owner);

      Owner toOwner(OwnerDto ownerDto);
  ```

- All other mappers (PetMapper, PetTypeMapper, etc.)

### Services (REDESIGN - CDI conversion)
- `src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java`
  ```java
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.cache.annotation.Cacheable;

  @Service
  @Transactional
  public class ClinicServiceImpl implements ClinicService {

      @Autowired
      private OwnerRepository ownerRepository;

      @Cacheable("vets")
      public Collection<Vet> findVets() throws DataAccessException {
  ```

- `src/main/java/org/springframework/samples/petclinic/service/UserServiceImpl.java`

### REST Controllers (REDESIGN - JAX-RS conversion)
- `src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java`
  ```java
  @RestController
  @CrossOrigin(exposedHeaders = "errors, content-type")
  @RequestMapping("/api/owners")
  public class OwnerRestController {

      @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
      @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
      public ResponseEntity<Collection<OwnerDto>> getOwners() {
  ```

- All other REST controllers (PetRestController, VetRestController, etc.)

## Out of scope

What neighboring code this story must NOT touch, and which story owns
it. (The tree must stay buildable: name any temporary seams — e.g. a
dependent class that keeps compiling against the old shape until its
own story.)

Configuration classes, main application class, property files, and POM are out of scope and owned by S03. The circular group must compile and function independently before platform configuration is applied in S03.

## Class roles & target contract (from architecture-profile §7)

For each in-scope class, its role and — for REDESIGN classes — the target
contract carried forward from profile §7, so M3 writes tasks and tests to
the target (not the legacy):

### Model Entities (HARVEST)
- **Owner, Pet, Visit, PetType, Specialty, Vet, Role, User** — HARVEST
  - Role: Domain entities with JPA relationships and validation
  - Target: Update javax.persistence.* imports to jakarta.persistence.*, maintain entity relationships, validation constraints, and business methods

### Repository Implementations (REDESIGN)
- **JdbcOwnerRepositoryImpl, JpaOwnerRepositoryImpl, etc.** — REDESIGN
  - Target: @ApplicationScoped CDI beans with constructor injection, replace @Autowired with @Inject, remove @Repository and @Profile annotations, use Quarkus transaction patterns
  - Concurrency: stateless singleton - thread-safe by design

- **SpringData*RepositoryImpl** — REDESIGN (OPEN DESIGN)
  - Target: Decide between Quarkus Panache or manual JPA approach
  - Concurrency: stateless singleton - thread-safe by design

### Mappers (HARVEST)
- **OwnerMapper, PetMapper, etc.** — HARVEST
  - Role: MapStruct-based conversion between entities and DTOs
  - Target: Update javax imports to jakarta, maintain MapStruct configuration, regenerate implementations

### Services (REDESIGN)
- **ClinicServiceImpl** — REDESIGN
  - Target: @ApplicationScoped with CDI constructor injection, replace @Autowired with @Inject, replace Spring @Cacheable with Quarkus caching or remove
  - Concurrency: shared singleton with mutable collections - thread-safe via ConcurrentHashMap
  - Cache policy: cached vet list requires bounded refresh - no clear-on-miss for @Cacheable("vets")
  - Aggregate/derived math: owner pets sorted by name - normalize-before-sorting

- **UserServiceImpl** — REDESIGN  
  - Target: @ApplicationScoped with CDI constructor injection
  - Concurrency: stateless singleton - thread-safe by design

### REST Controllers (REDESIGN)
- **OwnerRestController, PetRestController, VetRestController, etc.** — REDESIGN
  - Target: @ApplicationScoped @Path("/api/owners") with constructor injection
  - Concurrency: stateless singleton - thread-safe by design
  - Resource/cache policy: no caching - delegates to ClinicService
  - API contract: GET returns 404 on missing; POST with @Valid rejects with 400 (problem-detail); downstream failures map to 503 via JAX-RS ExceptionMapper
  - Validation: Replace Spring BindingResult with Bean Validation @Valid and ExceptionMapper

## Decided target shapes

The MAPPINGS.md rows that apply (quote the decided target, don't
re-decide). Recipe-executed rules already handled: reference
`migration/recipe-log.md` and `migration/staging/` where applicable.

**javax-to-jakarta-import-00001**: All entities, mappers, and generated code
**springboot-di-to-quarkus-00003**: Constructor injection via CDI @Inject (NOT spring-di extension)
**spring-components-00001/00002**: Version incompatibility resolved through conversion tasks
**OPEN DESIGN decisions**:
- Spring Data JPA → Quarkus Panache or manual JPA (decide during implementation)
- Spring Security → Quarkus security or remove (decide during implementation)  
- Transaction management → Quarkus/Hibernate patterns (decide during implementation)

**Story ordering:** extensions and BOM first, then models, then resources,
then config keys, then tests (`extensions → models → resources → config →
tests`).

## Contracts owned by this story

- **Findings**: the mandatory rule ids this story resolves (from the
  roadmap entry).
  - javax-to-jakarta-import-00001: All entities, mappers, generated code
  - springboot-di-to-quarkus-00003: All @Autowired → @Inject conversions
  - spring-components-00001/00002: Version compatibility fixes

- **Preserve**: the `preserve:` items whose surfaces live in scope —
  spell out the env var names/values mechanism to keep.
  - REST API base path "/api/*" maintained for backward compatibility
  - Database connection properties externalized (jdbc.url, jdbc.username, jdbc.password)

- **Behavioral pins**: the assertion values that must hold after this
  story (quote numbers/strings and their test source). Harvest classes
  and behavior-preserving redesign pin LEGACY values; behavior-changing
  redesign pins the §7 TARGET (e.g. 404, not create-on-GET). Name the
  contract GAPS this story closes with characterization tests.

  Service layer contracts (from AbstractClinicServiceTests.java):
  - findOwnerByLastName("Davis") returns exactly 2 owners
  - findOwnerById(1) returns owner with last name starting "Franklin" and 1 pet with type "cat"
  - saveOwner() assigns generated ID and makes entity findable by last name
  - updateOwner() modifies existing owner without changing ID

  REST layer contracts (from OwnerRestControllerTests.java):
  - GET `/api/owners` returns 200 with owner list or 404 if empty
  - GET `/api/owners/{id}` returns 200 with owner DTO or 404 if not found
  - POST `/api/owners` returns 201 with Location header on success, 400 with JSON errors on validation failure
  - PUT `/api/owners/{id}` returns 204 on success, 400 if body ID doesn't match path ID
  - DELETE `/api/owners/{id}` returns 204 on success, 404 if not found

  Contract gaps addressed with characterization tests:
  - Concurrent owner creation behavior
  - Invalid pet types or specialty associations
  - Pet name uniqueness validation
  - Pagination and filtering beyond last name search

  - **Oracle placement:** service stories own service-level oracles; endpoint
    stories own JAX-RS/RestAssured oracles. `STORY_SCOPE` may allowlist
    `*ExceptionMapper` / `@IfExists` types when the contract requires them
    in an earlier story.

- **Forbidden**: the fabrication tripwires relevant here.
  - Do not fabricate annotations (@Entity when legacy shows plain class)
  - Do not fabricate method signatures that differ from legacy
  - Do not change business logic behavior (e.g., owner pets sorting)
  - Do not remove @PreAuthorize security annotations (strategy decision pending)

## Done-criteria

Checkable, story-scoped:
- builds + `sensors.sh task` green at every commit; milestone green at
  story end
- All 53 circular group classes compile with Jakarta imports
- REST API endpoints functional: GET /api/owners, GET /api/owners/{id}, POST /api/owners
- All entity relationships maintained (Owner hasMany Pets, Visit belongsTo Pet, etc.)
- Service layer assertions pass: findOwnerByLastName returns correct counts
- REST layer status codes correct: 200 for success, 404 for not found, 201 for created
- CDI beans properly injected via constructor injection
- JPA repositories functional with EntityManager (where applicable)
- All characterization tests added for contract gaps pass
- Open design decisions documented (Spring Data JPA strategy, security approach)
- No compilation errors or test failures in the circular group
- Application serves REST API from /api/ endpoints
- Deploy story: factory pipeline green, deployed, acceptance path serving
