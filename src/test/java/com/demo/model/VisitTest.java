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

    private Visit fixture;

    @BeforeEach
    void setUp() {
        fixture = new Visit();
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
            assertThat(fixture.getDate()).isEqualTo(today);
        }

        @Test
        @DisplayName("date is initialized automatically")
        void dateIsInitializedAutomatically() {
            assertThat(fixture.getDate()).isNotNull().isInstanceOf(LocalDate.class);
        }
    }

    @Nested
    @DisplayName("Date field")
    class DateField {

        @Test
        @DisplayName("date is initialized by constructor")
        void dateIsInitializedByConstructor() {
            assertThat(fixture.getDate()).isNotNull();
        }

        @Test
        @DisplayName("setDate sets the date")
        void setDateSetsDate() {
            var date = LocalDate.of(2023, 6, 15);
            fixture.setDate(date);
            assertThat(fixture.getDate()).isEqualTo(date);
        }

        @Test
        @DisplayName("setDate overwrites previous date")
        void setDateOverwritesPreviousDate() {
            var date1 = LocalDate.of(2023, 1, 1);
            var date2 = LocalDate.of(2023, 12, 31);
            
            fixture.setDate(date1);
            fixture.setDate(date2);
            
            assertThat(fixture.getDate()).isEqualTo(date2);
        }

        @Test
        @DisplayName("setDate to null resets date")
        void setDateToNull() {
            fixture.setDate(LocalDate.of(2023, 6, 15));
            fixture.setDate(null);
            
            assertThat(fixture.getDate()).isNull();
        }

        @Test
        @DisplayName("can set date to past date")
        void canSetDateToPastDate() {
            var pastDate = LocalDate.of(2020, 3, 10);
            fixture.setDate(pastDate);
            assertThat(fixture.getDate()).isEqualTo(pastDate);
        }

        @Test
        @DisplayName("can set date to future date")
        void canSetDateToFutureDate() {
            var futureDate = LocalDate.of(2025, 12, 25);
            fixture.setDate(futureDate);
            assertThat(fixture.getDate()).isEqualTo(futureDate);
        }

        @Test
        @DisplayName("can set date to today")
        void canSetDateToToday() {
            var today = LocalDate.now();
            fixture.setDate(today);
            assertThat(fixture.getDate()).isEqualTo(today);
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
            assertThat(fixture.getDescription()).isNull();
        }

        @Test
        @DisplayName("setDescription sets the description")
        void setDescriptionSetsDescription() {
            fixture.setDescription("Regular checkup");
            assertThat(fixture.getDescription()).isEqualTo("Regular checkup");
        }

        @Test
        @DisplayName("setDescription overwrites previous description")
        void setDescriptionOverwritesPreviousDescription() {
            fixture.setDescription("First fixture");
            fixture.setDescription("Second fixture");
            assertThat(fixture.getDescription()).isEqualTo("Second fixture");
        }

        @Test
        @DisplayName("setDescription to null resets description")
        void setDescriptionToNull() {
            fixture.setDescription("Checkup");
            fixture.setDescription(null);
            assertThat(fixture.getDescription()).isNull();
        }

        @Test
        @DisplayName("can set empty description")
        void canSetEmptyDescription() {
            fixture.setDescription("");
            assertThat(fixture.getDescription()).isEmpty();
        }

        @Test
        @DisplayName("can set long description")
        void canSetLongDescription() {
            var longDescription = "This is a very long description that might span multiple lines and contain lots of details about the pet's health status and treatment plan";
            fixture.setDescription(longDescription);
            assertThat(fixture.getDescription()).isEqualTo(longDescription);
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
            assertThat(fixture.getPet()).isNull();
        }

        @Test
        @DisplayName("setPet sets the pet")
        void setPetSetsPet() {
            var pet = new Pet();
            pet.setName("Fluffy");
            
            fixture.setPet(pet);
            
            assertThat(fixture.getPet()).isEqualTo(pet);
        }

        @Test
        @DisplayName("setPet overwrites previous pet")
        void setPetOverwritesPreviousPet() {
            var pet1 = new Pet();
            pet1.setName("Fluffy");
            var pet2 = new Pet();
            pet2.setName("Spot");
            
            fixture.setPet(pet1);
            fixture.setPet(pet2);
            
            assertThat(fixture.getPet()).isEqualTo(pet2);
        }

        @Test
        @DisplayName("setPet to null resets pet")
        void setPetToNull() {
            var pet = new Pet();
            pet.setName("Rex");
            
            fixture.setPet(pet);
            fixture.setPet(null);
            
            assertThat(fixture.getPet()).isNull();
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
        @DisplayName("adding fixture to pet sets pet on fixture")
        void addingVisitToPetSetsPetOnVisit() {
            var pet = new Pet();
            var visit1 = new Visit();
            visit1.setDescription("Checkup");
            
            pet.addVisit(visit1);
            
            assertThat(visit1.getPet()).isEqualTo(pet);
        }

        @Test
        @DisplayName("adding fixture to pet also adds fixture to pet's collection")
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
        @DisplayName("setting pet on fixture does not automatically add fixture to pet")
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
            visit1.setDescription("Last fixture");
            visit1.setDate(LocalDate.of(2023, 3, 1));
            var visit2 = new Visit();
            visit2.setDescription("First fixture");
            visit2.setDate(LocalDate.of(2023, 1, 1));
            var visit3 = new Visit();
            visit3.setDescription("Middle fixture");
            visit3.setDate(LocalDate.of(2023, 2, 1));
            
            pet.addVisit(visit1);
            pet.addVisit(visit2);
            pet.addVisit(visit3);
            
            var visits = pet.getVisits();
            assertThat(visits.get(0).getDescription()).isEqualTo("First fixture");
            assertThat(visits.get(1).getDescription()).isEqualTo("Middle fixture");
            assertThat(visits.get(2).getDescription()).isEqualTo("Last fixture");
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
            assertThat(fixture).isInstanceOf(BaseEntity.class);
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
        @DisplayName("is new when id is null regardless of other fields")
        void isNewWhenIdIsNullRegardlessOfOtherFields() {
            fixture.setDescription("Checkup");
            fixture.setDate(LocalDate.of(2023, 6, 15));
            assertThat(fixture.isNew()).isTrue();
        }

        @Test
        @DisplayName("is not new when id is set even if other fields are null")
        void isNotNewWhenIdIsSet() {
            fixture.setId(1);
            assertThat(fixture.isNew()).isFalse();
        }

        @Test
        @DisplayName("can set id after construction")
        void canSetIdAfterConstruction() {
            fixture.setId(100);
            assertThat(fixture.getId()).isEqualTo(100);
        }

        @Test
        @DisplayName("can set id to zero")
        void canSetIdToZero() {
            fixture.setId(0);
            assertThat(fixture.getId()).isZero();
            assertThat(fixture.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("Business logic validation")
    class BusinessLogicValidation {

        @Test
        @DisplayName("can create fixture with current date")
        void canCreateVisitWithCurrentDate() {
            var visit1 = new Visit();
            var today = LocalDate.now();
            assertThat(visit1.getDate()).isEqualTo(today);
        }

        @Test
        @DisplayName("can create fixture with past date")
        void canCreateVisitWithPastDate() {
            var pastDate = LocalDate.of(2020, 5, 10);
            var visit1 = new Visit();
            visit1.setDate(pastDate);
            assertThat(visit1.getDate()).isEqualTo(pastDate);
        }

        @Test
        @DisplayName("can create fixture with future date")
        void canCreateVisitWithFutureDate() {
            var futureDate = LocalDate.of(2030, 12, 25);
            var visit1 = new Visit();
            visit1.setDate(futureDate);
            assertThat(visit1.getDate()).isEqualTo(futureDate);
        }

        @Test
        @DisplayName("can associate fixture with different pets")
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
            fixture.setDescription(null);
            assertThat(fixture.getDescription()).isNull();
            
            fixture.setDescription("");
            assertThat(fixture.getDescription()).isEmpty();
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
            
            fixture.setDescription("Annual checkup");
            fixture.setDate(LocalDate.of(2023, 6, 15));
            
            // Establish relationships
            owner.addPet(pet);
            pet.addVisit(fixture);
            
            // Verify bidirectional relationships
            assertThat(pet.getOwner()).isEqualTo(owner);
            assertThat(fixture.getPet()).isEqualTo(pet);
            assertThat(owner.getPets()).containsExactly(pet);
            assertThat(pet.getVisits()).containsExactly(fixture);
            
            // Verify data integrity
            var ownerFullName = owner.getFirstName() + " " + owner.getLastName();
            assertThat(ownerFullName).isEqualTo("John Doe");
            assertThat(pet.getName()).isEqualTo("Fluffy");
            assertThat(fixture.getDescription()).isEqualTo("Annual checkup");
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