package com.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotEmpty;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NamedEntityTest {

    static class TestNamedEntity extends NamedEntity {
    }

    private TestNamedEntity entity;

    @BeforeEach
    void setUp() {
        entity = new TestNamedEntity();
    }

    @Nested
    @DisplayName("getName and setName")
    class NameAccessors {

        @Test
        @DisplayName("name is null by default")
        void nameIsNullByDefault() {
            assertThat(entity.getName()).isNull();
        }

        @Test
        @DisplayName("setName sets the name")
        void setNameSetsName() {
            entity.setName("Test Name");
            assertThat(entity.getName()).isEqualTo("Test Name");
        }

        @Test
        @DisplayName("setName overwrites previous name")
        void setNameOverwritesPreviousName() {
            entity.setName("First Name");
            entity.setName("Second Name");
            assertThat(entity.getName()).isEqualTo("Second Name");
        }

        @Test
        @DisplayName("setName to null resets name")
        void setNameToNull() {
            entity.setName("Some Name");
            entity.setName(null);
            assertThat(entity.getName()).isNull();
        }

        @Test
        @DisplayName("setName with empty string")
        void setNameWithEmptyString() {
            entity.setName("");
            assertThat(entity.getName()).isEmpty();
        }
    }

    @Nested
    @DisplayName("toString")
    class ToStringBehavior {

        @Test
        @DisplayName("returns name when name is set")
        void returnsNameWhenSet() {
            entity.setName("Specialty Name");
            assertThat(entity).hasToString("Specialty Name");
        }

        @Test
        @DisplayName("returns null when name is null")
        void returnsNullWhenNameIsNull() {
            assertThat(entity.toString()).isNull();
        }

        @Test
        @DisplayName("returns empty string when name is empty")
        void returnsEmptyStringWhenNameIsEmpty() {
            entity.setName("");
            assertThat(entity.toString()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Jakarta imports verification")
    class JakartaImportsVerification {

        @Test
        @DisplayName("has Jakarta persistence imports")
        void hasJakartaPersistenceImports() throws Exception {
            try {
                var columnAnnotation = NamedEntity.class.getDeclaredField("name")
                    .getAnnotation(Column.class);
                assertThat(columnAnnotation).isNotNull();
                assertThat(columnAnnotation.name()).isEqualTo("name");
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }

        @Test
        @DisplayName("has Jakarta validation @NotEmpty import")
        void hasJakartaValidationNotEmpty() throws Exception {
            try {
                var notEmptyAnnotation = NamedEntity.class.getDeclaredField("name")
                    .getAnnotation(NotEmpty.class);
                assertThat(notEmptyAnnotation).isNotNull();
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }

        @Test
        @DisplayName("has MappedSuperclass annotation")
        void hasMappedSuperclassAnnotation() {
            assertThat(NamedEntity.class.isAnnotationPresent(MappedSuperclass.class)).isTrue();
        }

        @Test
        @DisplayName("extends BaseEntity")
        void extendsBaseEntity() {
            assertThat(entity).isInstanceOf(BaseEntity.class);
        }
    }

    @Nested
    @DisplayName("Entity lifecycle inherited from BaseEntity")
    class EntityLifecycleFromBaseEntity {

        @Test
        @DisplayName("inherits id lifecycle methods")
        void inheritsIdLifecycleMethods() {
            assertThat(entity.getId()).isNull();
            entity.setId(42);
            assertThat(entity.getId()).isEqualTo(42);
        }

        @Test
        @DisplayName("inherits isNew lifecycle method")
        void inheritsIsNewLifecycleMethod() {
            assertThat(entity.isNew()).isTrue();
            entity.setId(1);
            assertThat(entity.isNew()).isFalse();
        }

        @Test
        @DisplayName("is new when both id and name are null")
        void isNewWhenBothIdAndNameAreNull() {
            assertThat(entity.isNew()).isTrue();
        }

        @Test
        @DisplayName("is not new when id is set even if name is null")
        void isNotNewWhenIdIsSet() {
            entity.setId(1);
            assertThat(entity.isNew()).isFalse();
        }
    }
}