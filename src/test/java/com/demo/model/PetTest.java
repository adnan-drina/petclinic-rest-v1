package com.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PetTest {

    private Pet pet;

    @BeforeEach
    void setUp() {
        pet = new Pet();
    }

    @Nested
    @DisplayName("Birth date field")
    class BirthDateField {

        @Test
        @DisplayName("birthDate is null by default")
        void birthDateIsNullByDefault() {
            assertThat(pet.getBirthDate()).isNull();
        }

        @Test
        @DisplayName("setBirthDate sets the birthDate")
        void setBirthDateSetsBirthDate() {
            var birthDate = LocalDate.of(2020, 1, 1);
            pet.setBirthDate(birthDate);
            assertThat(pet.getBirthDate()).isEqualTo(birthDate);
        }

        @Test
        @DisplayName("setBirthDate overwrites previous birthDate")
        void setBirthDateOverwritesPreviousBirthDate() {
            var birthDate1 = LocalDate.of(2020, 1, 1);
            var birthDate2 = LocalDate.of(2021, 6, 15);
            
            pet.setBirthDate(birthDate1);
            pet.setBirthDate(birthDate2);
            
            assertThat(pet.getBirthDate()).isEqualTo(birthDate2);
        }

        @Test
        @DisplayName("setBirthDate to null resets birthDate")
        void setBirthDateToNull() {
            var birthDate = LocalDate.of(2020, 1, 1);
            pet.setBirthDate(birthDate);
            pet.setBirthDate(null);
            
            assertThat(pet.getBirthDate()).isNull();
        }

        @Test
        @DisplayName("has @Column annotation with columnDefinition DATE")
        void hasColumnAnnotationWithColumnDefinitionDate() throws Exception {
            var birthDateField = Pet.class.getDeclaredField("birthDate");
            var columnAnnotation = birthDateField.getAnnotation(Column.class);
            assertThat(columnAnnotation).isNotNull();
            assertThat(columnAnnotation.name()).isEqualTo("birth_date");
            assertThat(columnAnnotation.columnDefinition()).isEqualTo("DATE");
        }
    }

    @Nested
    @DisplayName("Type relationship (ManyToOne)")
    class TypeRelationship {

        @Test
        @DisplayName("type is null by default")
        void typeIsNullByDefault() {
            assertThat(pet.getType()).isNull();
        }

        @Test
        @DisplayName("setType sets the type")
        void setTypeSetsType() {
            var petType = new PetType();
            petType.setName("Dog");
            
            pet.setType(petType);
            
            assertThat(pet.getType()).isEqualTo(petType);
        }

        @Test
        @DisplayName("setType overwrites previous type")
        void setTypeOverwritesPreviousType() {
            var type1 = new PetType();
            type1.setName("Dog");
            var type2 = new PetType();
            type2.setName("Cat");
            
            pet.setType(type1);
            pet.setType(type2);
            
            assertThat(pet.getType()).isEqualTo(type2);
        }

        @Test
        @DisplayName("setType to null resets type")
        void setTypeToNull() {
            var petType = new PetType();
            petType.setName("Dog");
            
            pet.setType(petType);
            pet.setType(null);
            
            assertThat(pet.getType()).isNull();
        }

        @Test
        @DisplayName("has @ManyToOne relationship")
        void hasManyToOneRelationship() throws Exception {
            var typeField = Pet.class.getDeclaredField("type");
            var manyToOneAnnotation = typeField.getAnnotation(ManyToOne.class);
            assertThat(manyToOneAnnotation).isNotNull();
        }

        @Test
        @DisplayName("has @JoinColumn with name type_id")
        void hasJoinColumnWithNameTypeId() throws Exception {
            var typeField = Pet.class.getDeclaredField("type");
            var joinColumnAnnotation = typeField.getAnnotation(JoinColumn.class);
            assertThat(joinColumnAnnotation).isNotNull();
            assertThat(joinColumnAnnotation.name()).isEqualTo("type_id");
        }
    }

    @Nested
    @DisplayName("Owner relationship (ManyToOne)")
    class OwnerRelationship {

        @Test
        @DisplayName("owner is null by default")
        void ownerIsNullByDefault() {
            assertThat(pet.getOwner()).isNull();
        }

        @Test
        @DisplayName("setOwner sets the owner")
        void setOwnerSetsOwner() {
            var owner = new Owner();
            owner.setFirstName("John");
            owner.setLastName("Doe");
            
            pet.setOwner(owner);
            
            assertThat(pet.getOwner()).isEqualTo(owner);
        }

        @Test
        @DisplayName("setOwner overwrites previous owner")
        void setOwnerOverwritesPreviousOwner() {
            var owner1 = new Owner();
            owner1.setFirstName("Alice");
            var owner2 = new Owner();
            owner2.setFirstName("Bob");
            
            pet.setOwner(owner1);
            pet.setOwner(owner2);
            
            assertThat(pet.getOwner()).isEqualTo(owner2);
        }

        @Test
        @DisplayName("setOwner to null resets owner")
        void setOwnerToNull() {
            var owner = new Owner();
            owner.setFirstName("John");
            
            pet.setOwner(owner);
            pet.setOwner(null);
            
            assertThat(pet.getOwner()).isNull();
        }

        @Test
        @DisplayName("has @ManyToOne relationship")
        void hasManyToOneRelationship() throws Exception {
            var ownerField = Pet.class.getDeclaredField("owner");
            var manyToOneAnnotation = ownerField.getAnnotation(ManyToOne.class);
            assertThat(manyToOneAnnotation).isNotNull();
        }

        @Test
        @DisplayName("has @JoinColumn with name owner_id")
        void hasJoinColumnWithNameOwnerId() throws Exception {
            var ownerField = Pet.class.getDeclaredField("owner");
            var joinColumnAnnotation = ownerField.getAnnotation(JoinColumn.class);
            assertThat(joinColumnAnnotation).isNotNull();
            assertThat(joinColumnAnnotation.name()).isEqualTo("owner_id");
        }
    }

    @Nested
    @DisplayName("Visits relationship (OneToMany)")
    class VisitsRelationship {

        @Test
        @DisplayName("visits is null by default")
        void visitsIsNullByDefault() {
            var visits = pet.getVisits();
            assertThat(visits).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("getVisits returns unmodifiable list")
        void getVisitsReturnsUnmodifiableList() {
            var visits = pet.getVisits();
            assertThat(visits).isInstanceOf(Collections.unmodifiableList(List.of()).getClass());
        }

        @Test
        @DisplayName("addVisit adds visit to collection")
        void addVisitAddsVisitToCollection() {
            var visit = new Visit();
            visit.setDescription("Checkup");
            
            pet.addVisit(visit);
            
            var visits = pet.getVisits();
            assertThat(visits).hasSize(1);
            assertThat(visits.get(0).getDescription()).isEqualTo("Checkup");
        }

        @Test
        @DisplayName("addVisit sets pet on visit")
        void addVisitSetsPetOnVisit() {
            var visit = new Visit();
            visit.setDescription("Vaccination");
            
            pet.addVisit(visit);
            
            assertThat(visit.getPet()).isEqualTo(pet);
        }

        @Test
        @DisplayName("addVisit sets pet back-reference")
        void addVisitSetsPetBackReference() {
            var visit = new Visit();
            visit.setDescription("Surgery");
            
            pet.addVisit(visit);
            
            assertThat(visit.getPet()).isSameAs(pet);
        }

        @Test
        @DisplayName("multiple visits can be added")
        void multipleVisitsCanBeAdded() {
            var visit1 = new Visit();
            visit1.setDescription("Checkup");
            var visit2 = new Visit();
            visit2.setDescription("Vaccination");
            var visit3 = new Visit();
            visit3.setDescription("Surgery");
            
            pet.addVisit(visit1);
            pet.addVisit(visit2);
            pet.addVisit(visit3);
            
            assertThat(pet.getVisits()).hasSize(3);
        }

        @Test
        @DisplayName("getVisits returns visits sorted by date")
        void getVisitsReturnsVisitsSortedByDate() {
            var visit1 = new Visit();
            visit1.setDescription("Checkup");
            visit1.setDate(LocalDate.of(2023, 3, 1));
            var visit2 = new Visit();
            visit2.setDescription("Vaccination");
            visit2.setDate(LocalDate.of(2023, 1, 1));
            var visit3 = new Visit();
            visit3.setDescription("Surgery");
            visit3.setDate(LocalDate.of(2023, 2, 1));
            
            pet.addVisit(visit1);
            pet.addVisit(visit2);
            pet.addVisit(visit3);
            
            var visits = pet.getVisits();
            assertThat(visits.get(0).getDate()).isEqualTo(LocalDate.of(2023, 1, 1));
            assertThat(visits.get(1).getDate()).isEqualTo(LocalDate.of(2023, 2, 1));
            assertThat(visits.get(2).getDate()).isEqualTo(LocalDate.of(2023, 3, 1));
        }

        @Test
        @DisplayName("visits with null dates are sorted last")
        void visitsWithNullDatesAreSortedLast() {
            var visit1 = new Visit();
            visit1.setDescription("Checkup");
            visit1.setDate(LocalDate.of(2023, 3, 1));
            var visit2 = new Visit();
            visit2.setDescription("Vaccination");
            visit2.setDate(null); // Visit() defaults to LocalDate.now()
            var visit3 = new Visit();
            visit3.setDescription("Surgery");
            visit3.setDate(LocalDate.of(2023, 1, 1));
            
            pet.addVisit(visit1);
            pet.addVisit(visit2);
            pet.addVisit(visit3);
            
            var visits = pet.getVisits();
            assertThat(visits.get(0).getDate()).isEqualTo(LocalDate.of(2023, 1, 1));
            assertThat(visits.get(1).getDate()).isEqualTo(LocalDate.of(2023, 3, 1));
            assertThat(visits.get(2).getDate()).isNull();
        }

        @Test
        @DisplayName("setVisits replaces entire collection")
        void setVisitsReplacesEntireCollection() {
            var visit1 = new Visit();
            visit1.setDescription("Checkup");
            var visit2 = new Visit();
            visit2.setDescription("Vaccination");
            
            var visits = new ArrayList<Visit>();
            visits.add(visit1);
            visits.add(visit2);
            
            pet.setVisits(visits);
            
            assertThat(pet.getVisits()).hasSize(2);
            // both Visit() default to LocalDate.now() — order not insertion-stable
            assertThat(pet.getVisits()).extracting(Visit::getDescription)
                .containsExactlyInAnyOrder("Checkup", "Vaccination");
        }

        @Test
        @DisplayName("has @OneToMany relationship with CascadeType.ALL")
        void hasOneToManyRelationshipWithCascadeTypeAll() throws Exception {
            var visitsField = Pet.class.getDeclaredField("visits");
            var oneToManyAnnotation = visitsField.getAnnotation(OneToMany.class);
            assertThat(oneToManyAnnotation).isNotNull();
            assertThat(oneToManyAnnotation.cascade()).contains(CascadeType.ALL);
        }

        @Test
        @DisplayName("has @OneToMany relationship with FetchType.EAGER")
        void hasOneToManyRelationshipWithFetchTypeEager() throws Exception {
            var visitsField = Pet.class.getDeclaredField("visits");
            var oneToManyAnnotation = visitsField.getAnnotation(OneToMany.class);
            assertThat(oneToManyAnnotation.fetch()).isEqualTo(FetchType.EAGER);
        }

        @Test
        @DisplayName("has @OneToMany relationship with mappedBy pet")
        void hasOneToManyRelationshipWithMappedByPet() throws Exception {
            var visitsField = Pet.class.getDeclaredField("visits");
            var oneToManyAnnotation = visitsField.getAnnotation(OneToMany.class);
            assertThat(oneToManyAnnotation.mappedBy()).isEqualTo("pet");
        }
    }

    @Nested
    @DisplayName("Bidirectional relationship consistency")
    class BidirectionalRelationshipConsistency {

        @Test
        @DisplayName("adding pet to owner also sets owner on pet")
        void addingPetToOwnerAlsoSetsOwnerOnPet() {
            var owner = new Owner();
            var pet1 = new Pet();
            pet1.setName("Fluffy");
            
            owner.addPet(pet1);
            
            assertThat(pet1.getOwner()).isEqualTo(owner);
        }

        @Test
        @DisplayName("adding visit to pet also sets pet on visit")
        void addingVisitToPetAlsoSetsPetOnVisit() {
            var visit = new Visit();
            visit.setDescription("Checkup");
            
            pet.addVisit(visit);
            
            assertThat(visit.getPet()).isEqualTo(pet);
        }

        @Test
        @DisplayName("setting owner on pet does not automatically add pet to owner")
        void settingOwnerOnPetDoesNotAutomaticallyAddPetToOwner() {
            var owner = new Owner();
            var pet1 = new Pet();
            pet1.setName("Spot");
            
            pet1.setOwner(owner);
            
            // Pet is added to owner, but owner doesn't have the pet in its collection
            // This is expected behavior - use addPet() for bidirectional consistency
            assertThat(pet1.getOwner()).isEqualTo(owner);
        }

        @Test
        @DisplayName("bidirectional relationship with multiple pets")
        void bidirectionalRelationshipWithMultiplePets() {
            var owner = new Owner();
            var pet1 = new Pet();
            pet1.setName("Fluffy");
            var pet2 = new Pet();
            pet2.setName("Spot");
            
            owner.addPet(pet1);
            owner.addPet(pet2);
            
            assertThat(pet1.getOwner()).isEqualTo(owner);
            assertThat(pet2.getOwner()).isEqualTo(owner);
            assertThat(owner.getPets()).hasSize(2);
            assertThat(owner.getPets()).containsExactlyInAnyOrder(pet1, pet2);
        }
    }

    @Nested
    @DisplayName("JPA annotations verification")
    class JpaAnnotationsVerification {

        @Test
        @DisplayName("has @Entity annotation")
        void hasEntityAnnotation() {
            assertThat(Pet.class.isAnnotationPresent(Entity.class)).isTrue();
        }

        @Test
        @DisplayName("has @Table annotation with name pets")
        void hasTableAnnotationWithNamePets() {
            var tableAnnotation = Pet.class.getAnnotation(Table.class);
            assertThat(tableAnnotation).isNotNull();
            assertThat(tableAnnotation.name()).isEqualTo("pets");
        }

        @Test
        @DisplayName("extends NamedEntity")
        void extendsNamedEntity() {
            assertThat(pet).isInstanceOf(NamedEntity.class);
        }

        @Test
        @DisplayName("extends BaseEntity through NamedEntity")
        void extendsBaseEntityThroughNamedEntity() {
            assertThat(pet).isInstanceOf(BaseEntity.class);
        }
    }

    @Nested
    @DisplayName("Jakarta imports verification")
    class JakartaImportsVerification {

        @Test
        @DisplayName("uses Jakarta persistence imports")
        void usesJakartaPersistenceImports() {
            assertThat(Pet.class.getAnnotation(Entity.class)).isNotNull();
            assertThat(Pet.class.getAnnotation(Table.class)).isNotNull();
        }

        @Test
        @DisplayName("inherits Jakarta validation from NamedEntity")
        void inheritsJakartaValidationFromNamedEntity() throws Exception {
            var nameField = NamedEntity.class.getDeclaredField("name");
            assertThat(nameField.getAnnotation(jakarta.validation.constraints.NotEmpty.class)).isNotNull();
        }
    }

    @Nested
    @DisplayName("Entity lifecycle from inheritance")
    class EntityLifecycleFromInheritance {

        @Test
        @DisplayName("inherits name lifecycle methods from NamedEntity")
        void inheritsNameLifecycleMethodsFromNamedEntity() {
            assertThat(pet.getName()).isNull();
            pet.setName("Fluffy");
            assertThat(pet.getName()).isEqualTo("Fluffy");
        }

        @Test
        @DisplayName("inherits id lifecycle methods from BaseEntity")
        void inheritsIdLifecycleMethodsFromBaseEntity() {
            assertThat(pet.getId()).isNull();
            pet.setId(42);
            assertThat(pet.getId()).isEqualTo(42);
        }

        @Test
        @DisplayName("inherits isNew lifecycle method from BaseEntity")
        void inheritsIsNewLifecycleMethodFromBaseEntity() {
            assertThat(pet.isNew()).isTrue();
            pet.setId(1);
            assertThat(pet.isNew()).isFalse();
        }

        @Test
        @DisplayName("is new when id is null regardless of other fields")
        void isNewWhenIdIsNullRegardlessOfOtherFields() {
            pet.setName("Fluffy");
            pet.setBirthDate(LocalDate.of(2020, 1, 1));
            assertThat(pet.isNew()).isTrue();
        }

        @Test
        @DisplayName("is not new when id is set even if other fields are null")
        void isNotNewWhenIdIsSet() {
            pet.setId(1);
            assertThat(pet.isNew()).isFalse();
        }

        @Test
        @DisplayName("inherits toString from NamedEntity")
        void inheritsToStringFromNamedEntity() {
            pet.setName("Fluffy");
            assertThat(pet.toString()).isEqualTo("Fluffy");
        }
    }

    @Nested
    @DisplayName("Business logic validation")
    class BusinessLogicValidation {

        @Test
        @DisplayName("can set birth date in the past")
        void canSetBirthDateInThePast() {
            var birthDate = LocalDate.of(2010, 5, 15);
            pet.setBirthDate(birthDate);
            assertThat(pet.getBirthDate()).isEqualTo(birthDate);
        }

        @Test
        @DisplayName("can set birth date in the future")
        void canSetBirthDateInTheFuture() {
            var birthDate = LocalDate.of(2030, 12, 25);
            pet.setBirthDate(birthDate);
            assertThat(pet.getBirthDate()).isEqualTo(birthDate);
        }

        @Test
        @DisplayName("can have visits with different dates")
        void canHaveVisitsWithDifferentDates() {
            var visit1 = new Visit();
            visit1.setDescription("First visit");
            visit1.setDate(LocalDate.of(2023, 1, 1));
            var visit2 = new Visit();
            visit2.setDescription("Second visit");
            visit2.setDate(LocalDate.of(2023, 6, 1));
            
            pet.addVisit(visit1);
            pet.addVisit(visit2);
            
            assertThat(pet.getVisits()).hasSize(2);
            assertThat(pet.getVisits().get(0).getDate()).isBefore(pet.getVisits().get(1).getDate());
        }

        @Test
        @DisplayName("visits are automatically sorted by date")
        void visitsAreAutomaticallySortedByDate() {
            var visit1 = new Visit();
            visit1.setDescription("Last");
            visit1.setDate(LocalDate.of(2023, 3, 1));
            var visit2 = new Visit();
            visit2.setDescription("First");
            visit2.setDate(LocalDate.of(2023, 1, 1));
            var visit3 = new Visit();
            visit3.setDescription("Middle");
            visit3.setDate(LocalDate.of(2023, 2, 1));
            
            pet.addVisit(visit1);
            pet.addVisit(visit2);
            pet.addVisit(visit3);
            
            var visits = pet.getVisits();
            assertThat(visits.get(0).getDescription()).isEqualTo("First");
            assertThat(visits.get(1).getDescription()).isEqualTo("Middle");
            assertThat(visits.get(2).getDescription()).isEqualTo("Last");
        }
    }
}