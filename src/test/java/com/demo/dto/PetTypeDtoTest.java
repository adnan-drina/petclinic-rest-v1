package com.demo.dto;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

class PetTypeDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCreation() {
        PetTypeDto dto = new PetTypeDto(1, "Dog");
        assertNotNull(dto);
        assertEquals(Integer.valueOf(1), dto.id());
        assertEquals("Dog", dto.name());
    }

    @Test
    void testCreationWithNullId() {
        PetTypeDto dto = new PetTypeDto(null, "Dog");
        assertNotNull(dto);
        assertNull(dto.id());
        assertEquals("Dog", dto.name());
    }

    @Test
    void testCreationWithNullName() {
        PetTypeDto dto = new PetTypeDto(1, null);
        assertNotNull(dto);
        assertEquals(Integer.valueOf(1), dto.id());
        assertNull(dto.name());
    }

    @Test
    void testCreationWithEmptyName() {
        PetTypeDto dto = new PetTypeDto(1, "");
        assertNotNull(dto);
        assertEquals(Integer.valueOf(1), dto.id());
        assertEquals("", dto.name());
    }

    @Test
    void testValidPetTypeDto() {
        PetTypeDto dto = new PetTypeDto(1, "Dog");
        Set<ConstraintViolation<PetTypeDto>> violations = validator.validate(dto);
        assertEquals(0, violations.size());
    }

    @Test
    void testInvalidNegativeId() {
        PetTypeDto dto = new PetTypeDto(-1, "Dog");
        Set<ConstraintViolation<PetTypeDto>> violations = validator.validate(dto);
        
        assertEquals(1, violations.size());
        ConstraintViolation<PetTypeDto> violation = violations.iterator().next();
        assertEquals("id", violation.getPropertyPath().toString());
    }

    @Test
    void testInvalidNullName() {
        PetTypeDto dto = new PetTypeDto(1, null);
        Set<ConstraintViolation<PetTypeDto>> violations = validator.validate(dto);
        
        assertEquals(1, violations.size());
        ConstraintViolation<PetTypeDto> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void testInvalidEmptyName() {
        PetTypeDto dto = new PetTypeDto(1, "");
        Set<ConstraintViolation<PetTypeDto>> violations = validator.validate(dto);
        
        assertEquals(1, violations.size());
        ConstraintViolation<PetTypeDto> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void testInvalidTooLongName() {
        String longName = "a".repeat(81); // Exceeds max size of 80
        PetTypeDto dto = new PetTypeDto(1, longName);
        Set<ConstraintViolation<PetTypeDto>> violations = validator.validate(dto);
        
        assertEquals(1, violations.size());
        ConstraintViolation<PetTypeDto> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void testIdAccessor() {
        PetTypeDto dto = new PetTypeDto(42, "Cat");
        assertEquals(Integer.valueOf(42), dto.id());
    }

    @Test
    void testNameAccessor() {
        PetTypeDto dto = new PetTypeDto(1, "Rabbit");
        assertEquals("Rabbit", dto.name());
    }

    @Test
    void testEqualsAndHashCode() {
        PetTypeDto dto1 = new PetTypeDto(1, "Dog");
        PetTypeDto dto2 = new PetTypeDto(1, "Dog");
        PetTypeDto dto3 = new PetTypeDto(2, "Cat");

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        PetTypeDto dto = new PetTypeDto(1, "Dog");
        String result = dto.toString();
        assertTrue(result.contains("PetTypeDto"));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("name=Dog"));
    }
}