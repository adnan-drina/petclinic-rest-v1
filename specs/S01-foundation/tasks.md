# S01 Foundation and Utility Classes - Implementation Tasks

## Package Structure Setup

#### T-001: Create target package directory structure

**Class: rewrite**

**Target design**: Create `src/main/java/com/demo` directory structure with `.gitkeep` files

**Evidence**: Foundation classes must be placed in `com.demo` package per `migration.yaml` targetPackage

**Task Details**:
- Create `src/main/java/com/demo` directory structure
- Add `.gitkeep` files to maintain empty directory structure in git
- Verify package naming matches `migration.yaml` targetPackage exactly
- **Required**: Directory structure must contain `.gitkeep` or `package-info.java` for git commitability

**Target**: → `src/main/java/com/demo/`
**Owns**: Empty directory creation (required for commitability)

---

#### T-002: Harvest BaseEntity from staging with Jakarta imports

**Class: rewrite**

**Findings**: javax-to-jakarta-import-00001

**Target design**: → `src/main/java/com/demo/model/BaseEntity.java`

**Evidence**: Legacy file `org/springframework/samples/petclinic/model/BaseEntity.java` with javax imports at lines 18-21

**Task Details**:
- Copy from `migration/staging/src/main/java/org/springframework/samples/petclinic/model/BaseEntity.java`  
- Update package: `org.springframework.samples.petclinic.model` → `com.demo.model`
- Update imports: `javax.persistence.*` → `jakarta.persistence.*`
- Verify `@MappedSuperclass`, `@Id`, `@GeneratedValue` annotations preserved
- Ensure `getId()`, `setId()`, `isNew()` methods unchanged
- Maintain `@JsonIgnore` on `isNew()` method

**Target**: → `src/main/java/com/demo/model/BaseEntity.java`
**Owns**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/model/BaseEntity.java`

---

#### T-003: Update package-info files with Jakarta documentation

**Class: rewrite**

**Findings**: javax-to-jakarta-import-00001

**Target design**: Updated package-info.java files in com.demo package structure

**Evidence**: Legacy package-info files in model, repository, and rest packages

**Task Details**:
- Identify all package-info.java files in legacy repository packages
- Update package declarations from `org.springframework.samples.petclinic.*` to `com.demo.*`
- Update package-level JavaDoc to reference Jakarta/JPA 3.0 instead of Spring
- Add Jakarta namespace documentation where appropriate
- Maintain package-level annotations if any exist
- Create package-info.java in target directories to satisfy commitability requirements

**Target**: → `src/main/java/com/demo/*/package-info.java` (multiple files)
**Owns**: All package-info.java files in legacy repository packages

---

#### T-004: Harvest BindingErrorsResponse from staging with Jakarta validation

**Class: rewrite**

**Findings**: springboot-annotations-to-quarkus-00002

**Target design**: → `src/main/java/com/demo/rest/BindingErrorsResponse.java`

**Evidence**: Legacy file `org/springframework/samples/petclinic/rest/BindingErrorsResponse.java` with Spring validation imports at lines 22-23

**Task Details**:
- Copy from `migration/staging/src/main/java/org/springframework/samples/petclinic/rest/BindingErrorsResponse.java`
- Update package: `org.springframework.samples.petclinic.rest` → `com.demo.rest`
- Replace Spring validation imports with Jakarta Bean Validation:
  - Remove: `org.springframework.validation.BindingResult`
  - Remove: `org.springframework.validation.FieldError`  
  - Add: `jakarta.validation.ConstraintViolation`
- Update `addAllErrors()` method to accept `List<ConstraintViolation<?>>` instead of `BindingResult`
- Convert `FieldError` processing to `ConstraintViolation` mapping
- Preserve JSON error format structure
- Maintain constructor overloads and error aggregation logic

**Target**: → `src/main/java/com/demo/rest/BindingErrorsResponse.java`
**Owns**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/rest/BindingErrorsResponse.java`

---

#### T-005: Handle ApplicationSwaggerConfig ComponentScan removal

**Class: rewrite**

**Findings**: springboot-annotations-to-quarkus-00002

**Target design**: → `src/main/java/com/demo/util/ApplicationSwaggerConfig.java` (removed/refactored)

**Evidence**: Legacy file `org/springframework/samples/petclinic/util/ApplicationSwaggerConfig.java` with Spring `@ComponentScan` at line 52

**Task Details**:
- Identify ApplicationSwaggerConfig usage and purpose
- Remove or refactor to use Quarkus native OpenAPI/Swagger instead
- Update any imports and dependencies to Quarkus equivalents
- Ensure no compilation errors from removing Spring ComponentScan
- Document removal or refactoring decision

**Owns**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/util/ApplicationSwaggerConfig.java`

---

#### T-006: Remove EntityUtils class and refactor usage sites

**Class: infer**

**Target design**: Complete removal + Stream API migration

**Evidence**: Legacy file `org/springframework/samples/petclinic/util/EntityUtils.java` identified for removal in brief

**Task Details**:
- Remove `src/main/java/org/springframework/samples/petclinic/util/EntityUtils.java` (if exists in modernized)
- Find all usage sites of `EntityUtils.getById()` in legacy code
- Refactor each usage to Java Stream API:
  ```java
  // From: EntityUtils.getById(collection, EntityClass, id)
  // To: collection.stream().filter(e -> e.getId().equals(id)).findFirst()
  ```
- Replace `ObjectRetrievalFailureException` with `NoSuchElementException` from `findFirst()`
- Test that entity lookup behavior remains identical
- Verify no compilation errors in dependent code

**Absorbs**: `/projects/legacy/src/main/java/org/springframework/samples/petclinic/util/EntityUtils.java`
**Note**: Complete class removal - no target file created

---

#### T-007: Create BaseEntity characterization tests

**Class: infer**

**Target design**: → `src/test/java/com/demo/model/BaseEntityTest.java`

**Evidence**: BaseEntity behavioral contract requires testing

**Task Details**:
- Create unit tests for BaseEntity class
- Test `isNew()` behavior: returns `true` when `id == null`
- Test `isNew()` behavior: returns `false` when `id != null` 
- Test getter/setter for `id` field
- Verify Jackson `@JsonIgnore` annotation on `isNew()` method
- Test serialization/deserialization behavior
- Use JUnit 5 and assertJ for testing

**Behavioral Pins**: BaseEntity.isNew() returns true when id == null (legacy contract)

**Target**: → `src/test/java/com/demo/model/BaseEntityTest.java`
**Note**: Characterizes foundation class behavior before dependent classes

---

#### T-008: Create BindingErrorsResponse characterization tests  

**Class: infer**

**Target design**: → `src/test/java/com/demo/rest/BindingErrorsResponseTest.java`

**Evidence**: BindingErrorsResponse JSON contract requires validation

**Task Details**:
- Create unit tests for BindingErrorsResponse class
- Test constructor scenarios: empty, single ID, path/body ID mismatch
- Test `addError()` method for single error addition
- Test `addAllErrors()` method with ConstraintViolation list
- Verify JSON serialization produces expected format:
  ```json
  [
    {
      "objectName": "className",
      "fieldName": "propertyName", 
      "fieldValue": "rejectedValue",
      "errorMessage": "validation message"
    }
  ]
  ```
- Test JSON error format matches legacy structure exactly
- Use Mockito to create mock ConstraintViolation objects for testing

**Behavioral Pins**: BindingErrorsResponse error aggregation maintains JSON error format

**Target**: → `src/test/java/com/demo/rest/BindingErrorsResponseTest.java`
**Note**: Validates JSON contract maintained during Spring→Jakarta migration

---

#### T-009: Create EntityUtils migration integration tests

**Class: infer**

**Target design**: → `src/test/java/com/demo/util/EntityUtilsMigrationTest.java`

**Evidence**: EntityUtils removal requires integration testing

**Task Details**:
- Create integration tests verifying Stream API replacement works correctly
- Test entity lookup behavior: finds entity when present
- Test entity lookup behavior: throws exception when not found
- Test with collections of different entity types (Owner, Pet, Visit, etc.)
- Verify `NoSuchElementException` replaces `ObjectRetrievalFailureException`
- Test null handling and edge cases
- Create sample entity collections matching legacy usage patterns
- **Required**: Test directory must contain `.gitkeep` or package-info.java for commitability

**Note**: Tests verify EntityUtils replacement maintains identical behavior

**Target**: → `src/test/java/com/demo/util/EntityUtilsMigrationTest.java`
**Absorbs**: EntityUtils removal verification

---

## Story Completion Criteria

**Deployment Status**: `deploy=false`

Per **O-M3ACCEPT** guidance, when `deploy=false`, do not task the full literal `acceptance.path` with Java @Path/endpoint substance. The acceptance.path is deferred to the deploy story (S-AC1/G-OK).

**Summary of Changes**:
- ✓ Jakarta imports converted (javax→jakarta)
- ✓ BaseEntity foundation modernized  
- ✓ BindingErrorsResponse updated to Jakarta validation
- ✓ EntityUtils removed and replaced with Stream API
- ✓ Package structure created in com.demo
- ✓ Characterization tests validate behavioral contracts
- ✓ All findings resolved: javax-to-jakarta-import-00001, springboot-annotations-to-quarkus-00002

**Verification**:
- Build passes: `mvn -q clean test`
- All foundation classes compile against Jakarta imports
- Dependent classes compile against updated BaseEntity
- Tests validate behavioral contracts preserved

**UI Surface Coverage**: 
- **Explicitly waived**: Story S01 (foundation) does not cover the legacy web UI surface as it operates at the foundation level. All web endpoints and UI functionality are deferred to later stories (S02+). This foundation story provides only base entity classes and validation error responses used by the UI layer but does not expose any user-facing interfaces.

**Preserved Integration Coverage**:
- **petclinic.security.enable**: Explicitly deferred to security story. Foundation classes do not handle security configuration - this is an integration concern that will be addressed when Spring Security is migrated to Quarkus security in a later story.
- **server.servlet.context-path**: Explicitly deferred to deployment configuration story. Foundation classes do not handle servlet context path configuration - this is a runtime deployment concern handled at the application level in later stories.