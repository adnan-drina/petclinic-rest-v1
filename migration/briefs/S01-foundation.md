# S01: Foundation and utility classes

<!-- The brief is the self-contained work order for one modernization
     story. Bar: a competent developer or a fresh session starts the
     story from THIS FILE ALONE. Fill every section; delete none. -->

## Goal & position

What this story achieves and why it is next: its place in the roadmap,
what it unblocks, which stories it depends on (cite
dependency-order.md / architecture-profile.md).

This story establishes the foundational infrastructure classes that all other components depend upon. BaseEntity is extended by every JPA entity in the system (Owner, Pet, Visit, etc.), BindingErrorsResponse is used by all REST controllers for validation error handling, and utility classes provide shared functionality. These classes must be modernized to Jakarta namespaces and Quarkus patterns before any dependent classes can be converted. This is the first story as dictated by the dependency order analysis in migration/dependency-order.md.

## In scope

The exact legacy classes/files this story modernizes. For each, quote
the load-bearing legacy code (the lines being transformed — imports,
annotations, key methods), so the story never starts from a blank
read:

- `src/main/java/org/springframework/samples/petclinic/model/BaseEntity.java` — base entity with id and audit fields
  ```java
  import javax.persistence.GeneratedValue;
  import javax.persistence.GenerationType;
  import javax.persistence.Id;
  import javax.persistence.MappedSuperclass;

  @MappedSuperclass
  public class BaseEntity {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      protected Integer id;
  ```

- `src/main/java/org/springframework/samples/petclinic/rest/BindingErrorsResponse.java` — validation error response
  ```java
  import org.springframework.validation.BindingResult;
  import org.springframework.validation.FieldError;

  public class BindingErrorsResponse {

      public BindingErrorsResponse() {
          this(null);
      }

      public BindingErrorsResponse(Integer id) {
          this(null, id);
      }
  ```

- `src/main/java/org/springframework/samples/petclinic/util/EntityUtils.java` — removed utility class
  ```java
  // EntityUtils is identified for removal and replacement with Java Stream API
  ```

- `src/main/java/org/springframework/samples/petclinic/model/package-info.java` — model package documentation
- `src/main/java/org/springframework/samples/petclinic/repository/jdbc/package-info.java` — JDBC repository package documentation  
- `src/main/java/org/springframework/samples/petclinic/repository/jpa/package-info.java` — JPA repository package documentation
- `src/main/java/org/springframework/samples/petclinic/rest/package-info.java` — REST package documentation

## Out of scope

What neighboring code this story must NOT touch, and which story owns
it. (The tree must stay buildable: name any temporary seams — e.g. a
dependent class that keeps compiling against the old shape until its
own story.)

All JPA entities (Owner, Pet, Visit, etc.), REST controllers, services, repositories, and mappers are out of scope and owned by S02. Entity classes will continue using the old javax.persistence imports until S02 converts them to jakarta.persistence. This temporary state is acceptable as these files compile against the updated BaseEntity foundation.

## Class roles & target contract (from architecture-profile §7)

For each in-scope class, its role and — for REDESIGN classes — the target
contract carried forward from profile §7, so M3 writes tasks and tests to
the target (not the legacy):

- `BaseEntity` — HARVEST
  - Role: Foundation entity that provides id and audit capabilities to all domain entities
  - Target: Update javax.persistence.* imports to jakarta.persistence.*, maintain @MappedSuperclass, @Id, @GeneratedValue annotations and getId()/setId()/isNew() methods
  
- `BindingErrorsResponse` — HARVEST
  - Role: JSON error response formatting for validation failures
  - Target: Update Spring validation imports to Bean Validation equivalent patterns, maintain error aggregation and JSON serialization

- `EntityUtils` — REDESIGN (removal)
  - Role: Utility methods for entity operations
  - Target: Completely removed and replaced with Java Stream API calls in dependent classes

- Package-info files — HARVEST
  - Role: Documentation and package-level annotations
  - Target: Updated with Jakarta/JPA 3.0 documentation

## Decided target shapes

The MAPPINGS.md rows that apply (quote the decided target, don't
re-decide). Recipe-executed rules already handled: reference
`migration/recipe-log.md` and `migration/staging/` where applicable.

**javax-to-jakarta-import-00001**: Update all javax.persistence.* imports to jakarta.persistence.* for BaseEntity
**springboot-annotations-to-quarkus-00002**: Remove Spring ComponentScan patterns, rely on CDI discovery

**Story ordering:** extensions and BOM first, then models, then resources,
then config keys, then tests (`extensions → models → resources → config →
tests`).

## Contracts owned by this story

- **Findings**: the mandatory rule ids this story resolves (from the
  roadmap entry).
  - javax-to-jakarta-import-00001: BaseEntity and package-info files
  - springboot-annotations-to-quarkus-00002: Package metadata updates

- **Preserve**: the `preserve:` items whose surfaces live in scope —
  spell out the env var names/values mechanism to keep.
  None - foundation classes don't handle external configuration

- **Behavioral pins**: the assertion values that must hold after this
  story (quote numbers/strings and their test source). Harvest classes
  and behavior-preserving redesign pin LEGACY values; behavior-changing
  redesign pins the §7 TARGET (e.g. 404, not create-on-GET). Name the
  contract GAPS this story closes with characterization tests.
  - BaseEntity.isNew() behavior: returns true when id == null (maintain legacy contract)
  - BindingErrorsResponse error aggregation: maintains JSON error format

  - **Oracle placement:** service stories own service-level oracles; endpoint
    stories own JAX-RS/RestAssured oracles. `STORY_SCOPE` may allowlist
    `*ExceptionMapper` / `@IfExists` types when the contract requires them
    in an earlier story.

- **Forbidden**: the fabrication tripwires relevant here.
  Do not modify EntityUtils methods - entire class is removed in favor of Stream API

## Done-criteria

Checkable, story-scoped:
- builds + `sensors.sh task` green at every commit; milestone green at
  story end
- BaseEntity updated with Jakarta imports while maintaining all methods
- BindingErrorsResponse maintains error handling contract
- EntityUtils removed and no compilation errors in dependent code
- Package-info files updated with proper Jakarta documentation
- All javax→jakarta imports converted without breaking compilation
- All tests that depend on BaseEntity behavior still pass
- No new findings generated for these specific classes
