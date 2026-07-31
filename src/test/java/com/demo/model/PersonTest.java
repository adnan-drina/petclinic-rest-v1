package com.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotEmpty;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PersonTest {

    static class TestPerson extends Person {
    }

    private TestPerson person;

    @BeforeEach
    void setUp() {
        person = new TestPerson();
    }

    @Nested
    @DisplayName("getFirstName and setFirstName")
    class FirstNameAccessors {

        @Test
        @DisplayName("firstName is null by default")
        void firstNameIsNullByDefault() {
            assertThat(person.getFirstName()).isNull();
        }

        @Test
        @DisplayName("setFirstName sets the firstName")
        void setFirstNameSetsFirstName() {
            person.setFirstName("John");
            assertThat(person.getFirstName()).isEqualTo("John");
        }

        @Test
        @DisplayName("setFirstName overwrites previous firstName")
        void setFirstNameOverwritesPreviousFirstName() {
            person.setFirstName("Alice");
            person.setFirstName("Bob");
            assertThat(person.getFirstName()).isEqualTo("Bob");
        }

        @Test
        @DisplayName("setFirstName to null resets firstName")
        void setFirstNameToNull() {
            person.setFirstName("Jane");
            person.setFirstName(null);
            assertThat(person.getFirstName()).isNull();
        }
    }

    @Nested
    @DisplayName("getLastName and setLastName")
    class LastNameAccessors {

        @Test
        @DisplayName("lastName is null by default")
        void lastNameIsNullByDefault() {
            assertThat(person.getLastName()).isNull();
        }

        @Test
        @DisplayName("setLastName sets the lastName")
        void setLastNameSetsLastName() {
            person.setLastName("Smith");
            assertThat(person.getLastName()).isEqualTo("Smith");
        }

        @Test
        @DisplayName("setLastName overwrites previous lastName")
        void setLastNameOverwritesPreviousLastName() {
            person.setLastName("Johnson");
            person.setLastName("Williams");
            assertThat(person.getLastName()).isEqualTo("Williams");
        }

        @Test
        @DisplayName("setLastName to null resets lastName")
        void setLastNameToNull() {
            person.setLastName("Brown");
            person.setLastName(null);
            assertThat(person.getLastName()).isNull();
        }
    }

    @Nested
    @DisplayName("Full name handling")
    class FullNameHandling {

        @Test
        @DisplayName("can construct full name from first and last")
        void canConstructFullNameFromFirstAndLast() {
            person.setFirstName("John");
            person.setLastName("Doe");
            var fullName = person.getFirstName() + " " + person.getLastName();
            assertThat(fullName).isEqualTo("John Doe");
        }

        @Test
        @DisplayName("handles null first name")
        void handlesNullFirstName() {
            person.setLastName("Smith");
            var fullName = person.getFirstName() + " " + person.getLastName();
            assertThat(fullName).isEqualTo("null Smith");
        }

        @Test
        @DisplayName("handles null last name")
        void handlesNullLastName() {
            person.setFirstName("Jane");
            var fullName = person.getFirstName() + " " + person.getLastName();
            assertThat(fullName).isEqualTo("Jane null");
        }

        @Test
        @DisplayName("handles both null names")
        void handlesBothNullNames() {
            var fullName = person.getFirstName() + " " + person.getLastName();
            assertThat(fullName).isEqualTo("null null");
        }

        @Test
        @DisplayName("handles empty strings")
        void handlesEmptyStrings() {
            person.setFirstName("");
            person.setLastName("");
            var fullName = person.getFirstName() + " " + person.getLastName();
            assertThat(fullName).isEqualTo(" ");
        }
    }

    @Nested
    @DisplayName("Jakarta imports verification")
    class JakartaImportsVerification {

        @Test
        @DisplayName("has Jakarta persistence Column imports")
        void hasJakartaPersistenceColumnImports() throws Exception {
            var firstNameField = Person.class.getDeclaredField("firstName");
            var columnAnnotation = firstNameField.getAnnotation(Column.class);
            assertThat(columnAnnotation).isNotNull();
            assertThat(columnAnnotation.name()).isEqualTo("first_name");

            var lastNameField = Person.class.getDeclaredField("lastName");
            columnAnnotation = lastNameField.getAnnotation(Column.class);
            assertThat(columnAnnotation).isNotNull();
            assertThat(columnAnnotation.name()).isEqualTo("last_name");
        }

        @Test
        @DisplayName("has Jakarta validation @NotEmpty imports")
        void hasJakartaValidationNotEmpty() throws Exception {
            var firstNameField = Person.class.getDeclaredField("firstName");
            assertThat(firstNameField.getAnnotation(NotEmpty.class)).isNotNull();

            var lastNameField = Person.class.getDeclaredField("lastName");
            assertThat(lastNameField.getAnnotation(NotEmpty.class)).isNotNull();
        }

        @Test
        @DisplayName("has MappedSuperclass annotation")
        void hasMappedSuperclassAnnotation() {
            assertThat(Person.class.isAnnotationPresent(MappedSuperclass.class)).isTrue();
        }

        @Test
        @DisplayName("extends BaseEntity")
        void extendsBaseEntity() {
            assertThat(person).isInstanceOf(BaseEntity.class);
        }
    }

    @Nested
    @DisplayName("Entity lifecycle inherited from BaseEntity")
    class EntityLifecycleFromBaseEntity {

        @Test
        @DisplayName("inherits id lifecycle methods")
        void inheritsIdLifecycleMethods() {
            assertThat(person.getId()).isNull();
            person.setId(100);
            assertThat(person.getId()).isEqualTo(100);
        }

        @Test
        @DisplayName("inherits isNew lifecycle method")
        void inheritsIsNewLifecycleMethod() {
            assertThat(person.isNew()).isTrue();
            person.setId(5);
            assertThat(person.isNew()).isFalse();
        }

        @Test
        @DisplayName("is new when id is null regardless of name fields")
        void isNewWhenIdIsNull() {
            assertThat(person.isNew()).isTrue();
            person.setFirstName("John");
            person.setLastName("Doe");
            assertThat(person.isNew()).isTrue();
        }

        @Test
        @DisplayName("is not new when id is set even if name fields are null")
        void isNotNewWhenIdIsSet() {
            person.setId(1);
            assertThat(person.isNew()).isFalse();
        }
    }
}