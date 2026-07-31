package com.demo.dto;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;

class SpecialtyDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testSpecialtyDtoCreation() {
        SpecialtyDto specialtyDto = new SpecialtyDto();
        assertNotNull(specialtyDto);
        assertNull(specialtyDto.getId());
        assertNull(specialtyDto.getName());
    }

    @Test
    void testSpecialtyDtoSettersAndGetters() {
        SpecialtyDto specialtyDto = new SpecialtyDto();
        
        specialtyDto.setId(1);
        assertEquals(1, specialtyDto.getId());
        
        specialtyDto.setName("Surgery");
        assertEquals("Surgery", specialtyDto.getName());
    }

    @Test
    void testSpecialtyDtoEqualsAndHashCode() {
        SpecialtyDto specialty1 = new SpecialtyDto();
        specialty1.setId(1);
        specialty1.setName("Surgery");
        
        SpecialtyDto specialty2 = new SpecialtyDto();
        specialty2.setId(1);
        specialty2.setName("Surgery");
        
        assertEquals(specialty1, specialty2);
        assertEquals(specialty1.hashCode(), specialty2.hashCode());
        
        specialty2.setId(2);
        assertNotEquals(specialty1, specialty2);
        assertNotEquals(specialty1.hashCode(), specialty2.hashCode());
    }

    @Test
    void testValidationConstraints() {
        SpecialtyDto specialtyDto = new SpecialtyDto();
        specialtyDto.setName(""); // Empty string should be invalid
        
        Set<ConstraintViolation<SpecialtyDto>> violations = validator.validate(specialtyDto);
        
        // At least one violation expected for empty name
        assertFalse(violations.isEmpty());
    }
}