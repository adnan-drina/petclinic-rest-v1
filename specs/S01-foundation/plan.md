# S01 Foundation and Utility Classes - Migration Plan

## Quarkus Mapping Strategy

### BaseEntity → Jakarta JPA Foundation

**Class Role**: HARVEST - carry over faithfully with namespace updates

**Target Design**: 
```java
package com.demo.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Simple JavaBean domain object with an id property. Used as a base class for objects needing this property.
 * 
 * @author Ken Krebs
 * @author Juergen Hoeller
 */
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    @JsonIgnore
    public boolean isNew() {
        return this.id == null;
    }
}
```

**Mapping Decisions** (per MAPPINGS.md):
- **javax-to-jakarta-import-00001**: Replace all `javax.persistence.*` imports with `jakarta.persistence.*`
- **Class: rewrite** - mechanical import swap, no behavioral change
- Package rename: `org.springframework.samples.petclinic.model` → `com.demo.model`

### BindingErrorsResponse → Jakarta Bean Validation

**Class Role**: HARVEST - carry over with validation API modernization

**Target Design**:
```java
package com.demo.rest;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Vitaliy Fedoriv
 */
public class BindingErrorsResponse {

    public BindingErrorsResponse() {
        this(null);
    }

    public BindingErrorsResponse(Integer id) {
        this(null, id);
    }

    public BindingErrorsResponse(Integer pathId, Integer bodyId) {
        boolean onlyBodyIdSpecified = pathId == null && bodyId != null;
        if (onlyBodyIdSpecified) {
            addBodyIdError(bodyId, "must not be specified");
        }
        boolean bothIdsSpecified = pathId != null && bodyId != null;
        if (bothIdsSpecified && !pathId.equals(bodyId)) {
            addBodyIdError(bodyId, String.format("does not match pathId: %d", pathId));
        }
    }

    private void addBodyIdError(Integer bodyId, String message) {
        BindingError error = new BindingError();
        error.setObjectName("body");
        error.setFieldName("id");
        error.setFieldValue(bodyId.toString());
        error.setErrorMessage(message);
        addError(error);
    }

    private final List<BindingError> bindingErrors = new ArrayList<BindingError>();

    public void addError(BindingError bindingError) {
        this.bindingErrors.add(bindingError);
    }

    public void addAllErrors(List<ConstraintViolation<?>> violations) {
        for (ConstraintViolation<?> violation : violations) {
            BindingError error = new BindingError();
            error.setObjectName(violation.getLeafBean().getClass().getSimpleName());
            error.setFieldName(violation.getPropertyPath().toString());
            error.setFieldValue(String.valueOf(violation.getInvalidValue()));
            error.setErrorMessage(violation.getMessage());
            addError(error);
        }
    }

    public String toJSON() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        String errorsAsJSON = "";
        try {
            errorsAsJSON = mapper.writeValueAsString(bindingErrors);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return errorsAsJSON;
    }

    @Override
    public String toString() {
        return "BindingErrorsResponse [bindingErrors=" + bindingErrors + "]";
    }

    protected static class BindingError {
        private String objectName;
        private String fieldName;
        private String fieldValue;
        private String errorMessage;

        public BindingError() {
            this.objectName = "";
            this.fieldName = "";
            this.fieldValue = "";
            this.errorMessage = "";
        }

        protected void setObjectName(String objectName) {
            this.objectName = objectName;
        }

        protected void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        protected void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }

        protected void setErrorMessage(String error_message) {
            this.errorMessage = error_message;
        }

        @Override
        public String toString() {
            return "BindingError [objectName=" + objectName + ", fieldName=" + fieldName + ", fieldValue=" + fieldValue
                    + ", errorMessage=" + errorMessage + "]";
        }
    }
}
```

**Mapping Decisions** (per MAPPINGS.md):
- **springboot-annotations-to-quarkus-00002**: Replace Spring validation with Jakarta Bean Validation
- **Class: rewrite** - API migration, maintain JSON format contract
- Package rename: `org.springframework.samples.petclinic.rest` → `com.demo.rest`
- Replace `org.springframework.validation.BindingResult` with `jakarta.validation.ConstraintViolation`
- Remove `org.springframework.validation.FieldError` dependency

### EntityUtils → Removal + Stream API Migration

**Class Role**: REDESIGN (removal) - eliminate and replace with Java Stream API

**Target Design**: Complete class removal + usage site refactoring

**Mapping Decisions** (per MAPPINGS.md):
- **Class: infer** - judgment call: remove utility, migrate to Stream API
- Replace all callsites: `EntityUtils.getById(collection, Class, id)` → `collection.stream().filter(e -> e.getId().equals(id)).findFirst()`
- Remove Spring `ObjectRetrievalFailureException` dependency

### Package-Info Files → Jakarta Documentation

**Class Role**: HARVEST - update documentation, maintain structure

**Target Design**: Update package-info.java files with Jakarta/JPA 3.0 documentation

**Mapping Decisions**:
- **Class: rewrite** - mechanical documentation updates
- Update package-level JavaDoc to reference Jakarta namespaces
- Package rename: maintain structure with new `com.demo` prefix

## Dependency Order Compliance

Per `migration/dependency-order.md`, foundation classes must convert first:

1. **BaseEntity** - foundation for all JPA entities
2. **BindingErrorsResponse** - used by all REST controllers  
3. **EntityUtils** - utility used throughout application
4. **Package-info** files - update after core classes

## Story Dependencies

**Depends on**: None (foundation story)
**Unblocks**: S02 (model entities), S03 (REST controllers), S04 (services)

This story establishes the foundation that all other components depend upon. BaseEntity is extended by every JPA entity, BindingErrorsResponse is used by all REST controllers, and utility classes provide shared functionality.

## Behavioral Preservation

### BaseEntity Contract (Legacy → Target)
- `isNew()` behavior: returns `true` when `id == null` ✓ PRESERVED
- JPA annotations: `@MappedSuperclass`, `@Id`, `@GeneratedValue` ✓ PRESERVED (Jakarta equivalent)
- Accessors: `getId()`, `setId()` ✓ PRESERVED
- JSON ignore on `isNew()` ✓ PRESERVED

### BindingErrorsResponse Contract (Legacy → Target)
- JSON error format: array of error objects with fieldName, fieldValue, errorMessage ✓ PRESERVED
- Constructor overloads: empty, ID, path/body ID ✓ PRESERVED  
- Error aggregation: single error + batch errors ✓ PRESERVED
- Serialization: `toJSON()` produces same structure ✓ PRESERVED

### EntityUtils Contract (Legacy → Replacement)
- Entity lookup behavior: find by ID in collection ✓ PRESERVED (via Stream API)
- Exception handling: throw when not found ✓ REPLACED with `NoSuchElementException` from `findFirst()`
- Performance: linear search maintained ✓ PRESERVED

## Quality Gates

1. **Compilation**: All foundation classes compile against Jakarta imports
2. **Dependency Test**: All entity classes compile against new BaseEntity
3. **Validation Test**: REST controllers compile against updated BindingErrorsResponse  
4. **Utils Test**: All EntityUtils callsites successfully refactored
5. **JSON Contract**: BindingErrorsResponse maintains identical JSON output format