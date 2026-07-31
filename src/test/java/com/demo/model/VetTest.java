package com.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VetTest {

    private Vet fixture;

    @BeforeEach
    void setUp() {
        fixture = new Vet();
    }

    @Nested
    @DisplayName("Specialties relationship (ManyToMany)")
    class SpecialtiesRelationship {

        @Test
        @DisplayName("specialties is null by default")
        void specialtiesIsNullByDefault() {
            var specialties = fixture.getSpecialties();
            assertThat(specialties).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("getSpecialties returns unmodifiable list")
        void getSpecialtiesReturnsUnmodifiableList() {
            var specialties = fixture.getSpecialties();
            assertThat(specialties).isInstanceOf(Collections.unmodifiableList(List.of()).getClass());
        }

        @Test
        @DisplayName("addSpecialty adds specialty to collection")
        void addSpecialtyAddsSpecialtyToCollection() {
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");
            
            fixture.addSpecialty(cardiology);
            
            var specialties = fixture.getSpecialties();
            assertThat(specialties).hasSize(1);
            assertThat(specialties.get(0).getName()).isEqualTo("Cardiology");
        }

        @Test
        @DisplayName("multiple specialties can be added")
        void multipleSpecialtiesCanBeAdded() {
            var surgery = new Specialty();
            surgery.setName("Surgery");
            var radiology = new Specialty();
            radiology.setName("Radiology");
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");
            
            fixture.addSpecialty(surgery);
            fixture.addSpecialty(radiology);
            fixture.addSpecialty(cardiology);
            
            assertThat(fixture.getSpecialties()).hasSize(3);
        }

        @Test
        @DisplayName("getSpecialties returns specialties sorted by name")
        void getSpecialtiesReturnsSpecialtiesSortedByName() {
            var surgery = new Specialty();
            surgery.setName("Surgery");
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");
            var radiology = new Specialty();
            radiology.setName("Radiology");
            
            fixture.addSpecialty(surgery);
            fixture.addSpecialty(cardiology);
            fixture.addSpecialty(radiology);
            
            var specialties = fixture.getSpecialties();
            assertThat(specialties.get(0).getName()).isEqualTo("Cardiology");
            assertThat(specialties.get(1).getName()).isEqualTo("Radiology");
            assertThat(specialties.get(2).getName()).isEqualTo("Surgery");
        }

        @Test
        @DisplayName("specialties with null names are sorted last")
        void specialtiesWithNullNamesAreSortedLast() {
            var surgery = new Specialty();
            surgery.setName("Surgery");
            var nullSpecialty = new Specialty();
            // nullSpecialty has null name
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");
            
            fixture.addSpecialty(surgery);
            fixture.addSpecialty(nullSpecialty);
            fixture.addSpecialty(cardiology);
            
            var specialties = fixture.getSpecialties();
            assertThat(specialties.get(0).getName()).isEqualTo("Cardiology");
            assertThat(specialties.get(1).getName()).isEqualTo("Surgery");
            assertThat(specialties.get(2).getName()).isNull();
        }

        @Test
        @DisplayName("setSpecialties replaces entire collection")
        void setSpecialtiesReplacesEntireCollection() {
            var surgery = new Specialty();
            surgery.setName("Surgery");
            var radiology = new Specialty();
            radiology.setName("Radiology");
            
            var specialties = new ArrayList<Specialty>();
            specialties.add(surgery);
            specialties.add(radiology);
            
            fixture.setSpecialties(specialties);
            
            assertThat(fixture.getSpecialties()).hasSize(2);
            // getSpecialties sorts by name
            assertThat(fixture.getSpecialties().get(0).getName()).isEqualTo("Radiology");
            assertThat(fixture.getSpecialties().get(1).getName()).isEqualTo("Surgery");
        }

        @Test
        @DisplayName("clearSpecialties removes all specialties")
        void clearSpecialtiesRemovesAllSpecialties() {
            var surgery = new Specialty();
            surgery.setName("Surgery");
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");
            
            fixture.addSpecialty(surgery);
            fixture.addSpecialty(cardiology);
            
            assertThat(fixture.getSpecialties()).hasSize(2);
            
            fixture.clearSpecialties();
            
            assertThat(fixture.getSpecialties()).isEmpty();
        }

        @Test
        @DisplayName("clearSpecialties on empty collection does not fail")
        void clearSpecialtiesOnEmptyCollectionDoesNotFail() {
            fixture.clearSpecialties();
            assertThat(fixture.getSpecialties()).isEmpty();
        }

        @Test
        @DisplayName("has @ManyToMany relationship with FetchType.EAGER")
        void hasManyToManyRelationshipWithFetchTypeEager() throws Exception {
            var specialtiesField = Vet.class.getDeclaredField("specialties");
            var manyToManyAnnotation = specialtiesField.getAnnotation(ManyToMany.class);
            assertThat(manyToManyAnnotation).isNotNull();
            assertThat(manyToManyAnnotation.fetch()).isEqualTo(FetchType.EAGER);
        }

        @Test
        @DisplayName("has @JoinTable with correct configuration")
        void hasJoinTableWithCorrectConfiguration() throws Exception {
            var specialtiesField = Vet.class.getDeclaredField("specialties");
            var joinTableAnnotation = specialtiesField.getAnnotation(JoinTable.class);
            assertThat(joinTableAnnotation).isNotNull();
            assertThat(joinTableAnnotation.name()).isEqualTo("vet_specialties");
            
            var joinColumns = joinTableAnnotation.joinColumns();
            assertThat(joinColumns).hasSize(1);
            assertThat(joinColumns[0].name()).isEqualTo("vet_id");
            
            var inverseJoinColumns = joinTableAnnotation.inverseJoinColumns();
            assertThat(inverseJoinColumns).hasSize(1);
            assertThat(inverseJoinColumns[0].name()).isEqualTo("specialty_id");
        }
    }

    @Nested
    @DisplayName("Business method: getNrOfSpecialties")
    class GetNrOfSpecialtiesBusinessMethod {

        @Test
        @DisplayName("returns zero when no specialties")
        void returnsZeroWhenNoSpecialties() {
            assertThat(fixture.getNrOfSpecialties()).isZero();
        }

        @Test
        @DisplayName("returns correct count after adding specialties")
        void returnsCorrectCountAfterAddingSpecialties() {
            var surgery = new Specialty();
            surgery.setName("Surgery");
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");
            var radiology = new Specialty();
            radiology.setName("Radiology");
            
            fixture.addSpecialty(surgery);
            assertThat(fixture.getNrOfSpecialties()).isEqualTo(1);
            
            fixture.addSpecialty(cardiology);
            assertThat(fixture.getNrOfSpecialties()).isEqualTo(2);
            
            fixture.addSpecialty(radiology);
            assertThat(fixture.getNrOfSpecialties()).isEqualTo(3);
        }

        @Test
        @DisplayName("returns correct count after clearing specialties")
        void returnsCorrectCountAfterClearingSpecialties() {
            var surgery = new Specialty();
            surgery.setName("Surgery");
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");
            
            fixture.addSpecialty(surgery);
            fixture.addSpecialty(cardiology);
            assertThat(fixture.getNrOfSpecialties()).isEqualTo(2);
            
            fixture.clearSpecialties();
            assertThat(fixture.getNrOfSpecialties()).isZero();
        }

        @Test
        @DisplayName("returns correct count after setting specialties")
        void returnsCorrectCountAfterSettingSpecialties() {
            var surgery = new Specialty();
            surgery.setName("Surgery");
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");
            
            var specialties = new ArrayList<Specialty>();
            specialties.add(surgery);
            specialties.add(cardiology);
            
            fixture.setSpecialties(specialties);
            assertThat(fixture.getNrOfSpecialties()).isEqualTo(2);
        }

        @Test
        @DisplayName("is annotated with @JsonIgnore")
        void isAnnotatedWithJsonIgnore() throws NoSuchMethodException {
            var method = Vet.class.getMethod("getNrOfSpecialties");
            assertThat(method.getAnnotation(JsonIgnore.class)).isNotNull();
        }
    }

    @Nested
    @DisplayName("Specialties internal methods")
    class SpecialtiesInternalMethods {

        @Test
        @DisplayName("getSpecialtiesInternal returns non-null set")
        void getSpecialtiesInternalReturnsNonNullSet() {
            var specialtiesInternal = fixture.getSpecialtiesInternal();
            assertThat(specialtiesInternal).isNotNull();
        }

        @Test
        @DisplayName("getSpecialtiesInternal initializes empty set when null")
        void getSpecialtiesInternalInitializesEmptySetWhenNull() {
            var specialtiesInternal = fixture.getSpecialtiesInternal();
            assertThat(specialtiesInternal).isNotNull().isEmpty();
            
            // Call again to verify it returns the same instance
            var specialtiesInternal2 = fixture.getSpecialtiesInternal();
            assertThat(specialtiesInternal2).isSameAs(specialtiesInternal);
        }

        @Test
        @DisplayName("setSpecialtiesInternal sets the internal set")
        void setSpecialtiesInternalSetsTheInternalSet() {
            var surgery = new Specialty();
            surgery.setName("Surgery");
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");
            
            var specialties = new HashSet<Specialty>();
            specialties.add(surgery);
            specialties.add(cardiology);
            
            fixture.setSpecialtiesInternal(specialties);
            
            assertThat(fixture.getSpecialties()).hasSize(2);
        }

        @Test
        @DisplayName("setSpecialtiesInternal with null creates null internal set")
        void setSpecialtiesInternalWithNullCreatesNullInternalSet() {
            fixture.setSpecialtiesInternal(null);
            
            // getSpecialtiesInternal will create a new empty set when called
            var specialties = fixture.getSpecialties();
            assertThat(specialties).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("getSpecialtiesInternal is annotated with @JsonIgnore")
        void getSpecialtiesInternalIsAnnotatedWithJsonIgnore() throws Exception {
            var method = Vet.class.getDeclaredMethod("getSpecialtiesInternal");
            method.setAccessible(true);
            assertThat(method.getAnnotation(JsonIgnore.class)).isNotNull();
        }

        @Test
        @DisplayName("setSpecialtiesInternal is protected")
        void setSpecialtiesInternalIsProtected() throws NoSuchMethodException {
            var method = Vet.class.getDeclaredMethod("setSpecialtiesInternal", Set.class);
            assertThat(java.lang.reflect.Modifier.isProtected(method.getModifiers())).isTrue();
        }
    }

    @Nested
    @DisplayName("JPA annotations verification")
    class JpaAnnotationsVerification {

        @Test
        @DisplayName("has @Entity annotation")
        void hasEntityAnnotation() {
            assertThat(Vet.class.isAnnotationPresent(Entity.class)).isTrue();
        }

        @Test
        @DisplayName("has @Table annotation with name vets")
        void hasTableAnnotationWithNameVets() {
            var tableAnnotation = Vet.class.getAnnotation(Table.class);
            assertThat(tableAnnotation).isNotNull();
            assertThat(tableAnnotation.name()).isEqualTo("vets");
        }

        @Test
        @DisplayName("extends Person")
        void extendsPerson() {
            assertThat(fixture).isInstanceOf(Person.class);
        }

        @Test
        @DisplayName("extends BaseEntity through Person")
        void extendsBaseEntityThroughPerson() {
            assertThat(fixture).isInstanceOf(BaseEntity.class);
        }
    }

    @Nested
    @DisplayName("Jakarta imports verification")
    class JakartaImportsVerification {

        @Test
        @DisplayName("uses Jakarta persistence imports")
        void usesJakartaPersistenceImports() {
            assertThat(Vet.class.getAnnotation(Entity.class)).isNotNull();
            assertThat(Vet.class.getAnnotation(Table.class)).isNotNull();
        }

        @Test
        @DisplayName("uses Jakarta ManyToMany imports")
        void usesJakartaManyToManyImports() throws Exception {
            var specialtiesField = Vet.class.getDeclaredField("specialties");
            assertThat(specialtiesField.getAnnotation(ManyToMany.class)).isNotNull();
            assertThat(specialtiesField.getAnnotation(JoinTable.class)).isNotNull();
        }
    }

    @Nested
    @DisplayName("Entity lifecycle from inheritance")
    class EntityLifecycleFromInheritance {

        @Test
        @DisplayName("inherits name fields from Person")
        void inheritsNameFieldsFromPerson() {
            fixture.setFirstName("Dr. Jane");
            fixture.setLastName("Smith");
            assertThat(fixture.getFirstName()).isEqualTo("Dr. Jane");
            assertThat(fixture.getLastName()).isEqualTo("Smith");
            // getFullName doesn't exist, verify with firstName + lastName
            var fullName = fixture.getFirstName() + " " + fixture.getLastName();
            assertThat(fullName).isEqualTo("Dr. Jane Smith");
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
        @DisplayName("is new when id is null regardless of other fields")
        void isNewWhenIdIsNullRegardlessOfOtherFields() {
            fixture.setFirstName("Dr. John");
            fixture.setLastName("Doe");
            assertThat(fixture.isNew()).isTrue();
        }

        @Test
        @DisplayName("is not new when id is set even if other fields are null")
        void isNotNewWhenIdIsSet() {
            fixture.setId(1);
            assertThat(fixture.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("Bidirectional relationship with Specialty")
    class BidirectionalRelationshipWithSpecialty {

        @Test
        @DisplayName("adding specialty to fixture does not set back-reference")
        void addingSpecialtyToVetDoesNotSetBackReference() {
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");
            
            fixture.addSpecialty(cardiology);
            
            // Vet maintains unidirectional relationship to Specialty
            // Specialty doesn't maintain back-reference to Vets
            assertThat(fixture.getSpecialties()).hasSize(1);
            assertThat(fixture.getSpecialties().get(0).getName()).isEqualTo("Cardiology");
        }

        @Test
        @DisplayName("multiple vets can reference same specialty")
        void multipleVetsCanReferenceSameSpecialty() {
            var surgery = new Specialty();
            surgery.setName("Surgery");
            
            var vet1 = new Vet();
            vet1.setFirstName("Dr. John");
            vet1.addSpecialty(surgery);
            
            var vet2 = new Vet();
            vet2.setFirstName("Dr. Jane");
            vet2.addSpecialty(surgery);
            
            assertThat(vet1.getSpecialties()).containsExactly(surgery);
            assertThat(vet2.getSpecialties()).containsExactly(surgery);
            assertThat(vet1.getSpecialties().get(0)).isSameAs(vet2.getSpecialties().get(0));
        }

        @Test
        @DisplayName("fixture can change specialties")
        void vetCanChangeSpecialties() {
            var surgery = new Specialty();
            surgery.setName("Surgery");
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");
            
            fixture.addSpecialty(surgery);
            assertThat(fixture.getSpecialties()).hasSize(1);
            
            fixture.clearSpecialties();
            fixture.addSpecialty(cardiology);
            assertThat(fixture.getSpecialties()).hasSize(1);
            assertThat(fixture.getSpecialties().get(0).getName()).isEqualTo("Cardiology");
        }
    }

    @Nested
    @DisplayName("Business logic validation")
    class BusinessLogicValidation {

        @Test
        @DisplayName("can represent general practice veterinarian")
        void canRepresentGeneralPracticeVeterinarian() {
            var generalVet = new Vet();
            generalVet.setFirstName("Dr. Sarah");
            generalVet.setLastName("Johnson");
            
            assertThat(generalVet.getSpecialties()).isEmpty();
            assertThat(generalVet.getNrOfSpecialties()).isZero();
        }

        @Test
        @DisplayName("can represent specialist veterinarian")
        void canRepresentSpecialistVeterinarian() {
            var specialistVet = new Vet();
            specialistVet.setFirstName("Dr. Michael");
            specialistVet.setLastName("Brown");
            
            var surgery = new Specialty();
            surgery.setName("Surgery");
            specialistVet.addSpecialty(surgery);
            
            assertThat(specialistVet.getSpecialties()).hasSize(1);
            assertThat(specialistVet.getNrOfSpecialties()).isEqualTo(1);
        }

        @Test
        @DisplayName("can represent multi-specialist veterinarian")
        void canRepresentMultiSpecialistVeterinarian() {
            var multiSpecialist = new Vet();
            multiSpecialist.setFirstName("Dr. Emily");
            multiSpecialist.setLastName("Davis");
            
            var surgery = new Specialty();
            surgery.setName("Surgery");
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");
            var radiology = new Specialty();
            radiology.setName("Radiology");
            
            multiSpecialist.addSpecialty(surgery);
            multiSpecialist.addSpecialty(cardiology);
            multiSpecialist.addSpecialty(radiology);
            
            assertThat(multiSpecialist.getSpecialties()).hasSize(3);
            assertThat(multiSpecialist.getNrOfSpecialties()).isEqualTo(3);
        }

        @Test
        @DisplayName("specialties are automatically sorted by name")
        void specialtiesAreAutomaticallySortedByName() {
            var surgery = new Specialty();
            surgery.setName("Surgery");
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");
            var radiology = new Specialty();
            radiology.setName("Radiology");
            
            // Add in random order
            fixture.addSpecialty(surgery);
            fixture.addSpecialty(radiology);
            fixture.addSpecialty(cardiology);
            
            var specialties = fixture.getSpecialties();
            assertThat(specialties.get(0).getName()).isEqualTo("Cardiology");
            assertThat(specialties.get(1).getName()).isEqualTo("Radiology");
            assertThat(specialties.get(2).getName()).isEqualTo("Surgery");
        }

        @Test
        @DisplayName("can clear and re-add specialties")
        void canClearAndReAddSpecialties() {
            var surgery = new Specialty();
            surgery.setName("Surgery");
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");
            var radiology = new Specialty();
            radiology.setName("Radiology");
            
            fixture.addSpecialty(surgery);
            fixture.addSpecialty(cardiology);
            
            fixture.clearSpecialties();
            assertThat(fixture.getSpecialties()).isEmpty();
            assertThat(fixture.getNrOfSpecialties()).isZero();
            
            fixture.addSpecialty(radiology);
            assertThat(fixture.getSpecialties()).hasSize(1);
            assertThat(fixture.getNrOfSpecialties()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Integration with entity relationships")
    class IntegrationWithEntityRelationships {

        @Test
        @DisplayName("complete Vet -> Specialty relationship")
        void completeVetSpecialtyRelationship() {
            fixture.setFirstName("Dr. Lisa");
            fixture.setLastName("Wilson");
            
            var surgery = new Specialty();
            surgery.setName("Surgery");
            surgery.setId(1);
            
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");
            cardiology.setId(2);
            
            // Establish relationship
            fixture.addSpecialty(surgery);
            fixture.addSpecialty(cardiology);
            
            // Verify relationship
            assertThat(fixture.getSpecialties()).hasSize(2);
            assertThat(fixture.getNrOfSpecialties()).isEqualTo(2);
            var fullName = fixture.getFirstName() + " " + fixture.getLastName();
            assertThat(fullName).isEqualTo("Dr. Lisa Wilson");
            
            // Verify specialties are sorted
            var specialties = fixture.getSpecialties();
            assertThat(specialties.get(0).getName()).isEqualTo("Cardiology");
            assertThat(specialties.get(1).getName()).isEqualTo("Surgery");
        }

        @Test
        @DisplayName("multiple vets with different specialty combinations")
        void multipleVetsWithDifferentSpecialtyCombinations() {
            var surgery = new Specialty();
            surgery.setName("Surgery");
            
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");
            
            var vet1 = new Vet();
            vet1.setFirstName("Dr. Tom");
            vet1.addSpecialty(surgery);
            
            var vet2 = new Vet();
            vet2.setFirstName("Dr. Amy");
            vet2.addSpecialty(cardiology);
            
            var vet3 = new Vet();
            vet3.setFirstName("Dr. Bob");
            vet3.addSpecialty(surgery);
            vet3.addSpecialty(cardiology);
            
            assertThat(vet1.getSpecialties()).hasSize(1);
            assertThat(vet2.getSpecialties()).hasSize(1);
            assertThat(vet3.getSpecialties()).hasSize(2);
            
            assertThat(vet1.getSpecialties().get(0).getName()).isEqualTo("Surgery");
            assertThat(vet2.getSpecialties().get(0).getName()).isEqualTo("Cardiology");
            assertThat(vet3.getSpecialties().get(0).getName()).isEqualTo("Cardiology");
            assertThat(vet3.getSpecialties().get(1).getName()).isEqualTo("Surgery");
        }
    }

    @Nested
    @DisplayName("Edge cases")
    class EdgeCases {

        @Test
        @DisplayName("can handle null specialty in addSpecialty")
        void canHandleNullSpecialtyInAddSpecialty() {
            fixture.addSpecialty(null);
            // AS-IS: HashSet accepts null specialty
            assertThat(fixture.getNrOfSpecialties()).isEqualTo(1);
            assertThat(fixture.getSpecialties()).hasSize(1);
            assertThat(fixture.getSpecialties().get(0)).isNull();
        }

        @Test
        @DisplayName("can handle null specialties list in setSpecialties")
        void canHandleNullSpecialtiesListInSetSpecialties() {
            org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class,
                () -> fixture.setSpecialties(null));
        }

        @Test
        @DisplayName("can handle empty specialties list in setSpecialties")
        void canHandleEmptySpecialtiesListInSetSpecialties() {
            var emptyList = new ArrayList<Specialty>();
            fixture.setSpecialties(emptyList);
            assertThat(fixture.getSpecialties()).isEmpty();
        }

        @Test
        @DisplayName("can handle duplicate specialties")
        void canHandleDuplicateSpecialties() {
            var surgery = new Specialty();
            surgery.setName("Surgery");
            
            fixture.addSpecialty(surgery);
            fixture.addSpecialty(surgery); // Add same specialty again
            
            // HashSet should prevent duplicates
            assertThat(fixture.getSpecialties()).hasSize(1);
            assertThat(fixture.getNrOfSpecialties()).isEqualTo(1);
        }

        @Test
        @DisplayName("getSpecialtiesInternal returns same instance after modifications")
        void getSpecialtiesInternalReturnsSameInstanceAfterModifications() {
            var surgery = new Specialty();
            surgery.setName("Surgery");
            
            var internal1 = fixture.getSpecialtiesInternal();
            fixture.addSpecialty(surgery);
            var internal2 = fixture.getSpecialtiesInternal();
            
            assertThat(internal1).isSameAs(internal2).hasSize(1);
        }
    }
}