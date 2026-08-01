# S03: Configuration and platform setup

<!-- The brief is the self-contained work order for one modernization
     story. Bar: a competent developer or a fresh session starts the
     story from THIS FILE ALONE. Fill every section; delete none. -->

## Goal & position

What this story achieves and why it is next: its place in the roadmap,
what it unblocks, which stories it depends on (cite
dependency-order.md / architecture-profile.md).

This story completes the platform modernization by converting the Spring Boot build configuration and application setup to Quarkus. It handles POM dependencies, build plugins, main application class removal, and property file conversions. The core domain functionality from S02 needs to be wrapped in proper Quarkus infrastructure to build, run, and deploy successfully. This story depends on S02's functional circular group and delivers a complete Quarkus application ready for native compilation and deployment.

## In scope

The exact legacy classes/files this story modernizes. For each, quote
the load-bearing legacy code (the lines being transformed — imports,
annotations, key methods), so the story never starts from a blank
read:

### Main Application Class (REDESIGN - removal)
- `src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java`
  ```java
  import org.springframework.boot.SpringApplication;
  import org.springframework.boot.autoconfigure.SpringBootApplication;

  @SpringBootApplication
  public class PetClinicApplication extends SpringBootServletInitializer {

      public static void main(String[] args) {
          SpringApplication.run(PetClinicApplication.class, args);
      }
  ```

### Build Configuration (REDESIGN - Quarkus platform)
- `pom.xml`
  ```xml
  <parent>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-parent</artifactId>
      <version>2.6.2</version>
  </parent>

  <dependencies>
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-actuator</artifactId>
      </dependency>
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-web</artifactId>
      </dependency>
  ```

### Application Properties (REDESIGN - Quarkus format)
- `src/main/resources/application.properties`
  ```properties
  spring.profiles.active=hsqldb,spring-data-jpa
  server.port=9966
  server.servlet.context-path=/petclinic/
  spring.mvc.pathmatch.matching-strategy=ant_path_matcher
  logging.level.org.springframework=INFO
  ```

- `src/main/resources/application-hsqldb.properties`
  ```properties
  spring.datasource.url=jdbc:hsqldb:mem:petclinic
  spring.datasource.username=sa 
  spring.datasource.password=
  spring.jpa.database=HSQL
  spring.jpa.database-platform=org.hibernate.dialect.HSQLDialect
  ```

- `src/main/resources/application-mysql.properties`
  ```properties
  spring.datasource.url = jdbc:mysql://localhost:3306/petclinic?useUnicode=true
  spring.datasource.username=pc
  spring.datasource.password=petclinic
  spring.jpa.database=MYSQL
  spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
  ```

- `src/main/resources/application-postgresql.properties`
  ```properties
  spring.datasource.url=jdbc:postgresql://localhost:5432/petclinic
  spring.datasource.username=postgres
  spring.datasource.password=petclinic
  spring.jpa.database=POSTGRESQL
  spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
  ```

## Out of scope

What neighboring code this story must NOT touch, and which story owns
it. (The tree must stay buildable: name any temporary seams — e.g. a
dependent class that keeps compiling against the old shape until its
own story.)

All application code (entities, services, repositories, controllers, mappers) is out of scope and already modernized in S02. This story only handles infrastructure and platform configuration. The circular group must remain functional throughout the platform conversion.

## Class roles & target contract (from architecture-profile §7)

For each in-scope class, its role and — for REDESIGN classes — the target
contract carried forward from profile §7, so M3 writes tasks and tests to
the target (not the legacy):

### PetClinicApplication (REDESIGN - removal)
- Role: Spring Boot bootstrap and configuration
- Target: Complete removal - Quarkus auto-discovers CDI beans without main class
- Concurrency: N/A (removed)

### pom.xml (REDESIGN - Quarkus platform)
- Role: Build configuration and dependency management
- Target: Quarkus platform BOM (com.redhat.quarkus.platform), Quarkus dependencies (quarkus-rest-jackson, quarkus-smallrye-health), Quarkus Maven plugin, native profile
- Concurrency: N/A (build configuration)

### Property Files (REDESIGN - Quarkus format)
- Role: Application configuration
- Target: Quarkus `application.properties` with `quarkus.datasource.*` aligned to **migration.yaml** (`needsDatabase: true` + deploy `dbService` PostgreSQL). **Primary DB is PostgreSQL only** (DECISION-DB / O-BRIEFCONF) — do not treat HSQLDB/MySQL profile files as deploy primaries. Preserve `quarkus.hibernate-orm.sql-load-script=import.sql` and explicit-column seed ownership already delivered for acceptance (schema/seed deliverable). Optional `%dev`/`application-*.properties` local profiles may remain for workstation use but must not flip default `db-kind` away from postgresql.

## Decided target shapes

The MAPPINGS.md rows that apply (quote the decided target, don't
re-decide). Recipe-executed rules already handled: reference
`migration/recipe-log.md` and `migration/staging/` where applicable.

**javaee-pom-to-quarkus-00010/00020/00030/00040/00050/00060**: Quarkus BOM, Maven plugin, compiler, surefire, failsafe, native profile
**springboot-parent-pom-to-quarkus-00000**: Replace Spring Boot parent with Quarkus platform BOM
**springboot-plugins-to-quarkus-0000**: Replace spring-boot-maven-plugin with quarkus-maven-plugin
**springboot-properties-to-quarkus-00001/00002/00003**: Convert Spring properties to Quarkus equivalents
**springboot-actuator-to-quarkus-0100**: Replace actuator with quarkus-smallrye-health (/q/health)
**springboot-annotations-to-quarkus-00000**: Remove @SpringBootApplication + main class
**springboot-metrics-to-quarkus-0100**: Replace Micrometer with quarkus-smallrye-metrics
**javax-to-jakarta-dependencies-00001/00003**: Update javax.xml.bind to jakarta.xml.bind-api

**OPEN DESIGN decisions** (deferred from S02):
- Spring Data JPA strategy: decide between Quarkus Panache or manual JPA
- Security approach: Quarkus security or authentication removal
- Caching strategy: Quarkus caching or removal

**Story ordering:** extensions and BOM first, then models, then resources,
then config keys, then tests (`extensions → models → resources → config →
tests`).

## Contracts owned by this story

- **Findings**: the mandatory rule ids this story resolves (from the
  roadmap entry).
  - javaee-pom-to-quarkus-00010/00020/00030/00040/00050/00060: Platform setup
  - springboot-parent-pom-to-quarkus-00000: Parent POM replacement
  - springboot-plugins-to-quarkus-0000: Maven plugin replacement
  - springboot-properties-to-quarkus-00001/00002/00003: Property conversions
  - springboot-actuator-to-quarkus-0100: Health endpoints
  - springboot-annotations-to-quarkus-00000: Application bootstrap
  - springboot-metrics-to-quarkus-0100: Metrics replacement
  - javax-to-jakarta-dependencies-00001/00003: Jakarta XML binding

- **Preserve**: the `preserve:` items whose surfaces live in scope —
  spell out the env var names/values mechanism to keep.
  - Database connection properties externalized (jdbc.url, jdbc.username, jdbc.password)
  - Application context path preserved: /petclinic/ → quarkus.http.context-path=/petclinic
  - Logging configuration maintained

- **Behavioral pins**: the assertion values that must hold after this
  story (quote numbers/strings and their test source). Harvest classes
  and behavior-preserving redesign pin LEGACY values; behavior-changing
  redesign pins the §7 TARGET (e.g. 404, not create-on-GET). Name the
  contract GAPS this story closes with characterization tests.

  Platform contracts:
  - Application builds successfully with Maven
  - Quarkus dev mode starts without errors
  - Health endpoint /q/health responds successfully
  - Database connections work with all three profiles (hsqldb, mysql, postgresql)
  - Application serves from /petclinic/ context path
  - Native compilation completes successfully

  - **Oracle placement:** service stories own service-level oracles; endpoint
    stories own JAX-RS/RestAssured oracles. `STORY_SCOPE` may allowlist
    `*ExceptionMapper` / `@IfExists` types when the contract requires them
    in an earlier story.

- **Forbidden**: the fabrication tripwires relevant here.
  - Do not modify business logic in application code
  - Do not change entity relationships or validation rules
  - Do not alter REST API contracts or endpoints
  - Do not break existing functionality during platform conversion

## Done-criteria

Checkable, story-scoped:
- builds + `sensors.sh task` green at every commit; milestone green at
  story end
- Maven build completes successfully with Quarkus platform
- Quarkus dev mode starts and serves application at /petclinic/
- Health endpoint /q/health responds with application status
- All three database profiles (hsqldb, mysql, postgresql) work correctly
- Property files converted to Quarkus format without breaking functionality
- Application runs in native mode successfully
- No compilation errors or runtime failures in converted code
- Open design decisions documented and implemented (security, data access, caching)
- Application maintains backward compatibility with existing API contracts
- Full build pipeline green: compile → test → package → native build
