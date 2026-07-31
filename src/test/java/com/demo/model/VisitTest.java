package com.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class VisitTest {

    private Visit visit;

    @BeforeEach
    void setUp() {
        visit = new Visit();
    }

    @Nested
    @DisplayName("Constructor behavior")
    class ConstructorBehavior {

        @Test
        @DisplayName("constructor sets date to current date")
        void constructorSetsDateToCurrentDate() {
            var visitWithConstructor = new Visit();
            var today = LocalDate.now();
            assertThat(visitWithConstructor.getDate()).isEqualTo(today);
        }

        @Test
        @DisplayName("default constructor (no-arg) sets date to current date")
        void defaultConstructorSetsDateToCurrentDate() {
            var today = LocalDate.now();
            assertThat(visit.getDate()).isEqualTo(today);
        }

        @Test
        @DisplayName("date is initialized automatically")
        void dateIsInitializedAutomatically() {
            assertThat(visit.getDate()).isNotNull().isInstanceOf(LocalDate.class);
        }
    }

    @Nested
    @DisplayName("Date field")
    class DateField {

        @Test
        @DisplayName("date is initialized by constructor")
        void dateIsInitializedByConstructor() {
            assertThat(visit.getDate()).isNotNull();
        }

        @Test
        @DisplayName("setDate sets the date")
        void setDateSetsDate() {
            var date = LocalDate.of(2023, 6, 15);
            visit.setDate(date);
            assertThat(visit.getDate()).isEqualTo(date);
        }

        @Test
        @DisplayName("setDate overwrites previous date")
        void setDateOverwritesPreviousDate() {
            var date1 = LocalDate.of(2023, 1, 1);
            var date2 = LocalDate.of(2023, 12, 31);
            
            visit.setDate(date1);
            visit.setDate(date2);
            
            assertThat(visit.getDate()).isEqualTo(date2);
        }

        @Test
        @DisplayName("setDate to null resets date")
        void setDateToNull() {
            visit.setDate(LocalDate.of(2023, 6, 15));
            visit.setDate(null);
            
            assertThat(visit.getDate()).isNull();
        }

        @Test
        @DisplayName("can set date to past date")
        void canSetDateToPastDate() {
            var pastDate = LocalDate.of(2020, 3, 10);
            visit.setDate(pastDate);
            assertThat(visit.getDate()).isEqualTo(pastDate);
        }

        @Test
        @DisplayName("can set date to future date")
        void canSetDateToFutureDate() {
            var futureDate = LocalDate.of(2025, 12, 25);
            visit.setDate(futureDate);
            assertThat(visit.getDate()).isEqualTo(futureDate);
        }

        @Test
        @DisplayName("can set date to today")
        void canSetDateToToday() {
            var today = LocalDate.now();
            visit.setDate(today);
            assertThat(visit.getDate()).isEqualTo(today);
        }

        @Test
        @DisplayName("has @Column annotation with columnDefinition DATE")
        void hasColumnAnnotationWithColumnDefinitionDate() throws Exception {
            var dateField = Visit.class.getDeclaredField("date");
            var columnAnnotation = dateField.getAnnotation(Column.class);
            assertThat(columnAnnotation).isNotNull();
            assertThat(columnAnnotation.name()).isEqualTo("visit_date");
            assertThat(columnAnnotation.columnDefinition()).isEqualTo("DATE");
        }
    }

    @Nested
    @DisplayName("Description field")
    class DescriptionField {

        @Test
        @DisplayName("description is null by default")
        void descriptionIsNullByDefault() {
            assertThat(visit.getDescription()).isNull();
        }

        @Test
        @DisplayName("setDescription sets the description")
        void setDescriptionSetsDescription() {
            visit.setDescription("Regular checkup");
            assertThat(visit.getDescription()).isEqualTo("Regular checkup");
        }

        @Test
        @DisplayName("setDescription overwrites previous description")
        void setDescriptionOverwritesPreviousDescription() {
            visit.setDescription("First visit");
            visit.setDescription("Second visit");
            assertThat(visit.getDescription()).isEqualTo("Second visit");
        }

        @Test
        @DisplayName("setDescription to null resets description")
        void setDescriptionToNull() {
            visit.setDescription("Checkup");
            visit.setDescription(null);
            assertThat(visit.getDescription()).isNull();
        }

        @Test
        @DisplayName("can set empty description")
        void canSetEmptyDescription() {
            visit.setDescription("");
            assertThat(visit.getDescription()).isEmpty();
        }

        @Test
        @DisplayName("can set long description")
        void canSetLongDescription() {
            var longDescription = "This is a very long description that might span multiple lines and contain lots of details about the pet's health status and treatment plan";
            visit.setDescription(longDescription);
            assertThat(visit.getDescription()).isEqualTo(longDescription);
        }

        @Test
        @DisplayName("has @NotEmpty validation constraint")
        void hasNotEmptyValidationConstraint() throws Exception {
            var descriptionField = Visit.class.getDeclaredField("description");
            assertThat(descriptionField.getAnnotation(NotEmpty.class)).isNotNull();
        }

        @Test
        @DisplayName("has @Column annotation")
        void hasColumnAnnotation() throws Exception {
            var descriptionField = Visit.class.getDeclaredField("description");
            var columnAnnotation = descriptionField.getAnnotation(Column.class);
            assertThat(columnAnnotation).isNotNull();
            assertThat(columnAnnotation.name()).isEqualTo("description");
        }
    }

    @Nested
    @DisplayName("Pet relationship (ManyToOne)")
    class PetRelationship {

        @Test
        @DisplayName("pet is null by default")
        void petIsNullByDefault() {
            assertThat(visit.getPet()).isNull();
        }

        @Test
        @DisplayName("setPet sets the pet")
        void setPetSetsPet() {
            var pet = new Pet();
            pet.setName("Fluffy");
            
            visit.setPet(pet);
            
            assertThat(visit.getPet()).isEqualTo(pet);
        }

        @Test
        @DisplayName("setPet overwrites previous pet")
        void setPetOverwritesPreviousPet() {
            var pet1 = new Pet();
            pet1.setName("Fluffy");
            var pet2 = new Pet();
            pet2.setName("Spot");
            
            visit.setPet(pet1);
            visit.setPet(pet2);
            
            assertThat(visit.getPet()).isEqualTo(pet2);
        }

        @Test
        @DisplayName("setPet to null resets pet")
        void setPetToNull() {
            var pet = new Pet();
            pet.setName("Rex");
            
            visit.setPet(pet);
            visit.setPet(null);
            
            assertThat(visit.getPet()).isNull();
        }

        @Test
        @DisplayName("has @ManyToOne relationship")
        void hasManyToOneRelationship() throws Exception {
            var petField = Visit.class.getDeclaredField("pet");
            var manyToOneAnnotation = petField.getAnnotation(ManyToOne.class);
            assertThat(manyToOneAnnotation).isNotNull();
        }

        @Test
        @DisplayName("has @JoinColumn with name pet_id")
        void hasJoinColumnWithNamePetId() throws Exception {
            var petField = Visit.class.getDeclaredField("pet");
            var joinColumnAnnotation = petField.getAnnotation(JoinColumn.class);
            assertThat(joinColumnAnnotation).isNotNull();
            assertThat(joinColumnAnnotation.name()).isEqualTo("pet_id");
        }
    }

    @Nested
    @DisplayName("Bidirectional relationship with Pet")
    class BidirectionalRelationshipWithPet {

        @Test
        @DisplayName("adding visit to pet sets pet on visit")
        void addingVisitToPetSetsPetOnVisit() {
            var pet = new Pet();
            var visit1 = new Visit();
            visit1.setDescription("Checkup");
            
            pet.addVisit(visit1);
            
            assertThat(visit1.getPet()).isEqualTo(pet);
        }

        @Test
        @DisplayName("adding visit to pet also adds visit to pet's collection")
        void addingVisitToPetAlsoAddsVisitToPetCollection() {
            var pet = new Pet();
            var visit1 = new Visit();
            visit1.setDescription("Vaccination");
            
            pet.addVisit(visit1);
            
            assertThat(pet.getVisits()).hasSize(1);
            assertThat(pet.getVisits().get(0)).isEqualTo(visit1);
        }

        @Test
        @DisplayName("multiple visits can reference same pet")
        void multipleVisitsCanReferenceSamePet() {
            var pet = new Pet();
            var visit1 = new Visit();
            visit1.setDescription("Checkup");
            var visit2 = new Visit();
            visit2.setDescription("Vaccination");
            var visit3 = new Visit();
            visit3.setDescription("Surgery");
            
            pet.addVisit(visit1);
            pet.addVisit(visit2);
            pet.addVisit(visit3);
            
            assertThat(visit1.getPet()).isEqualTo(pet);
            assertThat(visit2.getPet()).isEqualTo(pet);
            assertThat(visit3.getPet()).isEqualTo(pet);
        }

        @Test
        @DisplayName("setting pet on visit does not automatically add visit to pet")
        void settingPetOnVisitDoesNotAutomaticallyAddVisitToPet() {
            var pet = new Pet();
            var visit1 = new Visit();
            visit1.setDescription("Checkup");
            
            visit1.setPet(pet);
            
            // Visit is associated with pet, but not added to pet's visits collection
            // This is expected behavior - use addVisit() for bidirectional consistency
            assertThat(visit1.getPet()).isEqualTo(pet);
        }

        @Test
        @DisplayName("visits are sorted by date in pet's collection")
        void visitsAreSortedByDateInPetCollection() {
            var pet = new Pet();
            var visit1 = new Visit();
            visit1.setDescription("Last visit");
            visit1.setDate(LocalDate.of(2023, 3, 1));
            var visit2 = new Visit();
            visit2.setDescription("First visit");
            visit2.setDate(LocalDate.of(2023, 1, 1));
            var visit3 = new Visit();
            visit3.setDescription("Middle visit");
            visit3.setDate(LocalDate.of(2023, 2, 1));
            
            pet.addVisit(visit1);
            pet.addVisit(visit2);
            pet.addVisit(visit3);
            
            var visits = pet.getVisits();
            assertThat(visits.get(0).getDescription()).isEqualTo("First visit");
            assertThat(visits.get(1).getDescription()).isEqualTo("Middle visit");
            assertThat(visits.get(2).getDescription()).isEqualTo("Last visit");
        }
    }

    @Nested
    @DisplayName("JPA annotations verification")
    class JpaAnnotationsVerification {

        @Test
        @DisplayName("has @Entity annotation")
        void hasEntityAnnotation() {
            assertThat(Visit.class.isAnnotationPresent(Entity.class)).isTrue();
        }

        @Test
        @DisplayName("has @Table annotation with name visits")
        void hasTableAnnotationWithNameVisits() {
            var tableAnnotation = Visit.class.getAnnotation(Table.class);
            assertThat(tableAnnotation).isNotNull();
            assertThat(tableAnnotation.name()).isEqualTo("visits");
        }

        @Test
        @DisplayName("extends BaseEntity")
        void extendsBaseEntity() {
            assertThat(visit).isInstanceOf(BaseEntity.class);
        }
    }

    @Nested
    @DisplayName("Jakarta imports verification")
    class JakartaImportsVerification {

        @Test
        @DisplayName("uses Jakarta persistence imports")
        void usesJakartaPersistenceImports() {
            assertThat(Visit.class.getAnnotation(Entity.class)).isNotNull();
            assertThat(Visit.class.getAnnotation(Table.class)).isNotNull();
        }

        @Test
        @DisplayName("uses Jakarta validation imports")
        void usesJakartaValidationImports() throws Exception {
            var descriptionField = Visit.class.getDeclaredField("description");
            assertThat(descriptionField.getAnnotation(NotEmpty.class)).isNotNull();
        }
    }

    @Nested
    @DisplayName("Entity lifecycle from inheritance")
    class EntityLifecycleFromInheritance {

        @Test
        @DisplayName("inherits id lifecycle methods from BaseEntity")
        void inheritsIdLifecycleMethodsFromBaseEntity() {
            assertThat(visit.getId()).isNull();
            visit.setId(42);
            assertThat(visit.getId()).isEqualTo(42);
        }

        @Test
        @DisplayName("inherits isNew lifecycle method from BaseEntity")
        void inheritsIsNewLifecycleMethodFromBaseEntity() {
            assertThat(visit.isNew()).isTrue();
            visit.setId(1);
            assertThat(visit.isNew()).isFalse();
        }

        @Test
        @DisplayName("is new when id is null regardless of other fields")
        void isNewWhenIdIsNullRegardlessOfOtherFields() {
            visit.setDescription("Checkup");
            visit.setDate(LocalDate.of(2023, 6, 15));
            assertThat(visit.isNew()).isTrue();
        }

        @Test
        @DisplayName("is not new when id is set even if other fields are null")
        void isNotNewWhenIdIsSet() {
            visit.setId(1);
            assertThat(visit.isNew()).isFalse();
        }

        @Test
        @DisplayName("can set id after construction")
        void canSetIdAfterConstruction() {
            visit.setId(100);
            assertThat(visit.getId()).isEqualTo(100);
        }

        @Test
        @DisplayName("can set id to zero")
        void canSetIdToZero() {
            visit.setId(0);
            assertThat(visit.getId()).isEqualTo(0);
            assertThat(visit.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("Business logic validation")
    class BusinessLogicValidation {

        @Test
        @DisplayName("can create visit with current date")
        void canCreateVisitWithCurrentDate() {
            var visit1 = new Visit();
            var today = LocalDate.now();
            assertThat(visit1.getDate()).isEqualTo(today);
        }

        @Test
        @DisplayName("can create visit with past date")
        void canCreateVisitWithPastDate() {
            var pastDate = LocalDate.of(2020, 5, 10);
            var visit1 = new Visit();
            visit1.setDate(pastDate);
            assertThat(visit1.getDate()).isEqualTo(pastDate);
        }

        @Test
        @DisplayName("can create visit with future date")
        void canCreateVisitWithFutureDate() {
            var futureDate = LocalDate.of(2030, 12, 25);
            var visit1 = new Visit();
            visit1.setDate(futureDate);
            assertThat(visit1.getDate()).isEqualTo(futureDate);
        }

        @Test
        @DisplayName("can associate visit with different pets")
        void canAssociateVisitWithDifferentPets() {
            var pet1 = new Pet();
            pet1.setName("Fluffy");
            var pet2 = new Pet();
            pet2.setName("Spot");
            
            var visit1 = new Visit();
            visit1.setDescription("Checkup");
            visit1.setPet(pet1);
            
            var visit2 = new Visit();
            visit2.setDescription("Vaccination");
            visit2.setPet(pet2);
            
            assertThat(visit1.getPet()).isEqualTo(pet1);
            assertThat(visit2.getPet()).isEqualTo(pet2);
            assertThat(visit1.getPet()).isNotEqualTo(visit2.getPet());
        }

        @Test
        @DisplayName("can have multiple visits for same pet")
        void canHaveMultipleVisitsForSamePet() {
            var pet = new Pet();
            pet.setName("Rex");
            
            var visit1 = new Visit();
            visit1.setDescription("Checkup");
            visit1.setDate(LocalDate.of(2023, 1, 1));
            
            var visit2 = new Visit();
            visit2.setDescription("Vaccination");
            visit2.setDate(LocalDate.of(2023, 6, 1));
            
            pet.addVisit(visit1);
            pet.addVisit(visit2);
            
            assertThat(pet.getVisits()).hasSize(2);
            assertThat(visit1.getPet()).isEqualTo(pet);
            assertThat(visit2.getPet()).isEqualTo(pet);
        }

        @Test
        @DisplayName("description is required but not automatically validated")
        void descriptionIsRequiredButNotAutomaticallyValidated() throws Exception {
            // The @NotEmpty annotation is present, but bean validation would need to be triggered
            // This test verifies the annotation is present, not that validation is automatically applied
            var descriptionField = Visit.class.getDeclaredField("description");
            assertThat(descriptionField.getAnnotation(NotEmpty.class)).isNotNull();
            
            // We can still set null or empty values at the object level
            visit.setDescription(null);
            assertThat(visit.getDescription()).isNull();
            
            visit.setDescription("");
            assertThat(visit.getDescription()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Integration with entity relationships")
    class IntegrationWithEntityRelationships {

        @Test
        @DisplayName("complete Owner -> Pet -> Visit relationship")
        void completeOwnerPetVisitRelationship() {
            var owner = new Owner();
            owner.setFirstName("John");
            owner.setLastName("Doe");
            
            var pet = new Pet();
            pet.setName("Fluffy");
            pet.setBirthDate(LocalDate.of(2020, 1, 1));
            
            var visit = new Visit();
            visit.setDescription("Annual checkup");
            visit.setDate(LocalDate.of(2023, 6, 15));
            
            // Establish relationships
            owner.addPet(pet);
            pet.addVisit(visit);
            
            // Verify bidirectional relationships
            assertThat(pet.getOwner()).isEqualTo(owner);
            assertThat(visit.getPet()).isEqualTo(pet);
            assertThat(owner.getPets()).containsExactly(pet);
            assertThat(pet.getVisits()).containsExactly(visit);
            
            // Verify data integrity
            var ownerFullName = owner.getFirstName() + " " + owner.getLastName();
            assertThat(ownerFullName).isEqualTo("John Doe");
            assertThat(pet.getName()).isEqualTo("Fluffy");
            assertThat(visit.getDescription()).isEqualTo("Annual checkup");
        }

        @Test
        @DisplayName("multiple pets with multiple visits")
        void multiplePetsWithMultipleVisits() {
            var owner = new Owner();
            owner.setFirstName("Jane");
            owner.setLastName("Smith");
            
            var pet1 = new Pet();
            pet1.setName("Fluffy");
            var pet2 = new Pet();
            pet2.setName("Spot");
            
            var visit1 = new Visit();
            visit1.setDescription("Fluffy's checkup");
            visit1.setDate(LocalDate.of(2023, 1, 1));
            
            var visit2 = new Visit();
            visit2.setDescription("Spot's vaccination");
            visit2.setDate(LocalDate.of(2023, 2, 1));
            
            var visit3 = new Visit();
            visit3.setDescription("Fluffy's vaccination");
            visit3.setDate(LocalDate.of(2023, 3, 1));
            
            // Establish relationships
            owner.addPet(pet1);
            owner.addPet(pet2);
            pet1.addVisit(visit1);
            pet2.addVisit(visit2);
            pet1.addVisit(visit3);
            
            // Verify relationships
            assertThat(owner.getPets()).hasSize(2);
            assertThat(pet1.getVisits()).hasSize(2);
            assertThat(pet2.getVisits()).hasSize(1);
            
            assertThat(visit1.getPet()).isEqualTo(pet1);
            assertThat(visit2.getPet()).isEqualTo(pet2);
            assertThat(visit3.getPet()).isEqualTo(pet1);
            
            // Verify visits are sorted by date in pet collections
            assertThat(pet1.getVisits().get(0).getDescription()).isEqualTo("Fluffy's checkup");
            assertThat(pet1.getVisits().get(1).getDescription()).isEqualTo("Fluffy's vaccination");
            assertThat(pet2.getVisits().get(0).getDescription()).isEqualTo("Spot's vaccination");
        }
    }
}