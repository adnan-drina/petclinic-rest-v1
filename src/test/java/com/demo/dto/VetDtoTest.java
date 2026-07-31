package com.demo.dto;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class VetDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCreation() {
        List<SpecialtyDto> specialties = new ArrayList<>();
        specialties.add(new SpecialtyDto(1, "Cardiology"));
        
        VetDto dto = new VetDto(1, "John", "Doe", specialties);
        assertNotNull(dto);
        assertEquals(Integer.valueOf(1), dto.id());
        assertEquals("John", dto.firstName());
        assertEquals("Doe", dto.lastName());
        assertEquals(specialties, dto.specialties());
    }

    @Test
    void testCreationWithNullId() {
        List<SpecialtyDto> specialties = new ArrayList<>();
        VetDto dto = new VetDto(null, "John", "Doe", specialties);
        assertNotNull(dto);
        assertNull(dto.id());
        assertEquals("John", dto.firstName());
        assertEquals("Doe", dto.lastName());
    }

    @Test
    void testCreationWithNullSpecialties() {
        VetDto dto = new VetDto(1, "John", "Doe", null);
        assertNotNull(dto);
        assertEquals(Integer.valueOf(1), dto.id());
        assertEquals("John", dto.firstName());
        assertEquals("Doe", dto.lastName());
        assertEquals(0, dto.specialties().size());
    }

    @Test
    void testCreationWithEmptySpecialties() {
        List<SpecialtyDto> specialties = new ArrayList<>();
        VetDto dto = new VetDto(1, "John", "Doe", specialties);
        assertNotNull(dto);
        assertEquals(0, dto.specialties().size());
    }

    @Test
    void testValidVetDto() {
        List<SpecialtyDto> specialties = new ArrayList<>();
        specialties.add(new SpecialtyDto(1, "Cardiology"));
        
        VetDto dto = new VetDto(1, "John", "Doe", specialties);
        Set<ConstraintViolation<VetDto>> violations = validator.validate(dto);
        assertEquals(0, violations.size());
    }

    @Test
    void testInvalidNegativeId() {
        List<SpecialtyDto> specialties = new ArrayList<>();
        VetDto dto = new VetDto(-1, "John", "Doe", specialties);
        Set<ConstraintViolation<VetDto>> violations = validator.validate(dto);
        
        assertEquals(1, violations.size());
        ConstraintViolation<VetDto> violation = violations.iterator().next();
        assertEquals("id", violation.getPropertyPath().toString());
    }

    @Test
    void shouldFailValidationForNullFirstName() {
        List<SpecialtyDto> specialties = new ArrayList<>();
        VetDto dto = new VetDto(1, null, "Doe", specialties);
        Set<ConstraintViolation<VetDto>> violations = validator.validate(dto);
        
        assertEquals(1, violations.size());
        ConstraintViolation<VetDto> violation = violations.iterator().next();
        assertEquals("firstName", violation.getPropertyPath().toString());
    }

    @Test
    void shouldFailValidationForNullLastName() {
        List<SpecialtyDto> specialties = new ArrayList<>();
        VetDto dto = new VetDto(1, "John", null, specialties);
        Set<ConstraintViolation<VetDto>> violations = validator.validate(dto);
        
        assertEquals(1, violations.size());
        ConstraintViolation<VetDto> violation = violations.iterator().next();
        assertEquals("lastName", violation.getPropertyPath().toString());
    }

    @Test
    void shouldFailValidationForInvalidFirstNames() {
        List<SpecialtyDto> specialties = new ArrayList<>();
        
        // Test empty name
        VetDto dto = new VetDto(1, "", "Doe", specialties);
        Set<ConstraintViolation<VetDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        ConstraintViolation<VetDto> violation = violations.iterator().next();
        assertEquals("firstName", violation.getPropertyPath().toString());
        
        // Test non-alphabetic name
        VetDto dto2 = new VetDto(1, "John123", "Doe", specialties);
        Set<ConstraintViolation<VetDto>> violations2 = validator.validate(dto2);
        assertEquals(1, violations2.size());
        ConstraintViolation<VetDto> violation2 = violations2.iterator().next();
        assertEquals("firstName", violation2.getPropertyPath().toString());
        
        // Test too long name
        String longName = "a".repeat(31); // Exceeds max size of 30
        VetDto dto3 = new VetDto(1, longName, "Doe", specialties);
        Set<ConstraintViolation<VetDto>> violations3 = validator.validate(dto3);
        assertEquals(1, violations3.size());
        ConstraintViolation<VetDto> violation3 = violations3.iterator().next();
        assertEquals("firstName", violation3.getPropertyPath().toString());
    }

    @Test
    void testInvalidNullSpecialties() {
        VetDto dto = new VetDto(1, "John", "Doe", null);
        Set<ConstraintViolation<VetDto>> violations = validator.validate(dto);
        
        assertEquals(1, violations.size());
        ConstraintViolation<VetDto> violation = violations.iterator().next();
        assertEquals("specialties", violation.getPropertyPath().toString());
    }

    @Test
    void testSpecialtiesAccessor() {
        List<SpecialtyDto> specialties = new ArrayList<>();
        specialties.add(new SpecialtyDto(1, "Cardiology"));
        specialties.add(new SpecialtyDto(2, "Radiology"));
        
        VetDto dto = new VetDto(1, "John", "Doe", specialties);
        List<SpecialtyDto> result = dto.specialties();
        
        assertEquals(2, result.size());
        assertEquals("Cardiology", result.get(0).name());
        assertEquals("Radiology", result.get(1).name());
    }

    @Test
    void testSpecialtiesAccessorWithNull() {
        VetDto dto = new VetDto(1, "John", "Doe", null);
        List<SpecialtyDto> result = dto.specialties();
        
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testAddSpecialtiesItem() {
        List<SpecialtyDto> specialties = new ArrayList<>();
        specialties.add(new SpecialtyDto(1, "Cardiology"));
        
        VetDto dto = new VetDto(1, "John", "Doe", specialties);
        SpecialtyDto newSpecialty = new SpecialtyDto(2, "Radiology");
        
        VetDto updatedDto = dto.addSpecialtiesItem(newSpecialty);
        
        assertNotNull(updatedDto);
        assertEquals(2, updatedDto.specialties().size());
        assertEquals("Cardiology", updatedDto.specialties().get(0).name());
        assertEquals("Radiology", updatedDto.specialties().get(1).name());
        assertEquals(dto.id(), updatedDto.id());
        assertEquals(dto.firstName(), updatedDto.firstName());
        assertEquals(dto.lastName(), updatedDto.lastName());
    }

    @Test
    void testAddSpecialtiesItemWithNullList() {
        VetDto dto = new VetDto(1, "John", "Doe", null);
        SpecialtyDto newSpecialty = new SpecialtyDto(1, "Cardiology");
        
        VetDto updatedDto = dto.addSpecialtiesItem(newSpecialty);
        
        assertNotNull(updatedDto);
        assertEquals(1, updatedDto.specialties().size());
        assertEquals("Cardiology", updatedDto.specialties().get(0).name());
    }

    @Test
    void testIdAccessor() {
        List<SpecialtyDto> specialties = new ArrayList<>();
        VetDto dto = new VetDto(42, "John", "Doe", specialties);
        assertEquals(Integer.valueOf(42), dto.id());
    }

    @Test
    void testFirstNameAccessor() {
        List<SpecialtyDto> specialties = new ArrayList<>();
        VetDto dto = new VetDto(1, "Jane", "Smith", specialties);
        assertEquals("Jane", dto.firstName());
    }

    @Test
    void testLastNameAccessor() {
        List<SpecialtyDto> specialties = new ArrayList<>();
        VetDto dto = new VetDto(1, "John", "Smith", specialties);
        assertEquals("Smith", dto.lastName());
    }

    @Test
    void testEqualsAndHashCode() {
        List<SpecialtyDto> specialties1 = new ArrayList<>();
        specialties1.add(new SpecialtyDto(1, "Cardiology"));
        
        List<SpecialtyDto> specialties2 = new ArrayList<>();
        specialties2.add(new SpecialtyDto(1, "Cardiology"));
        
        List<SpecialtyDto> specialties3 = new ArrayList<>();
        specialties3.add(new SpecialtyDto(2, "Radiology"));
        
        VetDto dto1 = new VetDto(1, "John", "Doe", specialties1);
        VetDto dto2 = new VetDto(1, "John", "Doe", specialties2);
        VetDto dto3 = new VetDto(2, "Jane", "Smith", specialties3);

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        List<SpecialtyDto> specialties = new ArrayList<>();
        specialties.add(new SpecialtyDto(1, "Cardiology"));
        
        VetDto dto = new VetDto(1, "John", "Doe", specialties);
        String result = dto.toString();
        assertTrue(result.contains("VetDto"));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("firstName=John"));
        assertTrue(result.contains("lastName=Doe"));
    }
}