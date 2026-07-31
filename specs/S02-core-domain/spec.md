# S02: Core Domain Specification

## Observed Legacy Behavior

This story modernizes the tightly coupled circular group of 53 classes containing all model entities, repositories, services, mappers, and REST controllers that implement the core PetClinic CRUD functionality.

### Model Layer (HARVEST)
**Entities**: Owner, Pet, Visit, PetType, Specialty, Vet, Role, User, BaseEntity, NamedEntity, Person
- **JPA Entities**: All entities use `javax.persistence.*` imports and annotations (`@Entity`, `@Table`, `@Column`, `@OneToMany`, `@ManyToOne`, `@JoinColumn`)
- **Validation**: Use `javax.validation.constraints.*` annotations (`@NotEmpty`, `@Digits`)
- **Relationships**: Complex bidirectional relationships between entities (Owner hasMany Pets, Pet belongsTo Owner and PetType, Visit belongsTo Pet)
- **Evidence**: `src/main/java/org/springframework/samples/petclinic/model/Owner.java:22-24` - uses `javax.persistence.*` and `javax.validation.constraints.*`
- **Evidence**: `src/main/java/org/springframework/samples/petclinic/model/Owner.java:52-53` - `@OneToMany` relationship with cascade and eager fetch

**Repository Interfaces**: Pure interfaces for data access (OwnerRepository, PetRepository, PetTypeRepository, SpecialtyRepository, UserRepository, VetRepository, VisitRepository)
- **Evidence**: `src/main/java/org/springframework/samples/petclinic/repository/OwnerRepository.java` - interface with CRUD methods

**DTOs**: Generated OpenAPI classes for JSON serialization (OwnerDto, PetDto, VisitDto, etc.)
- **Evidence**: `target/generated-sources/openapi/.../OwnerDto.java:13-14` - uses `javax.validation.*` annotations

**Mappers**: MapStruct interfaces for entity-to-DTO conversion (OwnerMapper, PetMapper, etc.)
- **Evidence**: `src/main/java/org/springframework/samples/petclinic/mapper/OwnerMapper.java:12` - `@Mapper` annotation with `uses = PetMapper.class`

### Service Layer (REDESIGN)
**ClinicServiceImpl**: Primary facade service aggregating all repository operations
- **Spring Annotations**: `@Service` (line 47), `@Transactional` (line 75+), `@Cacheable("vets")` (line 264)
- **Dependency Injection**: `@Autowired` constructor injection (lines 58-72) with 6 repository dependencies
- **Caching**: `findVets()` method uses `@Cacheable("vets")` annotation
- **Evidence**: `src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java:47-72` - shows constructor injection pattern
- **Evidence**: `src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java:264-267` - cached vet list retrieval

**UserServiceImpl**: User management service
- **Spring Annotations**: `@Service`, `@Transactional`, `@Autowired` field injection
- **Evidence**: `src/main/java/org/springframework/samples/petclinic/service/UserServiceImpl.java:10` - `@Service` annotation

### Repository Implementation Layer (REDESIGN)

**JDBC Implementations**: 7 classes (JdbcOwnerRepositoryImpl, JdbcPetRepositoryImpl, etc.)
- **Spring Annotations**: `@Repository`, `@Profile("jdbc")`, `@Autowired` constructor injection
- **DataSource**: Injected via `@Autowired` constructor (DataSource parameter)
- **Evidence**: `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcOwnerRepositoryImpl.java:54-56` - shows `@Repository` and `@Profile` annotations
- **Evidence**: `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcOwnerRepositoryImpl.java:62-64` - `@Autowired` constructor injection

**JPA Implementations**: 8 classes (JpaOwnerRepositoryImpl, JpaPetRepositoryImpl, etc.)
- **Spring Annotations**: `@Repository`, `@Profile("jpa")`, `@PersistenceContext` for EntityManager
- **Evidence**: `src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaOwnerRepositoryImpl.java:67-72` - shows `@PersistenceContext` EntityManager injection

**Spring Data JPA Implementations**: 4 classes (SpringDataPetRepositoryImpl, etc.)
- **Spring Annotations**: `@Repository`, `@Profile("springdatajpa")`
- **OPEN DESIGN**: Strategy decision pending between Quarkus Panache vs manual JPA

### REST Controller Layer (REDESIGN)

**OwnerRestController**: Primary REST controller for owner management
- **Spring Annotations**: `@RestController` (line 39), `@CrossOrigin` (line 40), `@RequestMapping("/api/owners")` (line 41)
- **Security**: `@PreAuthorize("hasRole(@roles.OWNER_ADMIN)")` (line 52)
- **HTTP Methods**: GET, POST, PUT, DELETE endpoints with proper HTTP status codes
- **Validation**: Uses `BindingResult` for validation error handling
- **Evidence**: `src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:52` - shows security annotation
- **Evidence**: `src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:47-50` - constructor injection

**API Contract**: 
- GET `/api/owners` - returns 200 with collection or 404 if empty
- GET `/api/owners/{id}` - returns 200 with owner or 404 if not found
- POST `/api/owners` - returns 201 with Location header or 400 with validation errors
- PUT `/api/owners/{id}` - returns 204 on success or 400 on validation failure
- DELETE `/api/owners/{id}` - returns 204 or 404 if not found

### Behavioral Contracts

**Service Layer Tests** (AbstractClinicServiceTests.java):
- `findOwnerByLastName("Davis")` returns exactly 2 owners (legacy behavior to preserve)
- `findOwnerById(1)` returns owner with last name "Franklin" and 1 pet of type "cat"
- `saveOwner()` assigns generated ID and makes entity findable by last name
- `updateOwner()` modifies existing owner without changing ID

**REST Layer Tests** (OwnerRestControllerTests.java):
- Proper HTTP status codes: 200 (success), 404 (not found), 201 (created), 204 (no content), 400 (bad request)
- JSON error responses using `BindingErrorsResponse`
- Location headers for POST operations
- Role-based access control via `@PreAuthorize`

## API Contract (from architecture profile §7)

### Target Contract for Redesigned Classes

**Services**:
- `@ApplicationScoped` CDI beans with constructor injection
- Replace `@Autowired` with `@Inject`
- Replace `@Transactional` with Hibernate `@Transactional` or programmatic transaction management
- Replace `@Cacheable("vets")` with Quarkus caching or remove if not critical
- Concurrency: ClinicServiceImpl uses mutable collections, thread-safe via ConcurrentHashMap patterns

**REST Controllers**:
- `@ApplicationScoped @Path("/api/owners")` with constructor injection
- JAX-RS annotations: `@Path`, `@GET`, `@POST`, `@PUT`, `@DELETE`, `@Produces`, `@Consumes`
- Replace `ResponseEntity` with JAX-RS `Response` and `ResponseBuilder`
- Replace `BindingResult` with Bean Validation `@Valid` and `ExceptionMapper`
- API contract: GET returns 404 on missing, POST with `@Valid` rejects with 400, downstream failures map to 503
- Remove `@PreAuthorize` or replace with Quarkus security (OPEN DESIGN)

**Repository Implementations**:
- `@ApplicationScoped` CDI beans with constructor injection
- Replace `@Autowired` with `@Inject`
- Remove `@Repository` and `@Profile` annotations
- Replace `@PersistenceContext` with `@Inject EntityManager`
- Ensure transaction management aligns with Quarkus/Hibernate patterns

### Package Mapping
- **Source**: `org.springframework.samples.petclinic` 
- **Target**: `com.demo` (full prefix replacement per migration.yaml)

### Migration.yaml Contracts
- **Preserve**: REST API base path "/api/*" maintained for backward compatibility
- **Preserve**: Database connection properties externalized (jdbc.url, jdbc.username, jdbc.password)
- **Deploy Story**: Story deploy=false, so do not implement acceptance.path endpoint substance - deferred to S-AC1/G-OK
