package com.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PetTypeTest {

    private PetType fixture;

    @BeforeEach
    void setUp() {
        fixture = new PetType();
    }

    @Nested
    @DisplayName("Name field inheritance from NamedEntity")
    class NameFieldInheritance {

        @Test
        @DisplayName("name is null by default")
        void nameIsNullByDefault() {
            assertThat(fixture.getName()).isNull();
        }

        @Test
        @DisplayName("setName sets the name")
        void setNameSetsName() {
            fixture.setName("Dog");
            assertThat(fixture.getName()).isEqualTo("Dog");
        }

        @Test
        @DisplayName("setName overwrites previous name")
        void setNameOverwritesPreviousName() {
            fixture.setName("Cat");
            fixture.setName("Dog");
            assertThat(fixture.getName()).isEqualTo("Dog");
        }

        @Test
        @DisplayName("setName to null resets name")
        void setNameToNull() {
            fixture.setName("Bird");
            fixture.setName(null);
            assertThat(fixture.getName()).isNull();
        }

        @Test
        @DisplayName("toString returns name when set")
        void toStringReturnsNameWhenSet() {
            fixture.setName("Hamster");
            assertThat(fixture).hasToString("Hamster");
        }

        @Test
        @DisplayName("toString returns null when name is null")
        void toStringReturnsNullWhenNameIsNull() {
            assertThat(fixture.toString()).isNull();
        }
    }

    @Nested
    @DisplayName("Entity inheritance from NamedEntity")
    class EntityInheritance {

        @Test
        @DisplayName("extends NamedEntity")
        void extendsNamedEntity() {
            assertThat(fixture).isInstanceOf(NamedEntity.class);
        }

        @Test
        @DisplayName("extends BaseEntity through NamedEntity")
        void extendsBaseEntityThroughNamedEntity() {
            assertThat(fixture).isInstanceOf(BaseEntity.class);
        }

        @Test
        @DisplayName("inherits id lifecycle methods from BaseEntity")
        void inheritsIdLifecycleMethodsFromBaseEntity() {
            assertThat(fixture.getId()).isNull();
            fixture.setId(42);
            assertThat(fixture.getId()).isEqualTo(42);
        }

        @Test
        @DisplayName("inherits isNew lifecycle method from BaseEntity")
        void inheritsIsNewLifecycleMethodFromBaseEntity() {
            assertThat(fixture.isNew()).isTrue();
            fixture.setId(1);
            assertThat(fixture.isNew()).isFalse();
        }

        @Test
        @DisplayName("inherits @NotEmpty validation from NamedEntity name field")
        void inheritsNotEmptyValidationFromNamedEntity() throws Exception {
            var nameField = NamedEntity.class.getDeclaredField("name");
            assertThat(nameField.getAnnotation(jakarta.validation.constraints.NotEmpty.class)).isNotNull();
        }

        @Test
        @DisplayName("inherits @Column annotation from NamedEntity name field")
        void inheritsColumnAnnotationFromNamedEntity() throws Exception {
            var nameField = NamedEntity.class.getDeclaredField("name");
            var columnAnnotation = nameField.getAnnotation(jakarta.persistence.Column.class);
            assertThat(columnAnnotation).isNotNull();
            assertThat(columnAnnotation.name()).isEqualTo("name");
        }
    }

    @Nested
    @DisplayName("JPA annotations verification")
    class JpaAnnotationsVerification {

        @Test
        @DisplayName("has @Entity annotation")
        void hasEntityAnnotation() {
            assertThat(PetType.class.isAnnotationPresent(Entity.class)).isTrue();
        }

        @Test
        @DisplayName("has @Table annotation with name types")
        void hasTableAnnotationWithNameTypes() {
            var tableAnnotation = PetType.class.getAnnotation(Table.class);
            assertThat(tableAnnotation).isNotNull();
            assertThat(tableAnnotation.name()).isEqualTo("types");
        }

        @Test
        @DisplayName("has MappedSuperclass through NamedEntity")
        void hasMappedSuperclassThroughNamedEntity() {
            assertThat(NamedEntity.class.isAnnotationPresent(jakarta.persistence.MappedSuperclass.class)).isTrue();
        }
    }

    @Nested
    @DisplayName("Jakarta imports verification")
    class JakartaImportsVerification {

        @Test
        @DisplayName("uses Jakarta persistence imports")
        void usesJakartaPersistenceImports() {
            assertThat(PetType.class.getAnnotation(Entity.class)).isNotNull();
            assertThat(PetType.class.getAnnotation(Table.class)).isNotNull();
        }

        @Test
        @DisplayName("inherits Jakarta validation from NamedEntity")
        void inheritsJakartaValidationFromNamedEntity() throws Exception {
            var nameField = NamedEntity.class.getDeclaredField("name");
            assertThat(nameField.getAnnotation(jakarta.validation.constraints.NotEmpty.class)).isNotNull();
        }
    }

    @Nested
    @DisplayName("Business logic")
    class BusinessLogic {

        @Test
        @DisplayName("can represent common pet types")
        void canRepresentCommonPetTypes() {
            var dog = new PetType();
            dog.setName("Dog");
            assertThat(dog.getName()).isEqualTo("Dog");

            var cat = new PetType();
            cat.setName("Cat");
            assertThat(cat.getName()).isEqualTo("Cat");

            var bird = new PetType();
            bird.setName("Bird");
            assertThat(bird.getName()).isEqualTo("Bird");
        }

        @Test
        @DisplayName("can handle exotic pet types")
        void canHandleExoticPetTypes() {
            var hamster = new PetType();
            hamster.setName("Hamster");
            assertThat(hamster.getName()).isEqualTo("Hamster");

            var snake = new PetType();
            snake.setName("Snake");
            assertThat(snake.getName()).isEqualTo("Snake");
        }

        @Test
        @DisplayName("can handle multi-word pet type names")
        void canHandleMultiWordPetTypeNames() {
            var petType1 = new PetType();
            petType1.setName("Guinea Pig");
            assertThat(petType1.getName()).isEqualTo("Guinea Pig");

            var petType2 = new PetType();
            petType2.setName("African Grey Parrot");
            assertThat(petType2.getName()).isEqualTo("African Grey Parrot");
        }

        @Test
        @DisplayName("can handle numeric pet type names")
        void canHandleNumericPetTypeNames() {
            fixture.setName("Species 12345");
            assertThat(fixture.getName()).isEqualTo("Species 12345");
        }

        @Test
        @DisplayName("is new when id is null regardless of name")
        void isNewWhenIdIsNullRegardlessOfName() {
            fixture.setName("Dog");
            assertThat(fixture.isNew()).isTrue();
        }

        @Test
        @DisplayName("is not new when id is set even if name is null")
        void isNotNewWhenIdIsSetEvenIfNameIsNull() {
            fixture.setId(1);
            assertThat(fixture.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("Use cases in Pet entity")
    class UseCasesInPetEntity {

        @Test
        @DisplayName("can be assigned to Pet type field")
        void canBeAssignedToPetTypeField() {
            fixture.setName("Dog");

            var pet = new Pet();
            pet.setName("Buddy");
            pet.setType(fixture);

            assertThat(pet.getType()).isEqualTo(fixture);
            assertThat(pet.getType().getName()).isEqualTo("Dog");
        }

        @Test
        @DisplayName("can be changed on existing Pet")
        void canBeChangedOnExistingPet() {
            var pet = new Pet();
            pet.setName("Whiskers");

            var catType = new PetType();
            catType.setName("Cat");
            var dogType = new PetType();
            dogType.setName("Dog");

            pet.setType(catType);
            assertThat(pet.getType().getName()).isEqualTo("Cat");

            pet.setType(dogType);
            assertThat(pet.getType().getName()).isEqualTo("Dog");
        }

        @Test
        @DisplayName("multiple pets can have same type")
        void multiplePetsCanHaveSameType() {
            var dogType = new PetType();
            dogType.setName("Dog");

            var pet1 = new Pet();
            pet1.setName("Buddy");
            pet1.setType(dogType);

            var pet2 = new Pet();
            pet2.setName("Rex");
            pet2.setType(dogType);

            assertThat(pet1.getType()).isEqualTo(pet2.getType());
            assertThat(pet1.getType().getName()).isEqualTo("Dog");
            assertThat(pet2.getType().getName()).isEqualTo("Dog");
        }

        @Test
        @DisplayName("type can be null on Pet")
        void typeCanBeNullOnPet() {
            var pet = new Pet();
            pet.setName("Mysterious");

            assertThat(pet.getType()).isNull();
        }
    }

    @Nested
    @DisplayName("Integration with entity hierarchy")
    class IntegrationWithEntityHierarchy {

        @Test
        @DisplayName("complete inheritance chain works correctly")
        void completeInheritanceChainWorksCorrectly() {
            fixture.setId(100);
            fixture.setName("Dog");

            // Test all levels of the inheritance chain
            assertThat(fixture).isInstanceOf(PetType.class).isInstanceOf(NamedEntity.class).isInstanceOf(BaseEntity.class);

            // Test all inherited methods work
            assertThat(fixture.getId()).isEqualTo(100);
            assertThat(fixture.getName()).isEqualTo("Dog");
            assertThat(fixture.isNew()).isFalse();
            assertThat(fixture).hasToString("Dog");
        }

        @Test
        @DisplayName("can be used in JPA relationships")
        void canBeUsedInJpaRelationships() throws Exception {
            // PetType is used in ManyToOne relationship in Pet entity
            fixture.setName("Cat");

            var pet = new Pet();
            pet.setName("Fluffy");
            pet.setType(fixture);

            // Verify the relationship is properly established
            var typeField = Pet.class.getDeclaredField("type");
            var manyToOneAnnotation = typeField.getAnnotation(jakarta.persistence.ManyToOne.class);
            var joinColumnAnnotation = typeField.getAnnotation(jakarta.persistence.JoinColumn.class);

            assertThat(manyToOneAnnotation).isNotNull();
            assertThat(joinColumnAnnotation).isNotNull();
            assertThat(joinColumnAnnotation.name()).isEqualTo("type_id");
        }
    }

    @Nested
    @DisplayName("Edge cases")
    class EdgeCases {

        @Test
        @DisplayName("can handle very long names")
        void canHandleVeryLongNames() {
            var longName = "This is a very long pet type name that might be used for scientific classification or specific breed identification";
            fixture.setName(longName);
            assertThat(fixture.getName()).isEqualTo(longName);
        }

        @Test
        @DisplayName("can handle special characters in names")
        void canHandleSpecialCharactersInNames() {
            var petType1 = new PetType();
            petType1.setName("Dog's Best Friend");
            assertThat(petType1.getName()).isEqualTo("Dog's Best Friend");

            var petType2 = new PetType();
            petType2.setName("Cat & Kitten");
            assertThat(petType2.getName()).isEqualTo("Cat & Kitten");
        }

        @Test
        @DisplayName("can handle Unicode characters")
        void canHandleUnicodeCharacters() {
            fixture.setName("宠物 (Pet)");
            assertThat(fixture.getName()).isEqualTo("宠物 (Pet)");
        }

        @Test
        @DisplayName("can have duplicate names")
        void canHaveDuplicateNames() {
            var type1 = new PetType();
            type1.setName("Dog");
            var type2 = new PetType();
            type2.setName("Dog");

            assertThat(type1.getName()).isEqualTo(type2.getName());
            // They are different instances even with same name
            assertThat(type1).isNotEqualTo(type2);
        }

        @Test
        @DisplayName("can be created with null name")
        void canBeCreatedWithNullName() {
            assertThat(fixture.getName()).isNull();
            assertThat(fixture.toString()).isNull();
        }

        @Test
        @DisplayName("can be created with empty name")
        void canBeCreatedWithEmptyName() {
            fixture.setName("");
            assertThat(fixture.getName()).isEmpty();
            assertThat(fixture.toString()).isEmpty();
        }
    }
}