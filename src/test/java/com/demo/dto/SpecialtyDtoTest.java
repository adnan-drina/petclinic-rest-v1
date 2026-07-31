package com.demo.dto;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

class SpecialtyDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCreation() {
        SpecialtyDto dto = new SpecialtyDto(1, "Cardiology");
        assertNotNull(dto);
        assertEquals(Integer.valueOf(1), dto.id());
        assertEquals("Cardiology", dto.name());
    }

    @Test
    void testCreationWithNullId() {
        SpecialtyDto dto = new SpecialtyDto(null, "Cardiology");
        assertNotNull(dto);
        assertNull(dto.id());
        assertEquals("Cardiology", dto.name());
    }

    @Test
    void testCreationWithNullName() {
        SpecialtyDto dto = new SpecialtyDto(1, null);
        assertNotNull(dto);
        assertEquals(Integer.valueOf(1), dto.id());
        assertNull(dto.name());
    }

    @Test
    void testCreationWithEmptyName() {
        SpecialtyDto dto = new SpecialtyDto(1, "");
        assertNotNull(dto);
        assertEquals(Integer.valueOf(1), dto.id());
        assertEquals("", dto.name());
    }

    @Test
    void testValidSpecialtyDto() {
        SpecialtyDto dto = new SpecialtyDto(1, "Cardiology");
        Set<ConstraintViolation<SpecialtyDto>> violations = validator.validate(dto);
        assertEquals(0, violations.size());
    }

    @Test
    void testInvalidNegativeId() {
        SpecialtyDto dto = new SpecialtyDto(-1, "Cardiology");
        Set<ConstraintViolation<SpecialtyDto>> violations = validator.validate(dto);
        
        assertEquals(1, violations.size());
        ConstraintViolation<SpecialtyDto> violation = violations.iterator().next();
        assertEquals("id", violation.getPropertyPath().toString());
    }

    @Test
    void testInvalidNullName() {
        SpecialtyDto dto = new SpecialtyDto(1, null);
        Set<ConstraintViolation<SpecialtyDto>> violations = validator.validate(dto);
        
        assertEquals(1, violations.size());
        ConstraintViolation<SpecialtyDto> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void testInvalidEmptyName() {
        SpecialtyDto dto = new SpecialtyDto(1, "");
        Set<ConstraintViolation<SpecialtyDto>> violations = validator.validate(dto);
        
        assertEquals(1, violations.size());
        ConstraintViolation<SpecialtyDto> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void testInvalidTooLongName() {
        String longName = "a".repeat(81); // Exceeds max size of 80
        SpecialtyDto dto = new SpecialtyDto(1, longName);
        Set<ConstraintViolation<SpecialtyDto>> violations = validator.validate(dto);
        
        assertEquals(1, violations.size());
        ConstraintViolation<SpecialtyDto> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void testIdAccessor() {
        SpecialtyDto dto = new SpecialtyDto(42, "Neurology");
        assertEquals(Integer.valueOf(42), dto.id());
    }

    @Test
    void testNameAccessor() {
        SpecialtyDto dto = new SpecialtyDto(1, "Radiology");
        assertEquals("Radiology", dto.name());
    }

    @Test
    void testEqualsAndHashCode() {
        SpecialtyDto dto1 = new SpecialtyDto(1, "Cardiology");
        SpecialtyDto dto2 = new SpecialtyDto(1, "Cardiology");
        SpecialtyDto dto3 = new SpecialtyDto(2, "Neurology");

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        SpecialtyDto dto = new SpecialtyDto(1, "Cardiology");
        String result = dto.toString();
        assertTrue(result.contains("SpecialtyDto"));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("name=Cardiology"));
    }
}