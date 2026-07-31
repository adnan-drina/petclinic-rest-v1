package com.demo.dto;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

class RoleDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testCreation() {
        RoleDto dto = new RoleDto("ADMIN");
        assertNotNull(dto);
        assertEquals("ADMIN", dto.name());
    }

    @Test
    void testCreationWithNullName() {
        RoleDto dto = new RoleDto(null);
        assertNotNull(dto);
        assertNull(dto.name());
    }

    @Test
    void testCreationWithEmptyName() {
        RoleDto dto = new RoleDto("");
        assertNotNull(dto);
        assertEquals("", dto.name());
    }

    @Test
    void testValidRoleDto() {
        RoleDto dto = new RoleDto("USER");
        Set<ConstraintViolation<RoleDto>> violations = validator.validate(dto);
        assertEquals(0, violations.size());
    }

    @Test
    void testInvalidNullName() {
        RoleDto dto = new RoleDto(null);
        Set<ConstraintViolation<RoleDto>> violations = validator.validate(dto);
        
        assertEquals(1, violations.size());
        ConstraintViolation<RoleDto> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void testInvalidEmptyName() {
        RoleDto dto = new RoleDto("");
        Set<ConstraintViolation<RoleDto>> violations = validator.validate(dto);
        
        assertEquals(1, violations.size());
        ConstraintViolation<RoleDto> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void testInvalidTooLongName() {
        String longName = "a".repeat(81); // Exceeds max size of 80
        RoleDto dto = new RoleDto(longName);
        Set<ConstraintViolation<RoleDto>> violations = validator.validate(dto);
        
        assertEquals(1, violations.size());
        ConstraintViolation<RoleDto> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void testNameAccessor() {
        RoleDto dto = new RoleDto("MANAGER");
        assertEquals("MANAGER", dto.name());
    }

    @Test
    void testEqualsAndHashCode() {
        RoleDto dto1 = new RoleDto("ADMIN");
        RoleDto dto2 = new RoleDto("ADMIN");
        RoleDto dto3 = new RoleDto("USER");

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        RoleDto dto = new RoleDto("ADMIN");
        String result = dto.toString();
        assertTrue(result.contains("RoleDto"));
        assertTrue(result.contains("name=ADMIN"));
    }
}