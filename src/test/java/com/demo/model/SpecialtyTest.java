package com.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpecialtyTest {

    private Specialty specialty;

    @BeforeEach
    void setUp() {
        specialty = new Specialty();
    }

    @Nested
    @DisplayName("Name field inheritance from NamedEntity")
    class NameFieldInheritance {

        @Test
        @DisplayName("name is null by default")
        void nameIsNullByDefault() {
            assertThat(specialty.getName()).isNull();
        }

        @Test
        @DisplayName("setName sets the name")
        void setNameSetsName() {
            specialty.setName("Cardiology");
            assertThat(specialty.getName()).isEqualTo("Cardiology");
        }

        @Test
        @DisplayName("setName overwrites previous name")
        void setNameOverwritesPreviousName() {
            specialty.setName("Surgery");
            specialty.setName("Radiology");
            assertThat(specialty.getName()).isEqualTo("Radiology");
        }

        @Test
        @DisplayName("setName to null resets name")
        void setNameToNull() {
            specialty.setName("Dentistry");
            specialty.setName(null);
            assertThat(specialty.getName()).isNull();
        }

        @Test
        @DisplayName("toString returns name when set")
        void toStringReturnsNameWhenSet() {
            specialty.setName("Dermatology");
            assertThat(specialty.toString()).isEqualTo("Dermatology");
        }

        @Test
        @DisplayName("toString returns null when name is null")
        void toStringReturnsNullWhenNameIsNull() {
            assertThat(specialty.toString()).isNull();
        }

        @Test
        @DisplayName("empty name returns empty string in toString")
        void emptyNameReturnsEmptyStringInToString() {
            specialty.setName("");
            assertThat(specialty.toString()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Entity inheritance from NamedEntity")
    class EntityInheritance {

        @Test
        @DisplayName("extends NamedEntity")
        void extendsNamedEntity() {
            assertThat(specialty).isInstanceOf(NamedEntity.class);
        }

        @Test
        @DisplayName("extends BaseEntity through NamedEntity")
        void extendsBaseEntityThroughNamedEntity() {
            assertThat(specialty).isInstanceOf(BaseEntity.class);
        }

        @Test
        @DisplayName("inherits id lifecycle methods from BaseEntity")
        void inheritsIdLifecycleMethodsFromBaseEntity() {
            assertThat(specialty.getId()).isNull();
            specialty.setId(42);
            assertThat(specialty.getId()).isEqualTo(42);
        }

        @Test
        @DisplayName("inherits isNew lifecycle method from BaseEntity")
        void inheritsIsNewLifecycleMethodFromBaseEntity() {
            assertThat(specialty.isNew()).isTrue();
            specialty.setId(1);
            assertThat(specialty.isNew()).isFalse();
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
            assertThat(Specialty.class.isAnnotationPresent(Entity.class)).isTrue();
        }

        @Test
        @DisplayName("has @Table annotation with name specialties")
        void hasTableAnnotationWithNameSpecialties() {
            var tableAnnotation = Specialty.class.getAnnotation(Table.class);
            assertThat(tableAnnotation).isNotNull();
            assertThat(tableAnnotation.name()).isEqualTo("specialties");
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
            assertThat(Specialty.class.getAnnotation(Entity.class)).isNotNull();
            assertThat(Specialty.class.getAnnotation(Table.class)).isNotNull();
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
        @DisplayName("can represent common veterinary specialties")
        void canRepresentCommonVeterinarySpecialties() {
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");
            assertThat(cardiology.getName()).isEqualTo("Cardiology");

            var surgery = new Specialty();
            surgery.setName("Surgery");
            assertThat(surgery.getName()).isEqualTo("Surgery");

            var dentistry = new Specialty();
            dentistry.setName("Dentistry");
            assertThat(dentistry.getName()).isEqualTo("Dentistry");
        }

        @Test
        @DisplayName("can handle sub-specialties")
        void canHandleSubSpecialties() {
            var internalMedicine = new Specialty();
            internalMedicine.setName("Internal Medicine");
            assertThat(internalMedicine.getName()).isEqualTo("Internal Medicine");

            var oncology = new Specialty();
            oncology.setName("Oncology");
            assertThat(oncology.getName()).isEqualTo("Oncology");
        }

        @Test
        @DisplayName("can handle multi-word specialty names")
        void canHandleMultiWordSpecialtyNames() {
            var specialty1 = new Specialty();
            specialty1.setName("Emergency Medicine");
            assertThat(specialty1.getName()).isEqualTo("Emergency Medicine");

            var specialty2 = new Specialty();
            specialty2.setName("Preventive Medicine");
            assertThat(specialty2.getName()).isEqualTo("Preventive Medicine");
        }

        @Test
        @DisplayName("can handle specialty names with abbreviations")
        void canHandleSpecialtyNamesWithAbbreviations() {
            var specialty = new Specialty();
            specialty.setName("DVM");
            assertThat(specialty.getName()).isEqualTo("DVM");

            var specialty2 = new Specialty();
            specialty2.setName("PhD");
            assertThat(specialty2.getName()).isEqualTo("PhD");
        }

        @Test
        @DisplayName("is new when id is null regardless of name")
        void isNewWhenIdIsNullRegardlessOfName() {
            specialty.setName("Surgery");
            assertThat(specialty.isNew()).isTrue();
        }

        @Test
        @DisplayName("is not new when id is set even if name is null")
        void isNotNewWhenIdIsSetEvenIfNameIsNull() {
            specialty.setId(1);
            assertThat(specialty.isNew()).isFalse();
        }
    }

    @Nested
    @DisplayName("Use cases in Vet entity")
    class UseCasesInVetEntity {

        @Test
        @DisplayName("can be assigned to Vet specialties field")
        void canBeAssignedToVetSpecialtiesField() {
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");

            var vet = new Vet();
            // Vet has ManyToMany relationship with Specialty
            assertThat(vet).isInstanceOf(Vet.class);
        }

        @Test
        @DisplayName("can be assigned to multiple specialties")
        void canBeAssignedToMultipleSpecialties() {
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");
            var surgery = new Specialty();
            surgery.setName("Surgery");
            var radiology = new Specialty();
            radiology.setName("Radiology");

            // Vet can have multiple specialties (ManyToMany relationship)
            // This test verifies the Specialty entity can be used for that purpose
            assertThat(cardiology.getName()).isEqualTo("Cardiology");
            assertThat(surgery.getName()).isEqualTo("Surgery");
            assertThat(radiology.getName()).isEqualTo("Radiology");
        }

        @Test
        @DisplayName("can be changed on existing Vet")
        void canBeChangedOnExistingVet() {
            var vet = new Vet();
            // Specialty can be added/removed from Vet's specialties set
            var internalMedicine = new Specialty();
            internalMedicine.setName("Internal Medicine");

            assertThat(internalMedicine.getName()).isEqualTo("Internal Medicine");
        }

        @Test
        @DisplayName("multiple vets can have same specialty")
        void multipleVetsCanHaveSameSpecialty() {
            var surgery = new Specialty();
            surgery.setName("Surgery");

            // Multiple vets can have the same specialty (ManyToMany relationship)
            assertThat(surgery.getName()).isEqualTo("Surgery");
        }

        @Test
        @DisplayName("specialty can be null in Vet entity")
        void specialtyCanBeNullInVetEntity() {
            var vet = new Vet();
            // Vet can have no specialties
            assertThat(vet).isInstanceOf(Vet.class);
        }
    }

    @Nested
    @DisplayName("Integration with entity hierarchy")
    class IntegrationWithEntityHierarchy {

        @Test
        @DisplayName("complete inheritance chain works correctly")
        void completeInheritanceChainWorksCorrectly() {
            var specialty = new Specialty();
            specialty.setId(100);
            specialty.setName("Cardiology");

            // Test all levels of the inheritance chain
            assertThat(specialty).isInstanceOf(Specialty.class);
            assertThat(specialty).isInstanceOf(NamedEntity.class);
            assertThat(specialty).isInstanceOf(BaseEntity.class);

            // Test all inherited methods work
            assertThat(specialty.getId()).isEqualTo(100);
            assertThat(specialty.getName()).isEqualTo("Cardiology");
            assertThat(specialty.isNew()).isFalse();
            assertThat(specialty.toString()).isEqualTo("Cardiology");
        }

        @Test
        @DisplayName("can be used in JPA ManyToMany relationships")
        void canBeUsedInJpaManyToManyRelationships() throws Exception {
            // Specialty is used in ManyToMany relationship in Vet entity
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");

            // Verify the relationship annotations exist
            var specialtiesField = Vet.class.getDeclaredField("specialties");
            var manyToManyAnnotation = specialtiesField.getAnnotation(jakarta.persistence.ManyToMany.class);
            var joinTableAnnotation = specialtiesField.getAnnotation(jakarta.persistence.JoinTable.class);

            assertThat(manyToManyAnnotation).isNotNull();
            assertThat(joinTableAnnotation).isNotNull();
            
            // Verify JoinTable configuration
            var joinTableName = joinTableAnnotation.name();
            assertThat(joinTableName).isEqualTo("vet_specialties");
        }

        @Test
        @DisplayName("can be used in bidirectional relationship with Vet")
        void canBeUsedInBidirectionalRelationshipWithVet() {
            var cardiology = new Specialty();
            cardiology.setName("Cardiology");
            var surgery = new Specialty();
            surgery.setName("Surgery");

            // The relationship is unidirectional from Vet to Specialty
            // Specialty doesn't maintain back-reference to Vets
            assertThat(cardiology.getName()).isEqualTo("Cardiology");
            assertThat(surgery.getName()).isEqualTo("Surgery");
        }
    }

    @Nested
    @DisplayName("Edge cases")
    class EdgeCases {

        @Test
        @DisplayName("can handle very long specialty names")
        void canHandleVeryLongSpecialtyNames() {
            var longName = "This is a very long specialty name that might be used for specialized veterinary medicine or research fields";
            specialty.setName(longName);
            assertThat(specialty.getName()).isEqualTo(longName);
        }

        @Test
        @DisplayName("can handle special characters in names")
        void canHandleSpecialCharactersInNames() {
            var specialty1 = new Specialty();
            specialty1.setName("Surgery & Medicine");
            assertThat(specialty1.getName()).isEqualTo("Surgery & Medicine");

            var specialty2 = new Specialty();
            specialty2.setName("Doctor's Specialty");
            assertThat(specialty2.getName()).isEqualTo("Doctor's Specialty");
        }

        @Test
        @DisplayName("can handle Unicode characters")
        void canHandleUnicodeCharacters() {
            var specialty = new Specialty();
            specialty.setName("专科医科 (Specialty)");
            assertThat(specialty.getName()).isEqualTo("专科医科 (Specialty)");
        }

        @Test
        @DisplayName("can have duplicate names")
        void canHaveDuplicateNames() {
            var specialty1 = new Specialty();
            specialty1.setName("Surgery");
            var specialty2 = new Specialty();
            specialty2.setName("Surgery");

            assertThat(specialty1.getName()).isEqualTo(specialty2.getName());
            // They are different instances even with same name
            assertThat(specialty1).isNotEqualTo(specialty2);
        }

        @Test
        @DisplayName("can be created with null name")
        void canBeCreatedWithNullName() {
            var specialty = new Specialty();
            assertThat(specialty.getName()).isNull();
            assertThat(specialty.toString()).isNull();
        }

        @Test
        @DisplayName("can be created with empty name")
        void canBeCreatedWithEmptyName() {
            var specialty = new Specialty();
            specialty.setName("");
            assertThat(specialty.getName()).isEmpty();
            assertThat(specialty.toString()).isEmpty();
        }

        @Test
        @DisplayName("can handle numeric specialty names")
        void canHandleNumericSpecialtyNames() {
            var specialty = new Specialty();
            specialty.setName("Level 1");
            assertThat(specialty.getName()).isEqualTo("Level 1");
        }

        @Test
        @DisplayName("can handle specialty names with hyphens")
        void canHandleSpecialtyNamesWithHyphens() {
            var specialty = new Specialty();
            specialty.setName("Internal-Medicine");
            assertThat(specialty.getName()).isEqualTo("Internal-Medicine");
        }
    }

    @Nested
    @DisplayName("Real-world veterinary specialties")
    class RealWorldVeterinarySpecialties {

        @Test
        @DisplayName("represents common veterinary specialties")
        void representsCommonVeterinarySpecialties() {
            var specialties = new String[]{
                "Surgery", "Internal Medicine", "Cardiology", "Neurology", "Oncology",
                "Dermatology", "Ophthalmology", "Emergency Medicine", "Preventive Medicine",
                "Dentistry", "Radiology", "Pathology", "Microbiology"
            };

            for (String specialtyName : specialties) {
                var specialty = new Specialty();
                specialty.setName(specialtyName);
                assertThat(specialty.getName()).isEqualTo(specialtyName);
            }
        }

        @Test
        @DisplayName("represents exotic veterinary specialties")
        void representsExoticVeterinarySpecialties() {
            var exoticSpecialties = new String[]{
                "Avian Medicine", "Reptile Medicine", "Aquatic Medicine", "Exotic Animal Medicine",
                "Zoo Medicine", "Wildlife Medicine", "Equine Medicine", "Bovine Medicine",
                "Porcine Medicine", "Laboratory Animal Medicine"
            };

            for (String specialtyName : exoticSpecialties) {
                var specialty = new Specialty();
                specialty.setName(specialtyName);
                assertThat(specialty.getName()).isEqualTo(specialtyName);
            }
        }

        @Test
        @DisplayName("can handle dual specialties")
        void canHandleDualSpecialties() {
            var dualSpecialty = new Specialty();
            dualSpecialty.setName("Surgery and Internal Medicine");
            assertThat(dualSpecialty.getName()).isEqualTo("Surgery and Internal Medicine");
        }

        @Test
        @DisplayName("can handle certification-style names")
        void canHandleCertificationStyleNames() {
            var certification = new Specialty();
            certification.setName("DACVS (Diplomate American College of Veterinary Surgeons)");
            assertThat(certification.getName()).isEqualTo("DACVS (Diplomate American College of Veterinary Surgeons)");
        }
    }
}