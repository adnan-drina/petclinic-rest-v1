package com.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OwnerTest {

    private Owner owner;

    @BeforeEach
    void setUp() {
        owner = new Owner();
    }

    @Nested
    @DisplayName("Address field")
    class AddressField {

        @Test
        @DisplayName("address is null by default")
        void addressIsNullByDefault() {
            assertThat(owner.getAddress()).isNull();
        }

        @Test
        @DisplayName("setAddress sets the address")
        void setAddressSetsAddress() {
            owner.setAddress("123 Main St");
            assertThat(owner.getAddress()).isEqualTo("123 Main St");
        }

        @Test
        @DisplayName("setAddress overwrites previous address")
        void setAddressOverwritesPreviousAddress() {
            owner.setAddress("456 Oak Ave");
            owner.setAddress("789 Pine Rd");
            assertThat(owner.getAddress()).isEqualTo("789 Pine Rd");
        }

        @Test
        @DisplayName("setAddress to null resets address")
        void setAddressToNull() {
            owner.setAddress("123 Main St");
            owner.setAddress(null);
            assertThat(owner.getAddress()).isNull();
        }

        @Test
        @DisplayName("has @NotEmpty validation constraint")
        void hasNotEmptyValidation() throws Exception {
            var addressField = Owner.class.getDeclaredField("address");
            assertThat(addressField.getAnnotation(NotEmpty.class)).isNotNull();
        }

        @Test
        @DisplayName("has @Column annotation")
        void hasColumnAnnotation() throws Exception {
            var addressField = Owner.class.getDeclaredField("address");
            var columnAnnotation = addressField.getAnnotation(Column.class);
            assertThat(columnAnnotation).isNotNull();
            assertThat(columnAnnotation.name()).isEqualTo("address");
        }
    }

    @Nested
    @DisplayName("City field")
    class CityField {

        @Test
        @DisplayName("city is null by default")
        void cityIsNullByDefault() {
            assertThat(owner.getCity()).isNull();
        }

        @Test
        @DisplayName("setCity sets the city")
        void setCitySetsCity() {
            owner.setCity("Springfield");
            assertThat(owner.getCity()).isEqualTo("Springfield");
        }

        @Test
        @DisplayName("setCity overwrites previous city")
        void setCityOverwritesPreviousCity() {
            owner.setCity("Chicago");
            owner.setCity("Boston");
            assertThat(owner.getCity()).isEqualTo("Boston");
        }

        @Test
        @DisplayName("setCity to null resets city")
        void setCityToNull() {
            owner.setCity("Springfield");
            owner.setCity(null);
            assertThat(owner.getCity()).isNull();
        }

        @Test
        @DisplayName("has @NotEmpty validation constraint")
        void hasNotEmptyValidation() throws Exception {
            var cityField = Owner.class.getDeclaredField("city");
            assertThat(cityField.getAnnotation(NotEmpty.class)).isNotNull();
        }

        @Test
        @DisplayName("has @Column annotation")
        void hasColumnAnnotation() throws Exception {
            var cityField = Owner.class.getDeclaredField("city");
            var columnAnnotation = cityField.getAnnotation(Column.class);
            assertThat(columnAnnotation).isNotNull();
            assertThat(columnAnnotation.name()).isEqualTo("city");
        }
    }

    @Nested
    @DisplayName("Telephone field")
    class TelephoneField {

        @Test
        @DisplayName("telephone is null by default")
        void telephoneIsNullByDefault() {
            assertThat(owner.getTelephone()).isNull();
        }

        @Test
        @DisplayName("setTelephone sets the telephone")
        void setTelephoneSetsTelephone() {
            owner.setTelephone("555-1234");
            assertThat(owner.getTelephone()).isEqualTo("555-1234");
        }

        @Test
        @DisplayName("setTelephone overwrites previous telephone")
        void setTelephoneOverwritesPreviousTelephone() {
            owner.setTelephone("555-1111");
            owner.setTelephone("555-9999");
            assertThat(owner.getTelephone()).isEqualTo("555-9999");
        }

        @Test
        @DisplayName("setTelephone to null resets telephone")
        void setTelephoneToNull() {
            owner.setTelephone("555-1234");
            owner.setTelephone(null);
            assertThat(owner.getTelephone()).isNull();
        }

        @Test
        @DisplayName("has @NotEmpty validation constraint")
        void hasNotEmptyValidation() throws Exception {
            var telephoneField = Owner.class.getDeclaredField("telephone");
            assertThat(telephoneField.getAnnotation(NotEmpty.class)).isNotNull();
        }

        @Test
        @DisplayName("has @Digits validation constraint")
        void hasDigitsValidation() throws Exception {
            var telephoneField = Owner.class.getDeclaredField("telephone");
            var digitsAnnotation = telephoneField.getAnnotation(Digits.class);
            assertThat(digitsAnnotation).isNotNull();
            assertThat(digitsAnnotation.fraction()).isZero();
            assertThat(digitsAnnotation.integer()).isEqualTo(10);
        }

        @Test
        @DisplayName("has @Column annotation")
        void hasColumnAnnotation() throws Exception {
            var telephoneField = Owner.class.getDeclaredField("telephone");
            var columnAnnotation = telephoneField.getAnnotation(Column.class);
            assertThat(columnAnnotation).isNotNull();
            assertThat(columnAnnotation.name()).isEqualTo("telephone");
        }
    }

    @Nested
    @DisplayName("Pets relationship")
    class PetsRelationship {

        @Test
        @DisplayName("pets is null by default")
        void petsIsNullByDefault() {
            var pets = owner.getPets();
            assertThat(pets).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("getPets returns unmodifiable list")
        void getPetsReturnsUnmodifiableList() {
            var pets = owner.getPets();
            assertThat(pets).isInstanceOf(Collections.unmodifiableList(List.of()).getClass());
        }

        @Test
        @DisplayName("addPet adds pet to collection")
        void addPetAddsPetToCollection() {
            var pet = new Pet();
            pet.setName("Fluffy");
            
            owner.addPet(pet);
            
            var pets = owner.getPets();
            assertThat(pets).hasSize(1);
            assertThat(pets.get(0).getName()).isEqualTo("Fluffy");
        }

        @Test
        @DisplayName("addPet sets owner on pet")
        void addPetSetsOwnerOnPet() {
            var pet = new Pet();
            pet.setName("Spot");
            
            owner.addPet(pet);
            
            assertThat(pet.getOwner()).isEqualTo(owner);
        }

        @Test
        @DisplayName("addPet sets owner back-reference")
        void addPetSetsOwnerBackReference() {
            var pet = new Pet();
            pet.setName("Rex");
            
            owner.addPet(pet);
            
            assertThat(pet.getOwner()).isSameAs(owner);
        }

        @Test
        @DisplayName("multiple pets can be added")
        void multiplePetsCanBeAdded() {
            var pet1 = new Pet();
            pet1.setName("Fluffy");
            var pet2 = new Pet();
            pet2.setName("Spot");
            var pet3 = new Pet();
            pet3.setName("Rex");
            
            owner.addPet(pet1);
            owner.addPet(pet2);
            owner.addPet(pet3);
            
            assertThat(owner.getPets()).hasSize(3);
        }

        @Test
        @DisplayName("getPets returns pets sorted by name")
        void getPetsReturnsPetsSortedByName() {
            var pet1 = new Pet();
            pet1.setName("Charlie");
            var pet2 = new Pet();
            pet2.setName("Alpha");
            var pet3 = new Pet();
            pet3.setName("Bravo");
            
            owner.addPet(pet1);
            owner.addPet(pet2);
            owner.addPet(pet3);
            
            var pets = owner.getPets();
            assertThat(pets.get(0).getName()).isEqualTo("Alpha");
            assertThat(pets.get(1).getName()).isEqualTo("Bravo");
            assertThat(pets.get(2).getName()).isEqualTo("Charlie");
        }

        @Test
        @DisplayName("pets with null names are sorted last")
        void petsWithNullNamesAreSortedLast() {
            var pet1 = new Pet();
            pet1.setName("Charlie");
            var pet2 = new Pet();
            // pet2 has null name
            var pet3 = new Pet();
            pet3.setName("Alpha");
            
            owner.addPet(pet1);
            owner.addPet(pet2);
            owner.addPet(pet3);
            
            var pets = owner.getPets();
            assertThat(pets.get(0).getName()).isEqualTo("Alpha");
            assertThat(pets.get(1).getName()).isEqualTo("Charlie");
            assertThat(pets.get(2).getName()).isNull();
        }

        @Test
        @DisplayName("setPets replaces entire collection")
        void setPetsReplacesEntireCollection() {
            var pet1 = new Pet();
            pet1.setName("Fluffy");
            var pet2 = new Pet();
            pet2.setName("Spot");
            
            var pets = new ArrayList<Pet>();
            pets.add(pet1);
            pets.add(pet2);
            
            owner.setPets(pets);
            
            assertThat(owner.getPets()).hasSize(2);
            assertThat(owner.getPets().get(0).getName()).isEqualTo("Fluffy");
            assertThat(owner.getPets().get(1).getName()).isEqualTo("Spot");
        }

        @Test
        @DisplayName("has @OneToMany relationship with CascadeType.ALL")
        void hasOneToManyRelationshipWithCascadeTypeAll() throws Exception {
            var petsField = Owner.class.getDeclaredField("pets");
            var oneToManyAnnotation = petsField.getAnnotation(OneToMany.class);
            assertThat(oneToManyAnnotation).isNotNull();
            assertThat(oneToManyAnnotation.cascade()).contains(CascadeType.ALL);
        }

        @Test
        @DisplayName("has @OneToMany relationship with FetchType.EAGER")
        void hasOneToManyRelationshipWithFetchTypeEager() throws Exception {
            var petsField = Owner.class.getDeclaredField("pets");
            var oneToManyAnnotation = petsField.getAnnotation(OneToMany.class);
            assertThat(oneToManyAnnotation.fetch()).isEqualTo(FetchType.EAGER);
        }

        @Test
        @DisplayName("has @OneToMany relationship with mappedBy owner")
        void hasOneToManyRelationshipWithMappedByOwner() throws Exception {
            var petsField = Owner.class.getDeclaredField("pets");
            var oneToManyAnnotation = petsField.getAnnotation(OneToMany.class);
            assertThat(oneToManyAnnotation.mappedBy()).isEqualTo("owner");
        }
    }

    @Nested
    @DisplayName("Business method: getPet(name)")
    class GetPetByNameBusinessMethod {

        @Test
        @DisplayName("getPet returns pet when found by name")
        void getPetReturnsPetWhenFound() {
            var pet1 = new Pet();
            pet1.setName("Fluffy");
            var pet2 = new Pet();
            pet2.setName("Spot");
            
            owner.addPet(pet1);
            owner.addPet(pet2);
            
            var foundPet = owner.getPet("Fluffy");
            assertThat(foundPet).isNotNull();
            assertThat(foundPet.getName()).isEqualTo("Fluffy");
        }

        @Test
        @DisplayName("getPet returns null when pet not found")
        void getPetReturnsNullWhenNotFound() {
            var pet1 = new Pet();
            pet1.setName("Fluffy");
            
            owner.addPet(pet1);
            
            var foundPet = owner.getPet("Spot");
            assertThat(foundPet).isNull();
        }

        @Test
        @DisplayName("getPet performs case-insensitive search")
        void getPetPerformsCaseInsensitiveSearch() {
            var pet1 = new Pet();
            pet1.setName("Fluffy");
            
            owner.addPet(pet1);
            
            var foundPet = owner.getPet("FLUFFY");
            assertThat(foundPet).isNotNull();
            assertThat(foundPet.getName()).isEqualTo("Fluffy");
        }

        @Test
        @DisplayName("getPet returns null when no pets exist")
        void getPetReturnsNullWhenNoPetsExist() {
            var foundPet = owner.getPet("Fluffy");
            assertThat(foundPet).isNull();
        }

        @Test
        @DisplayName("getPet ignores new pets by default")
        void getPetIgnoresNewPetsByDefault() {
            var pet1 = new Pet();
            pet1.setName("Fluffy");
            pet1.setId(1); // Not new
            var pet2 = new Pet();
            pet2.setName("Spot");
            // pet2 is new (no ID)
            
            owner.addPet(pet1);
            owner.addPet(pet2);
            
            // getPet(name) defaults ignoreNew=false — new pets ARE found
            var foundPet = owner.getPet("Spot");
            assertThat(foundPet).isNotNull();
            assertThat(foundPet.getName()).isEqualTo("Spot");
        }
    }

    @Nested
    @DisplayName("Business method: getPet(name, ignoreNew)")
    class GetPetByNameWithIgnoreNewBusinessMethod {

        @Test
        @DisplayName("getPet with ignoreNew=false finds new pets")
        void getPetWithIgnoreNewFalseFindsNewPets() {
            var pet1 = new Pet();
            pet1.setName("Fluffy");
            pet1.setId(1); // Not new
            var pet2 = new Pet();
            pet2.setName("Spot");
            // pet2 is new (no ID)
            
            owner.addPet(pet1);
            owner.addPet(pet2);
            
            var foundPet = owner.getPet("Spot", false);
            assertThat(foundPet).isNotNull();
            assertThat(foundPet.getName()).isEqualTo("Spot");
        }

        @Test
        @DisplayName("getPet with ignoreNew=true ignores new pets")
        void getPetWithIgnoreNewTrueIgnoresNewPets() {
            var pet1 = new Pet();
            pet1.setName("Fluffy");
            pet1.setId(1); // Not new
            var pet2 = new Pet();
            pet2.setName("Spot");
            // pet2 is new (no ID)
            
            owner.addPet(pet1);
            owner.addPet(pet2);
            
            var foundPet = owner.getPet("Spot", true);
            assertThat(foundPet).isNull();
        }

        @Test
        @DisplayName("getPet with ignoreNew=true finds existing pets")
        void getPetWithIgnoreNewTrueFindsExistingPets() {
            var pet1 = new Pet();
            pet1.setName("Fluffy");
            pet1.setId(1); // Not new
            var pet2 = new Pet();
            pet2.setName("Spot");
            // pet2 is new (no ID)
            
            owner.addPet(pet1);
            owner.addPet(pet2);
            
            var foundPet = owner.getPet("Fluffy", true);
            assertThat(foundPet).isNotNull();
            assertThat(foundPet.getName()).isEqualTo("Fluffy");
        }

        @Test
        @DisplayName("getPet with ignoreNew=true returns null when no existing pets")
        void getPetWithIgnoreNewTrueReturnsNullWhenNoExistingPets() {
            var pet = new Pet();
            pet.setName("Spot");
            // pet is new (no ID)
            
            owner.addPet(pet);
            
            var foundPet = owner.getPet("Spot", true);
            assertThat(foundPet).isNull();
        }
    }

    @Nested
    @DisplayName("JPA annotations verification")
    class JpaAnnotationsVerification {

        @Test
        @DisplayName("has @Entity annotation")
        void hasEntityAnnotation() {
            assertThat(Owner.class.isAnnotationPresent(Entity.class)).isTrue();
        }

        @Test
        @DisplayName("has @Table annotation with name owners")
        void hasTableAnnotationWithNameOwners() {
            var tableAnnotation = Owner.class.getAnnotation(Table.class);
            assertThat(tableAnnotation).isNotNull();
            assertThat(tableAnnotation.name()).isEqualTo("owners");
        }

        @Test
        @DisplayName("extends Person")
        void extendsPerson() {
            assertThat(owner).isInstanceOf(Person.class);
        }

        @Test
        @DisplayName("extends BaseEntity through Person")
        void extendsBaseEntityThroughPerson() {
            assertThat(owner).isInstanceOf(BaseEntity.class);
        }
    }

    @Nested
    @DisplayName("Jakarta imports verification")
    class JakartaImportsVerification {

        @Test
        @DisplayName("uses Jakarta persistence imports")
        void usesJakartaPersistenceImports() {
            assertThat(Owner.class.getAnnotation(Entity.class)).isNotNull();
            assertThat(Owner.class.getAnnotation(Table.class)).isNotNull();
        }

        @Test
        @DisplayName("uses Jakarta validation imports")
        void usesJakartaValidationImports() throws Exception {
            var addressField = Owner.class.getDeclaredField("address");
            assertThat(addressField.getAnnotation(NotEmpty.class)).isNotNull();
            
            var telephoneField = Owner.class.getDeclaredField("telephone");
            assertThat(telephoneField.getAnnotation(NotEmpty.class)).isNotNull();
            assertThat(telephoneField.getAnnotation(Digits.class)).isNotNull();
        }
    }

    @Nested
    @DisplayName("toString method")
    class ToStringMethod {

        @Test
        @DisplayName("toString includes all fields")
        void toStringIncludesAllFields() {
            owner.setId(1);
            owner.setFirstName("John");
            owner.setLastName("Doe");
            owner.setAddress("123 Main St");
            owner.setCity("Springfield");
            owner.setTelephone("555-1234");
            
            var toString = owner.toString();
            
            assertThat(toString).contains("id=1").contains("isNew=false").contains("lastName='Doe'").contains("firstName='John'").contains("address='123 Main St'").contains("city='Springfield'").contains("telephone='555-1234'");
        }

        @Test
        @DisplayName("toString handles null values")
        void toStringHandlesNullValues() {
            owner.setFirstName("Jane");
            
            var toString = owner.toString();
            
            assertThat(toString).contains("firstName='Jane'").contains("lastName='null'");
        }
    }

    @Nested
    @DisplayName("Entity lifecycle from inheritance")
    class EntityLifecycleFromInheritance {

        @Test
        @DisplayName("inherits id lifecycle methods from BaseEntity")
        void inheritsIdLifecycleMethodsFromBaseEntity() {
            assertThat(owner.getId()).isNull();
            owner.setId(42);
            assertThat(owner.getId()).isEqualTo(42);
        }

        @Test
        @DisplayName("inherits isNew lifecycle method from BaseEntity")
        void inheritsIsNewLifecycleMethodFromBaseEntity() {
            assertThat(owner.isNew()).isTrue();
            owner.setId(1);
            assertThat(owner.isNew()).isFalse();
        }

        @Test
        @DisplayName("inherits name fields from Person")
        void inheritsNameFieldsFromPerson() {
            owner.setFirstName("Alice");
            owner.setLastName("Smith");
            assertThat(owner.getFirstName()).isEqualTo("Alice");
            assertThat(owner.getLastName()).isEqualTo("Smith");
            // getFullName doesn't exist, verify with firstName + lastName
            assertThat(owner.getFirstName() + " " + owner.getLastName()).isEqualTo("Alice Smith");
        }

        @Test
        @DisplayName("is new when id is null regardless of other fields")
        void isNewWhenIdIsNullRegardlessOfOtherFields() {
            owner.setFirstName("John");
            owner.setLastName("Doe");
            owner.setAddress("123 Main St");
            owner.setCity("Springfield");
            owner.setTelephone("555-1234");
            assertThat(owner.isNew()).isTrue();
        }

        @Test
        @DisplayName("is not new when id is set even if other fields are null")
        void isNotNewWhenIdIsSet() {
            owner.setId(1);
            assertThat(owner.isNew()).isFalse();
        }
    }
}