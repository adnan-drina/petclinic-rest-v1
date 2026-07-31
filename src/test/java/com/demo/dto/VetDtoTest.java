package com.demo.dto;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;

class VetDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testVetDtoCreation() {
        VetDto vetDto = new VetDto();
        assertNotNull(vetDto);
        assertNull(vetDto.getId());
        assertNull(vetDto.getFirstName());
        assertNull(vetDto.getLastName());
        assertNotNull(vetDto.getSpecialties());
        assertTrue(vetDto.getSpecialties().isEmpty());
    }

    @Test
    void testVetDtoSettersAndGetters() {
        VetDto vetDto = new VetDto();
        
        vetDto.setId(1);
        assertEquals(1, vetDto.getId());
        
        vetDto.setFirstName("John");
        assertEquals("John", vetDto.getFirstName());
        
        vetDto.setLastName("Doe");
        assertEquals("Doe", vetDto.getLastName());
        
        SpecialtyDto specialty = new SpecialtyDto();
        specialty.setId(1);
        specialty.setName("Surgery");
        vetDto.setSpecialties(java.util.Collections.singletonList(specialty));
        
        assertEquals(1, vetDto.getSpecialties().size());
        assertEquals("Surgery", vetDto.getSpecialties().get(0).getName());
    }

    @Test
    void testVetDtoEqualsAndHashCode() {
        VetDto vetDto1 = new VetDto();
        vetDto1.setId(1);
        vetDto1.setFirstName("John");
        vetDto1.setLastName("Doe");
        
        VetDto vetDto2 = new VetDto();
        vetDto2.setId(1);
        vetDto2.setFirstName("John");
        vetDto2.setLastName("Doe");
        
        assertEquals(vetDto1, vetDto2);
        assertEquals(vetDto1.hashCode(), vetDto2.hashCode());
        
        vetDto2.setId(2);
        assertNotEquals(vetDto1, vetDto2);
        assertNotEquals(vetDto1.hashCode(), vetDto2.hashCode());
    }

    @Test
    void testValidationConstraints() {
        VetDto vetDto = new VetDto();
        vetDto.setFirstName(""); // Empty string should be invalid
        vetDto.setLastName(""); // Empty string should be invalid
        
        Set<ConstraintViolation<VetDto>> violations = validator.validate(vetDto);
        
        // At least one violation expected for empty names
        assertFalse(violations.isEmpty());
    }
}