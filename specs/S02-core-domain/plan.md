# S02: Core Domain Plan

## Findings Resolution Strategy

This plan maps each mandatory finding to its Quarkus target, categorizing tasks as `rewrite` (mechanical annotation/import/dependency changes) or `infer` (judgment calls requiring design decisions).

### javax-to-jakarta-import-00001 — HARVEST
**Finding**: All entities, mappers, DTOs, and generated code use `javax.*` imports
**Target**: Update `javax.persistence.*` → `jakarta.persistence.*` and `javax.validation.*` → `jakarta.validation.*`
**Class**: rewrite
**Evidence**: 
- `src/main/java/org/springframework/samples/petclinic/model/Owner.java:22-24` - `javax.persistence.*` and `javax.validation.*` imports
- `src/main/java/org/springframework/samples/petclinic/mapper/OwnerMapper.java` - uses `javax.validation.*` in DTOs
- `target/generated-sources/openapi/.../OwnerDto.java` - generated DTOs with `javax.validation.*`

**Scope**: 11 model entities (BaseEntity, NamedEntity, Person, Owner, Pet, PetType, Role, Specialty, User, Vet, Visit), 13 DTOs, 7 mappers, 7 generated mapper implementations

### springboot-di-to-quarkus-00003 — REDESIGN  
**Finding**: Spring DI annotations (@Autowired, @Inject, @Resource) need Quarkus conversion
**Target**: Replace @Autowired with CDI @Inject, convert @Service, @Repository to @ApplicationScoped
**Class**: rewrite
**Evidence**:
- `src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java:58` - @Autowired constructor injection
- `src/main/java/org/springframework/samples/petclinic/repository/jdbc/JdbcOwnerRepositoryImpl.java:62` - @Autowired constructor injection  
- `src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:47-50` - constructor injection pattern

**Scope**: 2 services (ClinicServiceImpl, UserServiceImpl), 19 repository implementations, 8 REST controllers, 7 generated mapper implementations

### spring-components-00001/00002 — REDESIGN
**Finding**: Spring component version incompatibilities resolved through conversion
**Target**: Update Spring Boot dependencies to Quarkus equivalents, remove Spring Boot auto-configuration
**Class**: infer  
**Evidence**:
- Spring Boot starter dependencies in pom.xml
- Spring Boot main application class configuration
- Spring Security, Spring Data JPA configurations

**Scope**: pom.xml dependencies, Spring Boot bootstrap removal

### springboot-web-to-quarkus-00000 — REDESIGN
**Finding**: Spring Web annotations need JAX-RS conversion
**Target**: Convert @RestController, @RequestMapping to JAX-RS @Path, @GET, @POST, etc.
**Class**: infer
**Evidence**:
- `src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:39-41` - @RestController and @RequestMapping
- 8 REST controllers with Spring Web annotations

**Scope**: 8 REST controllers (OwnerRestController, PetRestController, VetRestController, VisitRestController, PetTypeRestController, SpecialtyRestController, UserRestController, RootRestController)

### springboot-webmvc-to-quarkus-00000 — REDESIGN
**Finding**: Spring MVC ResponseEntity and UriComponentsBuilder need JAX-RS equivalents
**Target**: Convert ResponseEntity → JAX-RS Response, UriComponentsBuilder → UriBuilder
**Class**: rewrite
**Evidence**:
- `src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:19-21` - Spring ResponseEntity imports
- `src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:29` - UriComponentsBuilder usage

**Scope**: All REST controllers using ResponseEntity and URI building

### springboot-jpa-to-quarkus-00000 — REDESIGN  
**Finding**: Spring JPA annotations and patterns need Hibernate/Quarkus conversion
**Target**: Replace @PersistenceContext with @Inject EntityManager, ensure transaction alignment
**Class**: rewrite
**Evidence**:
- `src/main/java/org/springframework/samples/petclinic/repository/jpa/JpaOwnerRepositoryImpl.java:71-72` - @PersistenceContext EntityManager injection
- 8 JPA repository implementations

**Scope**: 8 JPA repository implementations, transaction management patterns

### springboot-security-to-quarkus-00000 — OPEN DESIGN
**Finding**: Spring Security configuration needs Quarkus security or removal
**Target**: Decide between Quarkus security integration or security removal for demo simplicity
**Class**: infer
**Evidence**:
- `src/main/java/org/springframework/samples/petclinic/rest/OwnerRestController.java:52` - @PreAuthorize annotation
- `src/main/java/org/springframework/samples/petclinic/security/BasicAuthenticationConfig.java:17,22,40` - Spring Security configuration
- `src/main/java/org/springframework/samples/petclinic/security/Roles.java:5,8` - OWNER_ADMIN role definition

**Scope**: Security annotations on REST endpoints, security configuration classes

### springboot-cache-to-quarkus-00000 — REDESIGN
**Finding**: Spring @Cacheable annotation needs Quarkus caching conversion or removal
**Target**: Replace @Cacheable("vets") with Quarkus caching or remove if not critical
**Class**: rewrite  
**Evidence**:
- `src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java:264-267` - @Cacheable("vets") on findVets method

**Scope**: ClinicServiceImpl.findVets() caching strategy

### transaction-to-quarkus-00003 — REDESIGN
**Finding**: Spring @Transactional needs Quarkus transaction management
**Target**: Replace Spring @Transactional with Hibernate @Transactional or programmatic transaction
**Class**: rewrite
**Evidence**:
- `src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java:75+` - @Transactional annotations on service methods
- Repository implementations with transactional methods

**Scope**: Service layer @Transactional, JPA repository remove operations, transaction propagation

### localhost-jdbc-00002 — REDESIGN
**Finding**: Database connection configuration needs Quarkus datasource conversion
**Target**: Convert Spring Boot datasource properties to Quarkus format
**Class**: infer
**Evidence**:
- `src/main/resources/application.properties` - jdbc.url, jdbc.username, jdbc.password properties
- Database connection externalization requirements

**Scope**: Database configuration properties, connection string management

### springboot-metrics-to-quarkus-0200 — REDESIGN
**Finding**: Spring metrics and CallMonitoringAspect need Micrometer/Observability replacement
**Target**: Replace CallMonitoringAspect with Micrometer-based observability
**Class**: infer
**Evidence**:
- `src/main/java/org/springframework/samples/petclinic/util/CallMonitoringAspect.java:37,47,52,57,63,68` - AspectJ aspects for method monitoring

**Scope**: CallMonitoringAspect, method performance monitoring

### springboot-jmx-to-quarkus-00001 — REDESIGN
**Finding**: Spring JMX aspects need Quarkus monitoring/observability replacement
**Target**: Remove JMX and replace with Micrometer metrics
**Class**: infer
**Evidence**:
- JMX configuration in Spring Boot application
- CallMonitoringAspect with JMX integration

**Scope**: JMX configuration, monitoring aspects

## Task Ordering Strategy

Per PLANNING.md and dependency-order.md:
1. **Extensions and BOM first** - Update dependencies and remove Spring Boot
2. **Models second** - HARVEST entities and mappers with jakarta updates
3. **Resources third** - REDESIGN services, repositories, and REST controllers
4. **Config fourth** - Database configuration, security decisions
5. **Tests last** - Characterization tests and validation

**Rewrite tasks** (mechanical) before **infer tasks** (judgment) within each phase.
**Circular group conversion** - All 53 classes must convert together due to bidirectional dependencies.
**Package mapping** - `org.springframework.samples.petclinic` → `com.demo` (full prefix replacement)

## Open Design Decisions Required

1. **Spring Data JPA Strategy**: Decide between Quarkus Panache or manual JPA for Spring Data repositories
2. **Security Approach**: Quarkus security integration vs security removal for demo
3. **Transaction Management**: Hibernate @Transactional vs programmatic transaction control
4. **Caching Strategy**: Quarkus caching vs cache removal for vet list
5. **Configuration Approach**: Database connection strategy for deployment
